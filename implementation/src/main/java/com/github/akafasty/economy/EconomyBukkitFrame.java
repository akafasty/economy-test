package com.github.akafasty.economy;

import com.github.akafasty.economy.api.controller.IEconomyUsersController;
import com.github.akafasty.economy.api.repository.IEconomyUserRepository;
import com.github.akafasty.economy.command.CoinsCommand;
import com.github.akafasty.economy.controller.EconomyUsersControllerImpl;
import com.github.akafasty.economy.listener.ConnectionListener;
import com.github.akafasty.economy.repository.MySQLEconomyUsersRepositoryImpl;
import com.github.akafasty.economy.repository.SQLiteEconomyUsersRepositoryImpl;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyBukkitFrame extends JavaPlugin {

    private IEconomyUsersController controller;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        setupStorage();

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(controller), this);

        new CoinsCommand(controller);

    }

    @Override
    public void onDisable() {

        if (controller == null || controller.getRepository() == null)
            return;

        controller.getRepository().shutdown();

    }

    protected void setupStorage() {

        FileConfiguration configuration = getConfig();

        IEconomyUserRepository repository = switch (configuration.getString("storage.type").toUpperCase()) {
            case "SQLITE" -> new SQLiteEconomyUsersRepositoryImpl(this);
            case "MYSQL" -> new MySQLEconomyUsersRepositoryImpl(configuration.getConfigurationSection("storage"));
            default -> null;
        };

        if (repository == null) {

            getLogger().severe("Database not connnected.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;

        }

        repository.init();

        controller = new EconomyUsersControllerImpl(repository);

    }

}
