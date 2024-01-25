package com.github.akafasty.economy.controller;

import com.github.akafasty.economy.api.cache.IEconomyUsersLocalCache;
import com.github.akafasty.economy.api.controller.IEconomyUsersController;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.api.repository.IEconomyUserRepository;
import com.github.akafasty.economy.cache.EconomyUsersCacheImpl;

import javax.annotation.Nullable;

public class EconomyUsersControllerImpl implements IEconomyUsersController {

    private final IEconomyUserRepository repository;
    private final IEconomyUsersLocalCache localCache;

    public EconomyUsersControllerImpl(IEconomyUserRepository repository) {
        this.repository = repository;
        this.localCache = new EconomyUsersCacheImpl();
    }

    @Override
    public IEconomyUserRepository getRepository() {
        return repository;
    }

    @Override
    public IEconomyUsersLocalCache getLocalCache() {
        return localCache;
    }

    @Nullable
    @Override
    public IEconomyUser getUser(String username) {

        IEconomyUser user = null;

        return (user = localCache.cacheInstance().getIfPresent(username.toLowerCase()))
                == null ? repository.selectOne(username) : user;

    }
}
