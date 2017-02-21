package me.d4rk.dispongie.listeners;

import me.d4rk.dispongie.Dispongie;
import net.dv8tion.jda.core.JDA;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;

public class DeathListener {

    JDA jda;
    String channel_id;

    public DeathListener(JDA gee, String chid) {
        jda = gee;
        channel_id = chid;
    }

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event) {
        if (event.getTargetEntity() instanceof Player) {
            if(Dispongie.config.MinecraftPlayerDeathMessageEnabled)
                jda.getTextChannelById(channel_id).sendMessage(Dispongie.config.MinecraftPlayerDeathMessageFormat.replace("%deathmessage%",event.getMessage().toPlain())).queue();
        }
    }

}
