package dev.gallon.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import dev.gallon.domain.I18nKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserCacheService {
    public static final @NotNull LoadingCache<UUID, Optional<String>> usernameCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull Optional<String> load(@NotNull UUID key) {
                    CompletableFuture.runAsync(() -> {
                        GameProfile playerProfile = new GameProfile(key, null);

                        Optional<ProfileResult> result = Optional.ofNullable(
                                Minecraft.getInstance()
                                        .getMinecraftSessionService()
                                        .fetchProfile(playerProfile.getId(), false)
                        );

                        playerProfile = result.isPresent() ? result.get().profile() : playerProfile;
                        usernameCache.put(key, Optional.ofNullable(playerProfile.getName()));
                    });

                    return Optional.of(I18n.get(I18nKeys.LOADING));
                }
            });
}
