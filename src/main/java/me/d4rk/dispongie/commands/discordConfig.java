package me.d4rk.dispongie.commands;

import me.d4rk.dispongie.Manager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class discordConfig implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player) {
            Player player = (Player) src;
            if(player.hasPermission("admin")){
                Manager.saveConfig();
                player.sendMessage(Text.of("Config fixed!"));
            }else {
                player.sendMessage(Text.of("No permission!"));
            }
        }
        else if(src instanceof ConsoleSource) {
            Manager.saveConfig();
            src.sendMessage(Text.of("Config fixed!"));
        }
        return CommandResult.success();
    }
}
