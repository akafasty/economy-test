package com.github.akafasty.economy.api.repository;

import com.avaje.ebean.validation.NotNull;
import com.github.akafasty.economy.api.model.IEconomyUser;

public interface IEconomyUserRepository {

    void init();

    void shutdown();

    @Nullable IEconomyUser selectOne(String username);

    void insertOne(IEconomyUser user);

    void deleteOne(IEconomyUser user);

    void deleteOne(String username);

}
