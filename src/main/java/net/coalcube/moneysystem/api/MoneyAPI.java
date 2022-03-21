package net.coalcube.moneysystem.api;

import net.coalcube.moneysystem.MoneySystem;
import net.coalcube.moneysystem.util.MoneyManager;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MoneyAPI {

    private MoneyManager moneyManager = new MoneySystem().getMoneyManager();
    private Player player;
    private UUID uuid;

    public MoneyAPI(Player player) {
        this.uuid = player.getUniqueId();
    }

    public MoneyAPI(UUID uuid) {
        this.uuid = uuid;
    }

    public void addMoney(double amount) throws SQLException, ExecutionException, InterruptedException {
        moneyManager.addMoney(uuid, amount);
    }

    public void removeMoney(double amount) throws SQLException, ExecutionException, InterruptedException {
        moneyManager.removeMoney(uuid, amount);
    }

    public void setMoney(double amount) throws SQLException {
        moneyManager.setMoney(uuid, amount);
    }

    public void deletePlayer() throws SQLException {
        moneyManager.deletePlayer(uuid);
    }

    public void registerPlayer(double startCapital) throws SQLException {
        moneyManager.registerPlayer(uuid, startCapital);
    }

    public Double getMoney() throws SQLException, ExecutionException, InterruptedException {
        return moneyManager.getMoney(uuid);
    }

    public boolean existsPlayer() throws SQLException, ExecutionException, InterruptedException {
        return moneyManager.existsPlayer(uuid);
    }

}
