package dev.gallon.services;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserCacheServiceTest {
    @Test
    void cachesAnEmptyResultWhenTheResolverFails() {
        UUID ownerId = UUID.randomUUID();
        AtomicReference<Optional<String>> cachedValue = new AtomicReference<>();

        UserCacheService.scheduleLookup(
                ownerId,
                Runnable::run,
                ignored -> {
                    throw new IllegalStateException("profile service unavailable");
                },
                (ignored, value) -> cachedValue.set(value)
        );

        assertEquals(Optional.empty(), cachedValue.get());
    }

    @Test
    void cachesAnEmptyResultWhenSchedulingFails() {
        UUID ownerId = UUID.randomUUID();
        AtomicReference<Optional<String>> cachedValue = new AtomicReference<>();
        Executor rejectingExecutor = ignored -> {
            throw new IllegalStateException("executor unavailable");
        };

        UserCacheService.scheduleLookup(
                ownerId,
                rejectingExecutor,
                ignored -> Optional.of("Owner"),
                (ignored, value) -> cachedValue.set(value)
        );

        assertEquals(Optional.empty(), cachedValue.get());
    }
}
