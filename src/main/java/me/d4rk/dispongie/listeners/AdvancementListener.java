package me.d4rk.dispongie.listeners;

import me.d4rk.dispongie.Dispongie;
import net.dv8tion.jda.core.JDA;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.advancement.AdvancementEvent;

public class AdvancementListener {
    JDA jda;
    String channel_id;
    public AdvancementListener(JDA gee, String chid) {
        jda = gee;
        channel_id = chid;
    }
    @Listener
    public void onPlayerGrantAchievement(AdvancementEvent.Grant event) {
        if(Dispongie.config.MinecraftPlayerAchievementMessagesEnabled)
            if(!event.getAdvancement().getName().startsWith("recipes"))
                jda.getTextChannelById(channel_id).sendMessage(Dispongie.config.MinecraftPlayerAchievementMessagesFormat.replace("%displayname%",event.getCause().first(Player.class).get().getName()).replace("%achievement%",event.getAdvancement().getName())).queue();
    }
}
