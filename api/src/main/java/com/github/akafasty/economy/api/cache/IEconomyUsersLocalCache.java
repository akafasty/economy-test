package com.github.akafasty.economy.api.cache;

import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.benmanes.caffeine.cache.Cache;

public interface IEconomyUsersLocalCache {

    Cache<String, IEconomyUser> cacheInstance();

}