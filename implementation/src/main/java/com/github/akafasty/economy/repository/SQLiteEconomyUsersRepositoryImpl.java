package com.github.akafasty.economy.repository;

import com.avaje.ebean.validation.NotNull;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.api.repository.IEconomyUserRepository;
import com.github.akafasty.economy.model.EconomyUserImpl;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class SQLiteEconomyUsersRepositoryImpl implements IEconomyUserRepository {

    private Connection connection;

    public SQLiteEconomyUsersRepositoryImpl(Plugin plugin) {

        try {

            File folder = plugin.getDataFolder().toPath()
                    .resolve("storage")
                    .toFile();

            if (!folder.exists())
                folder.mkdirs();

            File file = folder.toPath()
                    .resolve("database.sql")
                    .toFile();

            if (!file.exists())
                file.createNewFile();

            Class.forName("org.sqlite.JDBC");

            this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());

            init();

            plugin.getLogger().log(Level.INFO, "O plugin está utilizando SQLite.");

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

    @Override
    public void init() {

        try (PreparedStatement statement = connection.prepareStatement("create table if not exists " +
                "economy_users(" +
                "username varchar(32) primary key, " +
                "balance bigdecimal " +
                ");")) {

            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

    @Override
    public void shutdown() {

        try {

            if (connection == null || connection.isClosed())
                return;

            connection.close();

        }

        catch (Exception exception) {

            exception.printStackTrace();
            System.out.println("Shutdown method exception was triggered");

        }
    }

    @Override
    public @Nullable IEconomyUser selectOne(String username) {

        try (PreparedStatement statement = connection.prepareStatement("select * from economy_users where username = ?;")) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {

                if (!result.next())
                    return null;

                return EconomyUserImpl.builder()
                        .username(username)
                        .balance(BigDecimal.valueOf(result.getDouble("balance")))
                        .build();

            }
        }

        catch (Exception exception) { exception.printStackTrace(); }

        return null;

    }

    @Override
    public void insertOne(IEconomyUser user) {

        try (PreparedStatement statement = connection.prepareStatement("replace into " +
                "economy_users (username, balance) " +
                "values (?, ?);")) {

            statement.setString(1, user.getUsername());
            statement.setDouble(2, user.getBalance().doubleValue());

            statement.executeUpdate();

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

    @Override
    public void deleteOne(IEconomyUser user) {
        deleteOne(user.getUsername());
    }

    @Override
    public void deleteOne(String username) {

        try (PreparedStatement statement = connection.prepareStatement("delete from economy_users where username = ?")) {

            statement.setString(1, username);

            statement.executeUpdate();

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }
}
