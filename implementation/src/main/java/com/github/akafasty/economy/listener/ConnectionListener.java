package com.github.akafasty.economy.listener;

import com.github.akafasty.economy.api.controller.IEconomyUsersController;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.model.EconomyUserImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.math.BigDecimal;

public class ConnectionListener implements Listener {

    private final IEconomyUsersController controller;

    public ConnectionListener(IEconomyUsersController controller) {
        this.controller = controller;
    }

    @EventHandler
    protected void playerJoinEventHandler(PlayerJoinEvent event) {

        String username = event.getPlayer().getName();
        IEconomyUser economyUser = controller.getUser(username);

        if (economyUser == null) {

            economyUser = EconomyUserImpl.builder()
                    .username(username)
                    .balance(new BigDecimal("0.0"))
                    .build();

        }

        controller.getLocalCache().cacheInstance().put(username.toLowerCase(), economyUser);

    }

    @EventHandler
    protected void playerQuitEventHandler(PlayerQuitEvent event) {

        String username = event.getPlayer().getName();

        controller.getLocalCache().cacheInstance().invalidate(username.toLowerCase());

    }

}
