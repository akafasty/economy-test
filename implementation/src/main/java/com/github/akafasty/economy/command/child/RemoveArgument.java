package com.github.akafasty.economy.command.child;

import com.github.akafasty.economy.api.controller.IEconomyUsersController;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.util.NumberFormatter;
import com.github.akafasty.economy.util.child.IArgument;
import com.github.akafasty.economy.util.context.CommandContext;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class RemoveArgument implements IArgument {

    private final IEconomyUsersController controller;

    public RemoveArgument(IEconomyUsersController controller) {
        this.controller = controller;
    }

    @Override
    public String getPermission() {
        return "economy.remove";
    }

    @Override
    public String getUsage() {
        return "§cUtilize: \"/coins remover <jogador> <quantia>\".";
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

            targetUser.setBalance(targetUser.getBalance().subtract(value));

            controller.getRepository().insertOne(targetUser);

            context.getSender().sendMessage(String.format("§aVocê removou §f%s§a coins do saldo de §f%s§a.", NumberFormatter.numberToString(value), targetUser.getUsername()));

        });

    }
}
