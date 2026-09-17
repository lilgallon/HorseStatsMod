package dev.gallon.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.logging.LogUtils;
import dev.gallon.domain.I18nKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.players.ProfileResolver;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class UserCacheService {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final @NotNull LoadingCache<UUID, Optional<String>> USERNAME_CACHE = CacheBuilder
            .newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull Optional<String> load(@NotNull UUID key) {
                    ProfileResolver profileResolver = Minecraft.getInstance()
                            .services()
                            .profileResolver();
                    scheduleLookup(
                            key,
                            ForkJoinPool.commonPool(),
                            ownerId -> fetchUsername(profileResolver, ownerId),
                            UserCacheService::cacheUsername
                    );
                    return Optional.of(I18n.get(I18nKeys.LOADING));
                }
            });

    private UserCacheService() {
    }

    static @NotNull Optional<String> getUsername(@NotNull UUID key) {
        try {
            return USERNAME_CACHE.getUnchecked(key);
        } catch (RuntimeException exception) {
            LOGGER.warn("Could not start the profile lookup for horse owner {}", key, exception);
            return Optional.empty();
        }
    }

    private static @NotNull Optional<String> fetchUsername(
            @NotNull ProfileResolver profileResolver,
            @NotNull UUID key
    ) {
        return profileResolver.fetchById(key)
                .map(profile -> profile.name());
    }

    private static void cacheUsername(@NotNull UUID key, @NotNull Optional<String> username) {
        USERNAME_CACHE.put(key, username);
    }

    static void scheduleLookup(
            @NotNull UUID key,
            @NotNull Executor executor,
            @NotNull Function<UUID, Optional<String>> resolver,
            @NotNull BiConsumer<UUID, Optional<String>> cacheWriter
    ) {
        Runnable lookup = () -> {
            Optional<String> username;
            try {
                username = resolver.apply(key);
                if (username == null) {
                    username = Optional.empty();
                }
            } catch (RuntimeException exception) {
                LOGGER.warn("Could not resolve horse owner {}", key, exception);
                username = Optional.empty();
            }

            try {
                cacheWriter.accept(key, username);
            } catch (RuntimeException exception) {
                LOGGER.warn("Could not cache the resolved horse owner {}", key, exception);
            }
        };

        try {
            executor.execute(lookup);
        } catch (RuntimeException exception) {
            LOGGER.warn("Could not schedule the profile lookup for horse owner {}", key, exception);
            try {
                cacheWriter.accept(key, Optional.empty());
            } catch (RuntimeException cacheException) {
                LOGGER.warn("Could not cache the failed horse owner lookup {}", key, cacheException);
            }
        }
    }
}
