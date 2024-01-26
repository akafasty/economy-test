package com.github.akafasty.economy.repository;

import com.avaje.ebean.validation.NotNull;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.api.repository.IEconomyUserRepository;
import com.github.akafasty.economy.model.EconomyUserImpl;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;

public class MySQLEconomyUsersRepositoryImpl implements IEconomyUserRepository {

    private Connection connection;

    public MySQLEconomyUsersRepositoryImpl(ConfigurationSection configuration) {

        String host = configuration.getString("host");
        String database = configuration.getString("database");
        String user = configuration.getString("user");
        String password = configuration.getString("password");

        String jdbcUrl = String.format("jdbc:mysql://%s/%s", host, database);

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Connection");

            this.connection = DriverManager.getConnection(jdbcUrl, user, password);

            init();

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

    @Override
    public void init() {

        try (PreparedStatement statement = connection.prepareStatement("create table if not exists economy_users (username varchar(32) primary key, balance decimal);")) {

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
                        .balance(result.getBigDecimal("balance"))
                        .build();

            }
        }

        catch (Exception exception) { exception.printStackTrace(); }

        return null;

    }

    @Override
    public void insertOne(IEconomyUser user) {

        try (PreparedStatement statement = connection.prepareStatement("insert into economy_users (" +
                "username, " +
                "balance)" +
                "values (?, ?) " +
                "on duplicate key update balance = values(balance);")) {

            statement.setString(1, user.getUsername());
            statement.setBigDecimal(2, user.getBalance());

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
