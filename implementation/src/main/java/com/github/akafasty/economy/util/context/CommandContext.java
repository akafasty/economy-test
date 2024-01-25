package com.github.akafasty.economy.util.context;

import com.github.akafasty.economy.util.CommandBase;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor(staticName = "of")
@Getter
public class CommandContext {

    private final String label;
    private final CommandSender sender;
    private final String[] arguments;
    private final CommandBase commandBase;

    public String getAsString(int argument) {
        return arguments[argument];
    }

    public Double getAsDouble(int argument) {
        return Doubles.tryParse(arguments[argument]);
    }

    public Integer getAsInt(int argument) {
        return Ints.tryParse(arguments[argument]);
    }

    public Player getAsPlayer(int argument) {
        return Bukkit.getPlayer(arguments[argument]);
    }

    public Player getSenderAsPlayer() {
        return (Player) sender;
    }

}
