package com.github.akafasty.economy.command.child;

import com.github.akafasty.economy.api.controller.IEconomyUsersController;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.util.NumberFormatter;
import com.github.akafasty.economy.util.child.IArgument;
import com.github.akafasty.economy.util.context.CommandContext;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class AddArgument implements IArgument {

    private final IEconomyUsersController controller;

    public AddArgument(IEconomyUsersController controller) {
        this.controller = controller;
    }

    @Override
    public String getPermission() {
        return "economy.add";
    }

    @Override
    public String getUsage() {
        return "§cUtilize: \"/coins adicionar <jogador> <quantia>\".";
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

        CompletableFuture<IEconomyUser> userFuture = CompletableFuture.supplyAsync(() -> controller.getUser(context.getAsString(0)));

        userFuture.whenCompleteAsync((targetUser, throwable) -> {

            if (targetUser == null || throwable != null) {
                context.getSender().sendMessage("§cEste jogador não foi localizado em nosso banco de dados.");
                return;
            }

            targetUser.setBalance(targetUser.getBalance().add(value));

            controller.getRepository().insertOne(targetUser);

            context.getSender().sendMessage(String.format("§aVocê adicionou um saldo de §f%s§a coins para §f%s§a.", NumberFormatter.numberToString(value), targetUser.getUsername()));

        });

    }
}
