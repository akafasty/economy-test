package com.github.akafasty.economy.cache;

import com.github.akafasty.economy.api.cache.IEconomyUsersLocalCache;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class EconomyUsersCacheImpl implements IEconomyUsersLocalCache {

    private final Cache<String, IEconomyUser> cache = Caffeine.newBuilder()
            .build();

    @Override
    public Cache<String, IEconomyUser> cacheInstance() {
        return cache;
    }
}
