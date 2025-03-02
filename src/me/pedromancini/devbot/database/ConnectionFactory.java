package me.pedromancini.devbot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConn() {
        String url = "jdbc:sqlite:C:/Users/ypedr/Desktop/BotDiscord/devbot.db";

        try {
            return DriverManager.getConnection(url);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;



    }
}
