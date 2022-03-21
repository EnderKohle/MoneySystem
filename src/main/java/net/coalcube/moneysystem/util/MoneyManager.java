package net.coalcube.moneysystem.util;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MoneyManager {

    private MySQL mySQL;


    public MoneyManager(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    public void addMoney(UUID player, double amount) throws SQLException, ExecutionException, InterruptedException {
        setMoney(player, getMoney(player) + amount);
    }

    public void removeMoney(UUID player, double amount) throws SQLException, ExecutionException, InterruptedException {
        setMoney(player, getMoney(player) - amount);
    }

    public void setMoney(UUID player, double amount) throws SQLException {
        mySQL.update("UPDATE PlayerMoney SET money=" + amount + " WHERE player='" + player + "'");
    }

    public void registerPlayer(UUID player, double money) throws SQLException {
        mySQL.update("INSERT INTO `PlayerMoney` (`player`, `money`) VALUES ('" + player + "', '" + money + "')");
    }

    public void deletePlayer(UUID player) throws SQLException {
        mySQL.update("DELETE FROM PlayerMoney WHERE player='" + player + "'");
    }

    public Double getMoney(UUID player) throws SQLException, ExecutionException, InterruptedException {
        ResultSet rs = mySQL.getResult("SELECT money FROM PlayerMoney WHERE player='" + player + "'");

        while (rs.next()) {
            return rs.getDouble("money");
        }
        return -1D;
    }

    public HashMap<UUID, Double> getTopTen() throws SQLException, ExecutionException, InterruptedException {
        HashMap<UUID, Double> map = new HashMap();

        ResultSet rs = mySQL.getResult("(SELECT * FROM PlayerMoney ORDER BY money DESC LIMIT 10) ORDER BY money DESC");

        while (rs.next()) {
            map.put(UUID.fromString(rs.getString("player")), rs.getDouble("money"));
        }

        return map;
    }

    public boolean existsPlayer(UUID player) throws SQLException, ExecutionException, InterruptedException {
        ResultSet rs = mySQL.getResult("SELECT * FROM PlayerMoney WHERE player='" + player + "'");

        while (rs.next()) {
            return true;
        }
        return false;
    }
}
