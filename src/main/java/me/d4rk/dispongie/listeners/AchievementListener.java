package me.d4rk.dispongie.listeners;

import me.d4rk.dispongie.Dispongie;
import net.dv8tion.jda.core.JDA;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
//import org.spongepowered.api.event.achievement.GrantAchievementEvent;

public class AchievementListener {
    /*JDA jda;
    String channel_id;

    public AchievementListener(JDA gee, String chid) {
        jda = gee;
        channel_id = chid;
    }

    @Listener
    public void onPlayerGrantAchievement(GrantAchievementEvent event) {
        if(Dispongie.config.MinecraftPlayerAchievementMessagesEnabled)
            if(!event.getCause().first(Player.class).get().getAchievementData().achievements().contains(event.getAchievement()))
                jda.getTextChannelById(channel_id).sendMessage(Dispongie.config.MinecraftPlayerAchievementMessagesFormat.replace("%displayname%",event.getCause().first(Player.class).get().getName()).replace("%achievement%",event.getAchievement().getName())).queue();
    }
    */
}
