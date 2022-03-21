package net.coalcube.moneysystem.util;

import java.sql.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MySQL {

    private String host, username, password, database;
    private int port;
    private Connection connection;


    public MySQL(String host, int port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
    }

    public ResultSet getResult(String qry) throws SQLException, ExecutionException, InterruptedException {
        if(!isConnected()) {
            connect();
        }
        final FutureTask<ResultSet> task = new FutureTask<>(() -> {
            PreparedStatement stmt = connection.prepareStatement(qry);
            return stmt.executeQuery();
        });
        task.run();

        return task.get();
    }

    public void update(String qry) throws SQLException {
        if(!isConnected()) {
            connect();
        }
        new FutureTask<>(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(qry);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }, 1).run();
    }

    public void disconnect() throws SQLException {
        connection.close();
        connection = null;
    }

    public void createTable() throws SQLException {
        update("CREATE TABLE IF NOT EXISTS `" + database + "`.`PlayerMoney` ( `player` VARCHAR(100) NOT NULL , `money` DOUBLE NOT NULL ) ENGINE = InnoDB;");
    }

    public Boolean isConnected() {
        return (connection != null);
    }

}
