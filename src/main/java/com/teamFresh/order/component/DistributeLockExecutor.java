package com.teamFresh.order.component;

import com.teamFresh.order.enums.LockType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.teamFresh.order.exception.ErrorCode.REDIS_LOCK_ERROR;
import static com.teamFresh.order.exception.ErrorCode.REDIS_LOCK_WAIT;

@RequiredArgsConstructor
@Component
@Slf4j
public class DistributeLockExecutor {

    private final RedissonClient redissonClient;

    public <T> T execute(LockType lockType, Supplier<T> task) {
        RLock lock = redissonClient.getLock(lockType.name());
        try {
            boolean isLocked = lock.tryLock(5, 5, TimeUnit.MILLISECONDS);
            if (!isLocked) {
                throw REDIS_LOCK_WAIT.build(lockType);
            }
            return task.get();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw REDIS_LOCK_ERROR.build(lockType);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
