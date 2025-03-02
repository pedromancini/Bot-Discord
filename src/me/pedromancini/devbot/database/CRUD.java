package me.pedromancini.devbot.database;

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


}
