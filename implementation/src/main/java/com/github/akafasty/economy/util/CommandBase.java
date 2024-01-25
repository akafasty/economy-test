package com.github.akafasty.economy.util;

import com.github.akafasty.economy.util.child.IArgument;
import com.github.akafasty.economy.util.context.CommandContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.util.Arrays;
import java.util.Map;

public abstract class CommandBase extends Command {

    private Map<String, IArgument> childCommandMap;

    public CommandBase(String name, String description, String usage, String permission, String... aliases) {

        super(name, description, usage, aliases == null ? Lists.newArrayListWithCapacity(0) : Arrays.asList(aliases));

        setPermission(permission);
        setPermissionMessage("§cVocê não possui acesso a este comando.");

        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        SimpleCommandMap simpleCommandMap = craftServer.getCommandMap();

        simpleCommandMap.register("kyzerstudios", this);

    }

    protected abstract void executeArgs(CommandContext context);

    protected void executeEmpty(CommandContext context) {
        context.getSender().sendMessage(usageMessage);
    }

    public final void withArgument(String string, IArgument child) {
        if (this.childCommandMap == null) childCommandMap = Maps.newHashMap();

        childCommandMap.put(string, child);
    }

    @Override
    public final boolean execute(CommandSender commandSender, String s, String[] strings) {

        if (getPermission() != null && !getPermission().isEmpty() && !commandSender.hasPermission(getPermission())) {
            commandSender.sendMessage(getPermissionMessage());
            return false;
        }

        if (strings.length == 0) {
            executeEmpty(CommandContext.of(s, commandSender, strings, this));
            return true;
        }

        IArgument command = childCommandMap == null ? null : childCommandMap.get(strings[0].toLowerCase());

        if (command == null) {
            executeArgs(CommandContext.of(s, commandSender, strings, this));
            return false;
        }

        if (command.getPermission() != null && !command.getPermission().isEmpty() && !commandSender.hasPermission(command.getPermission())) {
            commandSender.sendMessage(getPermissionMessage());
            return false;
        }

        strings = Arrays.copyOfRange(strings, 1, strings.length);

        if (strings.length < command.getRequiredArgs()) {
            commandSender.sendMessage(command.getUsage());
            return false;
        }

        command.execute(CommandContext.of(s, commandSender, strings, this));

        return false;
    }

}
