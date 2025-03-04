package me.pedromancini.devbot.commands;

import me.pedromancini.devbot.database.ConnectionFactory;
import me.pedromancini.devbot.main.DevBot;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class PresetCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Recupera o prefixo do servidor
        String prefix = String.valueOf(DevBot.prefixMap.get(event.getGuild().getId()));

        // Divide a mensagem em argumentos
        String[] args = event.getMessage().getContentRaw().split(" ");

        // Verifica se o comando é '!comandos' ou o equivalente com o prefixo correto
        if (args[0].equalsIgnoreCase(prefix + "comandos")) {

            // Cria a lista de comandos com o prefixo
            String sql = """
                             📜 **Lista de Comandos Disponíveis:**
                              🔹 '%s'ping - Verifica a latência do bot.
                              🔹 '%s'prefix - Exibe o prefix do bot.
                              🔹 '%s'setprefix - Seta um novo prefix para o bot.
                              🔹 '%s'gerarimg - ------.
                     """.formatted(prefix, prefix, prefix, prefix, prefix);
            try {
                Statement statement = Objects.requireNonNull(ConnectionFactory.getConn()).createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            // Envia a mensagem com a lista de comandos para o canal de texto
            TextChannel textChannel = (TextChannel) event.getChannel();
            textChannel.sendMessage(sql).queue();
        }
    }
}
