package net.coalcube.moneysystem.listener;

import net.coalcube.moneysystem.util.ConfigurationManager;
import net.coalcube.moneysystem.util.MoneyManager;
import net.coalcube.moneysystem.util.UpdateChecker;
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
    private ConfigurationManager configurationManager;

    public JoinEvent(MoneyManager moneyManager, FileConfiguration config, ConfigurationManager configurationManager) {
        this.moneyManager = moneyManager;
        this.config = config;
        this.configurationManager = configurationManager;
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

        if (player.hasPermission("moneysys.set")) {
            try {
                if (new UpdateChecker(101672).checkForUpdates()) {

                    player.sendMessage(configurationManager.getMessage("prefix") + "§cEin neues Update ist verfügbar.");
                    player.sendMessage(configurationManager.getMessage("prefix")
                            + "§7Lade es dir unter §ehttps://www.spigotmc.org/resources/bansystem-mit-ids.65863/ " +
                            "§7runter um aktuell zu bleiben.");

                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

}
