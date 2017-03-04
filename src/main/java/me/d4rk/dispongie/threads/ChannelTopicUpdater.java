package me.d4rk.dispongie.threads;

import me.d4rk.dispongie.Dispongie;
import me.d4rk.dispongie.utils.MemUtil;
import net.dv8tion.jda.core.Permission;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChannelTopicUpdater extends Thread {

    public void run() {
        int rate = Dispongie.config.ChannelTopicUpdaterRateInSeconds * 1000;

        while (!isInterrupted())
        {
            try {
                String chatTopic = applyFormatters(Dispongie.config.ChannelTopicUpdaterFormat);

                if ((Dispongie.channel == null) || (chatTopic.isEmpty())) interrupt();
                if (Dispongie.jda == null || Dispongie.jda.getSelfUser() == null) continue;

                if (!chatTopic.isEmpty() && Dispongie.channel != null && !Dispongie.channel.getGuild().getMember(Dispongie.jda.getSelfUser()).hasPermission(Dispongie.channel, Permission.MANAGE_CHANNEL))
                    Dispongie.getInstance().getLogger().warn("Unable to update chat channel; no permission to manage channel");

                if (!chatTopic.isEmpty() && Dispongie.channel != null && Dispongie.channel.getGuild().getMember(Dispongie.jda.getSelfUser()).hasPermission(Dispongie.channel, Permission.MANAGE_CHANNEL))
                    Dispongie.channel.getManager().setTopic(chatTopic).queue();

            } catch (NullPointerException ignored) {}

            try { Thread.sleep(rate); } catch (InterruptedException ignored) {}
        }
    }

    private String applyFormatters(String input) {
        Map<String, String> mem = MemUtil.get();

        input = input
                .replace("%playercount%", Integer.toString(Dispongie.getInstance().getGame().getServer().getOnlinePlayers().size()))
                .replace("%playermax%", Integer.toString(Dispongie.getInstance().getGame().getServer().getMaxPlayers()))
                .replace("%date%", new Date().toString())
                .replace("%uptimemins%", Long.toString(TimeUnit.NANOSECONDS.toMinutes(System.nanoTime() - Dispongie.startTime)))
                .replace("%uptimehours%", Long.toString(TimeUnit.NANOSECONDS.toHours(System.nanoTime() - Dispongie.startTime)))
                .replace("%freememory%", mem.get("freeMB"))
                .replace("%usedmemory%", mem.get("usedMB"))
                .replace("%totalmemory%", mem.get("totalMB"))
                .replace("%maxmemory%", mem.get("maxMB"))
                .replace("%freememorygb%", mem.get("freeGB"))
                .replace("%usedmemorygb%", mem.get("usedGB"))
                .replace("%totalmemorygb%", mem.get("totalGB"))
                .replace("%maxmemorygb%", mem.get("maxGB"))
                .replace("%tps%", Dispongie.getInstance().getGame().getServer().getTicksPerSecond() + "")
        ;

        return input;
    }
}
