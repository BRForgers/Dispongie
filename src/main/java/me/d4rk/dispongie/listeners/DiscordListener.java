package me.d4rk.dispongie.listeners;

import me.d4rk.dispongie.Dispongie;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class DiscordListener extends ListenerAdapter {

    String channel_id;

    public DiscordListener(String chid) {
        channel_id = chid;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor() != event.getJDA().getSelfUser() && !event.getAuthor().isFake() && event.getTextChannel().getId().equals(channel_id) ) {
            if(event.getMessage().getRawContent().startsWith(Dispongie.config.ListCommand) && Dispongie.config.ChannelListCommandEnabled){
                if (Sponge.getServer().getOnlinePlayers().size() == 0) {
                    event.getChannel().sendMessage(Dispongie.config.ListCommandFormatNoOnlinePlayers).queue();
                }else {
                    List<String> onlinePlayers = new ArrayList<>();
                    Sponge.getServer().getOnlinePlayers().forEach(player -> onlinePlayers.add(player.getName()));

                    String playerlistMessage = "";
                    playerlistMessage += "```\n";
                    playerlistMessage += Dispongie.config.ListCommandFormatOnlinePlayersCount.replace("%playercount%", Sponge.getServer().getOnlinePlayers().size() + "/" + Sponge.getServer().getMaxPlayers());
                    playerlistMessage += "\n";
                    playerlistMessage += String.join(", ", onlinePlayers);

                    if (playerlistMessage.length() > 1996)
                        playerlistMessage = playerlistMessage.substring(0, 1993) + "...";
                    playerlistMessage += "\n```";
                    event.getChannel().sendMessage(playerlistMessage).queue();
                }
            }
            else if(Dispongie.config.Admins.contains(event.getAuthor().getId()) && !Dispongie.config.CmdCommand.isEmpty() && event.getMessage().getRawContent().startsWith(Dispongie.config.CmdCommand)){
                CommandResult singlecmd = Dispongie.getInstance().getGame().getCommandManager().process(Dispongie.getInstance().getGame().getServer().getConsole(), event.getMessage().getRawContent().replace(Dispongie.config.CmdCommand,"").trim());
            }
            else {
                Text discord = Text.builder("[Discord]").color(TextColors.BLUE).append(
                        Text.builder(" <" + event.getAuthor().getName() + "> " + event.getMessage().getRawContent()+((event.getMessage().getAttachments().size() > 0)?" <att>":"")).color(TextColors.WHITE).build()).build();
                Sponge.getServer().getBroadcastChannel().send(discord.trim());
            }
        }
    }

}
