package com.github.akafasty.economy.command;

import com.github.akafasty.economy.api.controller.IEconomyUsersController;
import com.github.akafasty.economy.api.model.IEconomyUser;
import com.github.akafasty.economy.command.child.AddArgument;
import com.github.akafasty.economy.command.child.PayArgument;
import com.github.akafasty.economy.command.child.RemoveArgument;
import com.github.akafasty.economy.command.child.SetArgument;
import com.github.akafasty.economy.util.CommandBase;
import com.github.akafasty.economy.util.NumberFormatter;
import com.github.akafasty.economy.util.context.CommandContext;

import java.util.concurrent.CompletableFuture;

public class CoinsCommand extends CommandBase {

    private final IEconomyUsersController controller;

    public CoinsCommand(IEconomyUsersController controller) {

        super("coins", "Main command for managing balances.", "§c/coins", null, "money", "balance");
        this.controller = controller;

        withArgument("definir", new SetArgument(controller));
        withArgument("adicionar", new AddArgument(controller));
        withArgument("remover", new RemoveArgument(controller));

        withArgument("enviar", new PayArgument(controller));

    }

    @Override
    protected void executeEmpty(CommandContext context) {

        IEconomyUser economyUser = controller.getUser(context.getSender().getName().toLowerCase());

        if (economyUser == null) context.getSender().sendMessage("§cVocê não foi localizado em nosso banco de dados.");
        else context.getSender().sendMessage(String.format("§eSaldo: §f%s", NumberFormatter.numberToString(economyUser.getBalance())));

    }

    @Override
    protected void executeArgs(CommandContext context) {

        if (context.getArguments().length != 1) {
            context.getSender().sendMessage("§cUtilize: \"/coins <jogador>\".");
            return;
        }

        CompletableFuture<IEconomyUser> userFuture = CompletableFuture.supplyAsync(() -> controller.getUser(context.getAsString(0)));

        userFuture.whenCompleteAsync((economyUser, throwable) -> {

            if (economyUser == null || throwable != null) {
                context.getSender().sendMessage("§cEste jogador não foi localizado em nosso banco de dados.");
                return;
            }

            context.getSender().sendMessage(String.format("§eSaldo de %s: §f%s", economyUser.getUsername(), NumberFormatter.numberToString(economyUser.getBalance())));

        });

    }
}
