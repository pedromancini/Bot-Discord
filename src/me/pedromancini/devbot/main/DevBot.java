package me.pedromancini.devbot.main;

import me.pedromancini.devbot.commands.Ping;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.Guild;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DevBot {

    private static final Logger LOGGER = Logger.getLogger(DevBot.class.getName());
    public static JDA jda;
    public static Map<Long, String> mapGuildName = new HashMap<>();

    public static void main(String[] args) {
        String token = Config.getToken();

        if (token == null || token.isEmpty()) {
            LOGGER.severe("Token não encontrado! Verifique o arquivo config.properties.");
            return;
        }

        try {
            // Inicializa o bot com o token carregado
            jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class)).build();
            jda.addEventListener(new Ping());

            // Mapeia os nomes das guildas
            for (Guild guild : jda.getGuilds()) {
                mapGuildName.put(guild.getIdLong(), guild.getName());
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao inicializar o bot", e);
        }
    }
}
