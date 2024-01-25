package com.github.akafasty.economy.util.child;

import com.github.akafasty.economy.util.context.CommandContext;

public interface IArgument {

    String getPermission();

    String getUsage();

    int getRequiredArgs();

    void execute(CommandContext context);

}
