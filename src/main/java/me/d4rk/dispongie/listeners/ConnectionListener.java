package me.d4rk.dispongie.listeners;

import me.d4rk.dispongie.Dispongie;
import net.dv8tion.jda.core.JDA;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;


public class ConnectionListener {

    JDA jda;
    String channel_id;

    public ConnectionListener(JDA gee, String chid) {
        jda = gee;
        channel_id = chid;
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        if(Dispongie.config.MinecraftPlayerJoinMessageEnabled)
            jda.getTextChannelById(channel_id).sendMessage(Dispongie.config.MinecraftPlayerJoinMessageFormat.replace("%displayname%",event.getCause().first(Player.class).get().getName())).queue();
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        if(Dispongie.config.MinecraftPlayerLeaveMessageEnabled)
            jda.getTextChannelById(channel_id).sendMessage(Dispongie.config.MinecraftPlayerLeaveMessageFormat.replace("%displayname%",event.getCause().first(Player.class).get().getName())).queue();
    }

}
