package me.pedromancini.devbot.main;

public class Config {
    public static String getToken() {
        return System.getenv("DISCORD_TOKEN"); // Busca a variável de ambiente
    }
}
