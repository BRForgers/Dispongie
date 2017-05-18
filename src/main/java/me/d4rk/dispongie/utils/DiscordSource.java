package me.d4rk.dispongie.utils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class DiscordSource implements CommandSource {

    private final CommandSource src;
    private final MessageReceivedEvent event;

    public DiscordSource(CommandSource src, MessageReceivedEvent event) {
        this.src = src;
        this.event = event;
    }


    @Override
    public String getName() {
        return src.getName();
    }

    @Override
    public Locale getLocale() {
        return src.getLocale();
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return src.getCommandSource();
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return src.getContainingCollection();
    }

    @Override
    public SubjectData getSubjectData() {
        return src.getSubjectData();
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return src.getTransientSubjectData();
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        return src.getPermissionValue(contexts,permission);
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        return src.isChildOf(contexts,parent);
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        return src.getParents(contexts);
    }

    @Override
    public Optional<String> getOption(Set<Context> contexts, String key) {
        return src.getOption(contexts,key);
    }

    @Override
    public String getIdentifier() {
        return src.getIdentifier();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return src.getActiveContexts();
    }

    @Override
    public void sendMessage(Text message) {
        src.sendMessage(message);
        event.getChannel().sendMessage((message.toPlain().length() > 2000)?Hastebin.paste(message.toPlain()):message.toPlain()).queue();
    }

    @Override
    public MessageChannel getMessageChannel() {
        return src.getMessageChannel();
    }

    @Override
    public void setMessageChannel(MessageChannel channel) {
        src.setMessageChannel(channel);
    }
}
