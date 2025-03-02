package me.pedromancini.devbot.commands;

import me.pedromancini.devbot.database.CRUD;
import me.pedromancini.devbot.main.DevBot;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;


public class Prefix extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();

        if(args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "prefix")){
            textChannel.sendMessage("O prefixo para este servidor é: " + DevBot.prefixMap.get(event.getGuild().getId())).queue();
        }

        if(args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "setprefix")){
            DevBot.prefixMap.put(event.getGuild().getId(), args[1].charAt(0));

            try {
                CRUD.update(event, event.getGuild().getId(), args[1].charAt(0));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            textChannel.sendMessage("O prefixo para este servidor foi alterado para: " + args[1]).queue();

        }
    }
}
