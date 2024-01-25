package com.github.akafasty.economy.api.model;

import com.avaje.ebean.validation.NotNull;

import javax.annotation.Nullable;
import java.math.BigDecimal;

public interface IEconomyUser {

    @NotNull String getUsername();

    @Nullable BigDecimal getBalance();

    void setBalance(BigDecimal balance);

}
