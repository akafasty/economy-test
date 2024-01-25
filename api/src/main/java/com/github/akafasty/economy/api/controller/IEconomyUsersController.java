package com.github.akafasty.economy.api.controller;

import com.avaje.ebean.validation.NotNull;
import com.github.akafasty.economy.api.cache.IEconomyUsersLocalCache;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.api.repository.IEconomyUserRepository;

import javax.annotation.Nullable;

public interface IEconomyUsersController {

    @NotNull IEconomyUserRepository getRepository();

    @NotNull IEconomyUsersLocalCache getLocalCache();

    @Nullable IEconomyUser getUser(String username);

}
