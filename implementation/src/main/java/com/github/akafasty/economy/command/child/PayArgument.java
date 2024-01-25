package com.github.akafasty.economy.command.child;

import com.github.akafasty.economy.api.controller.IEconomyUsersController;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.util.NumberFormatter;
import com.github.akafasty.economy.util.child.IArgument;
import com.github.akafasty.economy.util.context.CommandContext;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class PayArgument implements IArgument {

    private final IEconomyUsersController controller;

    public PayArgument(IEconomyUsersController controller) {
        this.controller = controller;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getUsage() {
        return "§cUtilize: \"/coins enviar <jogador> <quantia>\".";
    }

    @Override
    public int getRequiredArgs() {
        return 2;
    }

    @Override
    public void execute(CommandContext context) {

        BigDecimal value = NumberFormatter.stringToNumber(context.getAsString(1));

        if (value == null || value.doubleValue() <= 0.0 || Double.isNaN(value.doubleValue())) {
            context.getSender().sendMessage("§cQuantia inválida.");
            return;
        }

        IEconomyUser economyUser = controller.getUser(context.getSender().getName());

        if (economyUser.getBalance().doubleValue() < value.doubleValue()) {
            context.getSender().sendMessage("§cVocê não possui saldo suficiente para efetuar esta transação.");
            return;
        }

        CompletableFuture<IEconomyUser> userFuture = CompletableFuture.supplyAsync(() -> controller.getUser(context.getAsString(0)));

        userFuture.whenCompleteAsync((targetUser, throwable) -> {

            if (targetUser == null || throwable != null) {
                context.getSender().sendMessage("§cEste jogador não foi localizado em nosso banco de dados.");
                return;
            }

            targetUser.setBalance(targetUser.getBalance().add(value));
            economyUser.setBalance(economyUser.getBalance().subtract(value));

            controller.getRepository().insertOne(targetUser);
            controller.getRepository().insertOne(economyUser);

            context.getSender().sendMessage(String.format("§aVocê enviou §f%s§a coins para §f%s§a.", NumberFormatter.numberToString(value), targetUser.getUsername()));

        });

    }
}
