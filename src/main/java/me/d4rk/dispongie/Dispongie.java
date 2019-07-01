package me.d4rk.dispongie;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import me.d4rk.dispongie.commands.Lenny;
import me.d4rk.dispongie.commands.Shrug;
import me.d4rk.dispongie.commands.discordConfig;
import me.d4rk.dispongie.data.Config;
import me.d4rk.dispongie.data.WhitelistLink;
import me.d4rk.dispongie.listeners.*;
import me.d4rk.dispongie.threads.ChannelTopicUpdater;
import me.d4rk.dispongie.utils.IOHelper;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Plugin(id = "dispongie", name = "Dispongie", version = "1.0")
public class Dispongie {

    private static Dispongie instance;
    public static Dispongie getInstance() {
        return instance;
    }

    public static final Gson JSON = new GsonBuilder().setPrettyPrinting().create();
    public static Config config = new Config();
    public static WhitelistLink whitelistLink = new WhitelistLink();

    public static ChannelTopicUpdater channelTopicUpdater;

    private final Logger logger;
    private final Game game;

    public static final long startTime = System.nanoTime();

    public static JDA jda;
    String channel_id = "ID DO CANAL";
    String bot_token = "TOKEN DO BOT";
    String webhook = "LINK DO WEBHOOK";
    public static Channel channel = null;

    CommandSpec ShrugSpec = CommandSpec.builder()
            .description(Text.of("Shrug"))
            .executor(new Shrug())
            .build();

    CommandSpec LennySpec = CommandSpec.builder()
            .description(Text.of("Lenny"))
            .executor(new Lenny())
            .build();

    CommandSpec discordConfigSpec = CommandSpec.builder()
            .description(Text.of("Fix config."))
            .permission("admin")
            .executor(new discordConfig())
            .build();

    @Inject @ConfigDir(sharedRoot = false)
    File privateConfigDir;

    @Inject
    public Dispongie(Logger logger, Game game) {
        instance = this;

        this.logger = logger;
        this.game = game;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent e){
        logger.debug(privateConfigDir.toString());
        getLogger().debug(privateConfigDir.toString());
    }

    @Listener
    public void onInit(GameInitializationEvent event){
        Manager.loadConfig();
        this.bot_token = config.BotToken;
        this.channel_id = config.DiscordChannelId;
        try {
            JSONObject jo = (JSONObject) new JSONTokener(IOHelper.toString(config.WebHookURL)).nextValue();
            if(jo.getString("channel_id").equals(channel_id)){
                webhook = config.WebHookURL;
                getLogger().info("Webhook enabled!");
            }else {
                webhook = null;
                getLogger().info("Webhook disabled! Webhook channel incorrect!");
            }
        }catch (Exception e){
            webhook = null;
            getLogger().info("Webhook disabled! Exception found.");
        }
        if(config.DiscordWhitelistSystem){
            Manager.loadWhitelist();
        }
    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event) {
        logger.info("Armelin is topper!");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        //game.getCommandManager().process(game.getServer().getConsole(), "command string");
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(bot_token)
                    .setGame((!config.DiscordGameStatus.isEmpty())?net.dv8tion.jda.core.entities.Game.of(net.dv8tion.jda.core.entities.Game.GameType.DEFAULT, config.DiscordGameStatus):null)
                    .addEventListener(new DiscordListener(channel_id))
                    .buildBlocking();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        channel = jda.getTextChannelById(channel_id);
        Sponge.getCommandManager().register(this, ShrugSpec, "shrug");
        Sponge.getCommandManager().register(this, discordConfigSpec, "discordConfig");
        Sponge.getCommandManager().register(this, LennySpec,"lenny");
        Sponge.getEventManager().registerListeners(this, new ChatListener(webhook, jda, channel_id));
        Sponge.getEventManager().registerListeners(this, new DeathListener(jda, channel_id));
        Sponge.getEventManager().registerListeners(this, new ConnectionListener(jda, channel_id));
        Sponge.getEventManager().registerListeners(this, new AdvancementListener(jda, channel_id));

        if (channelTopicUpdater == null) {
            channelTopicUpdater = new ChannelTopicUpdater();
            channelTopicUpdater.start();
        }

        if(config.ServerStartupMessageEnabled)
            jda.getTextChannelById(channel_id).sendMessage(config.ServerStartupMessage).queue();
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {
        Manager.saveWhitelist();

        if (channelTopicUpdater != null) {
            channelTopicUpdater.interrupt();
            channelTopicUpdater = null;
        }

        if(config.ServerShutdownMessageEnabled)
            jda.getTextChannelById(channel_id).sendMessage(config.ServerShutdownMessage).queue();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        jda.shutdown();

        Sponge.getEventManager().unregisterPluginListeners(this);
    }

    public File getConfigDir() {
        return privateConfigDir;
    }

    public Logger getLogger() {
        return logger;
    }

    public Game getGame() {
        return game;
    }

    public static String convertMentionsFromNames(String message) {
        if (!message.contains("@")) return message;
        List<String> splitMessage = new ArrayList<>(Arrays.asList(message.split("@| ")));
        for (Member member : channel.getMembers())
            for (String segment : splitMessage)
                if (member.getUser().getName().toLowerCase().equals(segment.toLowerCase()) || (member.getNickname() != null && member.getNickname().toLowerCase().equals(segment.toLowerCase()))) {
                    splitMessage.set(splitMessage.indexOf(segment), member.getAsMention());
                }
        splitMessage.removeAll(Arrays.asList("", null));
        return String.join(" ", splitMessage);
    }
}
