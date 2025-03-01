package me.pedromancini.devbot.commands;

import me.pedromancini.devbot.main.DevBot;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Ping extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();

        if(args[0].equalsIgnoreCase("!" + "ping")){
            textChannel.sendMessage(DevBot.jda.getGatewayPing() + "ms").queue();

        }

    }
}
