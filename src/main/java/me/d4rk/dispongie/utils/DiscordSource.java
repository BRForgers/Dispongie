package me.d4rk.dispongie.utils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ProxySource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class DiscordSource implements ProxySource {

    private final CommandSource src;
    private final MessageReceivedEvent event;

    public DiscordSource(CommandSource src, MessageReceivedEvent event) {
        this.src = src;
        this.event = event;
    }
    

    @Override
    public void sendMessage(Text message) {
        event.getChannel().sendMessage((message.toPlain().length() > 2000)?Hastebin.paste(message.toPlain()):message.toPlain()).queue();
        this.src.sendMessage(message);
    }

    @Override
    public void sendMessage(TextTemplate template) {
        this.sendMessage(checkNotNull(template, "template").apply().build());
        this.src.sendMessage(template);
    }

    @Override
    public void sendMessage(TextTemplate template, Map<String, TextElement> parameters) {
        this.sendMessage(checkNotNull(template, "template").apply(parameters).build());
        this.src.sendMessage(template,parameters);

    }

    @Override
    public void sendMessages(Iterable<Text> messages) {
        for (Text message : checkNotNull(messages, "messages")) {
            this.sendMessage(message);
        }
        this.src.sendMessages(messages);
    }

    @Override
    public void sendMessages(Text... messages) {
        checkNotNull(messages, "messages");

        for (Text message : messages) {
            this.sendMessage(message);
        }
        this.src.sendMessages(messages);
    }

    @Override
    public String getName() {
        return this.src.getName();
    }

    @Override
    public Locale getLocale() {
        return this.src.getLocale();
    }

    @Override
    public MessageChannel getMessageChannel() {
        return this.src.getMessageChannel();
    }

    @Override
    public void setMessageChannel(MessageChannel channel) {
        this.src.setMessageChannel(channel);
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return this.src.getCommandSource();
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return this.src.getContainingCollection();
    }

    @Override
    public SubjectData getSubjectData() {
        return this.src.getSubjectData();
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return this.src.getTransientSubjectData();
    }

    @Override
    public boolean hasPermission(Set<Context> contexts, String permission) {
        return this.src.hasPermission(contexts, permission);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.src.hasPermission(permission);
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        return this.src.getPermissionValue(contexts, permission);
    }

    @Override
    public boolean isChildOf(Subject parent) {
        return this.src.isChildOf(parent);
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        return this.src.isChildOf(contexts, parent);
    }

    @Override
    public List<Subject> getParents() {
        return this.src.getParents();
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        return this.src.getParents(contexts);
    }

    @Override
    public Optional<String> getOption(Set<Context> contexts, String key) {
        return this.src.getOption(contexts, key);
    }

    @Override
    public Optional<String> getOption(String key) {
        return src.getOption(key);
    }

    @Override
    public String getIdentifier() {
        return this.src.getIdentifier();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return this.src.getActiveContexts();
    }

    @Override
    public CommandSource getOriginalSource() {
        return this.src;
    }
}
