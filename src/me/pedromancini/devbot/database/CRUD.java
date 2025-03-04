package me.pedromancini.devbot.database;

import me.pedromancini.devbot.main.DevBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class CRUD {

    public static void createTable() throws SQLException {

        String sql = "create table tb_guild\n" +
                "(\n" +
                "    id integer not null primary key autoincrement unique,\n" +
                "    guild_id text not null unique,\n" +
                "    prefix text not null\n" +
                ")";


        Statement statement = Objects.requireNonNull(ConnectionFactory.getConn()).createStatement();
        statement.execute(sql);
        statement.close();
        ConnectionFactory.getConn().close();
    }

    public static void insert(String guildId, char prefix) throws SQLException {

        String sql = """
                        insert or ignore into tb_guild values (null, '%s', '%s')
                    """.formatted(guildId, prefix);


        Statement statement = Objects.requireNonNull(ConnectionFactory.getConn()).createStatement();
        statement.execute(sql);
        statement.close();
        ConnectionFactory.getConn().close();
    }



    public static void select(String guildId) throws SQLException {

        String sql = """
                        select * from tb_guild where guild_id = '%s'
                     """.formatted(guildId);
        Statement statement = Objects.requireNonNull(ConnectionFactory.getConn()).createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            DevBot.prefixMap.put(guildId, resultSet.getString("prefix").charAt(0));
        }

        //statement.execute(sql);
        statement.close();
        ConnectionFactory.getConn().close();
    }

    public static void update(MessageReceivedEvent event, String guildId, char newPrefix) throws SQLException {
        String sql = """
                        update tb_guild set prefix = '%s' where guild_id = '%s'
                    """.formatted(newPrefix, guildId);
        Statement statement = Objects.requireNonNull(ConnectionFactory.getConn()).createStatement();

        statement.execute(sql);
        statement.close();
        ConnectionFactory.getConn().close();
    }

}
