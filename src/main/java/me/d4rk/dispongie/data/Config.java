package me.d4rk.dispongie.data;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public String BotToken = "BOT_TOKEN";
    public String DiscordChannelId = "DISCORD_CHANNEL_ID";
    public String WebHookURL = "WEBHOOK_URL";
    public String DiscordGameStatus = "Minecraft";
    public String CmdCommand = "!cmd";
    public Boolean ServerStartupMessageEnabled = true;
    public String ServerStartupMessage = "**Server has started**";
    public Boolean ServerShutdownMessageEnabled = true;
    public String ServerShutdownMessage = "**Server has stopped**";
    public Boolean ChannelListCommandEnabled = true;
    public String ListCommand = "playerlist";
    public String ListCommandFormatOnlinePlayersCount = "Online players (%playercount%):";
    public String ListCommandFormatNoOnlinePlayers = "**No online players**";
    public Boolean MinecraftPlayerJoinMessageEnabled =  true;
    public String MinecraftPlayerJoinMessageFormat = "**%displayname% joined the server**";
    public Boolean MinecraftPlayerLeaveMessageEnabled = true;
    public String MinecraftPlayerLeaveMessageFormat = "**%displayname% left the server**";
    public Boolean MinecraftPlayerDeathMessageEnabled = true;
    public String MinecraftPlayerDeathMessageFormat = "**%deathmessage%**";
    public Boolean MinecraftPlayerAchievementMessagesEnabled = true;
    public String MinecraftPlayerAchievementMessagesFormat = "**%displayname% earned achievement %achievement%!**";
    public List<String> DiscordAdminsIds = new ArrayList<>();
    public Boolean DiscordWhitelistSystem = false;
    public String DiscordWhitelistCommand = "!whitelist";
    public String WhitelistRemovedMsg = "You have been removed from the whitelist.";
    public String WhitelistReplaceMsg = "Successfully changed IGN of **%oldnick%** to **%newnick%**!";
    public String WhitelistAddMsg = "Successfully added **%nick%** to whitelist!";
    public String WhitelistCmdError = "Use: !whitelist *nick*";
    public String ChannelTopicUpdaterFormat = "TPS: %tps% | Mem: %usedmemorygb%GB used/%freememorygb%GB free/%maxmemorygb%GB max | %playercount%/%playermax% players online | Server online for %uptimemins% minutes | Last update: %date%";
    public int ChannelTopicUpdaterRateInSeconds = 5;
}
