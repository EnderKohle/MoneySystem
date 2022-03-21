package net.coalcube.moneysystem.listener;

import net.coalcube.moneysystem.util.MoneyManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class JoinEvent implements Listener {

    private MoneyManager moneyManager;
    private FileConfiguration config;

    public JoinEvent(MoneyManager moneyManager, FileConfiguration config) {
        this.moneyManager = moneyManager;
        this.config = config;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        try {
            if(!moneyManager.existsPlayer(player.getUniqueId())) {
                try {
                    moneyManager.registerPlayer(player.getUniqueId(), config.getInt("startCapital"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
