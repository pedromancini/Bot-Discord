package me.pedromancini.devbot.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class Config {

    public static File databaseFile = new File( "C:/Users/ypedr/Desktop/BotDiscord/devbot.db");

    public static void createFilesAndTable() throws IOException, SQLException {


        if(Files.notExists(databaseFile.toPath())){
            Files.createFile(databaseFile.toPath());
            CRUD.createTable();

        }





    }

}
