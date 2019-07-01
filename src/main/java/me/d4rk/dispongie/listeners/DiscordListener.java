package me.d4rk.dispongie.listeners;

import me.d4rk.dispongie.Dispongie;
import me.d4rk.dispongie.Manager;
import me.d4rk.dispongie.utils.DiscordSource;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.spongepowered.api.Sponge;
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
            if(event.getMessage().getContentRaw().startsWith(Dispongie.config.ListCommand) && Dispongie.config.ChannelListCommandEnabled){
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
            else if(Dispongie.config.DiscordAdminsIds.contains(event.getAuthor().getId()) && !Dispongie.config.CmdCommand.isEmpty() && event.getMessage().getContentRaw().startsWith(Dispongie.config.CmdCommand)){
                Dispongie.getInstance().getGame().getCommandManager().process(new DiscordSource(Dispongie.getInstance().getGame().getServer().getConsole(),event), event.getMessage().getContentRaw().replace(Dispongie.config.CmdCommand, "").trim());
            }
            else if(Dispongie.config.DiscordWhitelistSystem && !Dispongie.config.DiscordWhitelistCommand.isEmpty() && event.getMessage().getContentRaw().startsWith(Dispongie.config.DiscordWhitelistCommand)){
                String playernick = event.getMessage().getContentRaw().replace(Dispongie.config.DiscordWhitelistCommand,"").trim();
                if(!playernick.isEmpty()){
                    if(Dispongie.whitelistLink.Link.containsKey(event.getAuthor().getId())){
                        String old = Dispongie.whitelistLink.Link.get(event.getAuthor().getId());
                        Dispongie.getInstance().getGame().getCommandManager().process(Dispongie.getInstance().getGame().getServer().getConsole(), "kick " + old + " " + Dispongie.config.WhitelistRemovedMsg);
                        Dispongie.getInstance().getGame().getCommandManager().process(Dispongie.getInstance().getGame().getServer().getConsole(), "whitelist remove " + old);
                        Dispongie.getInstance().getGame().getCommandManager().process(Dispongie.getInstance().getGame().getServer().getConsole(), "whitelist add " + playernick);
                        Dispongie.whitelistLink.Link.put(event.getAuthor().getId(),playernick);
                        event.getChannel().sendMessage(Dispongie.config.WhitelistReplaceMsg.replace("%oldnick%", old).replace("%newnick%", playernick)).queue();
                    }else{
                        Dispongie.whitelistLink.Link.put(event.getAuthor().getId(), playernick);
                        Dispongie.getInstance().getGame().getCommandManager().process(Dispongie.getInstance().getGame().getServer().getConsole(), "whitelist add " + playernick);
                        event.getChannel().sendMessage(Dispongie.config.WhitelistAddMsg.replace("%nick%", playernick)).queue();
                    }
                    Manager.saveWhitelist();
                }
                else {
                    event.getChannel().sendMessage(Dispongie.config.WhitelistCmdError).queue();
                }
            }
            else {
                Text discord = Text.builder("[Discord]").color(TextColors.BLUE).append(
                        Text.builder(" <" + event.getAuthor().getName() + "> " + event.getMessage().getContentDisplay()+((event.getMessage().getAttachments().size() > 0)?" <att>":"")+((event.getMessage().getEmbeds().size() > 0)?" <embed>":"")).color(TextColors.WHITE).build()).build();
                Sponge.getServer().getBroadcastChannel().send(discord.trim());
            }
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        if (Dispongie.config.DiscordWhitelistSystem) {
            if (Dispongie.whitelistLink.Link.containsKey(event.getMember().getUser().getId())) {
                Dispongie.getInstance().getGame().getCommandManager().process(Dispongie.getInstance().getGame().getServer().getConsole(), "whitelist remove " + Dispongie.whitelistLink.Link.get(event.getMember().getUser().getId()));
                Dispongie.whitelistLink.Link.remove(event.getMember().getUser().getId());
                Manager.saveWhitelist();
            }
        }
    }

    @SuppressWarnings("unused")
    public void debug(String msg){
        Text discord = Text.builder("[DEBUG]").color(TextColors.RED).append(
                Text.builder(msg).color(TextColors.WHITE).build()).build();
        Sponge.getServer().getBroadcastChannel().send(discord.trim());
    }
}
