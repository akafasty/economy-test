package com.github.akafasty.economy.model;

import com.github.akafasty.economy.api.model.IEconomyUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @Builder
public class EconomyUserImpl implements IEconomyUser {

    private final String username;
    private BigDecimal balance;

}
