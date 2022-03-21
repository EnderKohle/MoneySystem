package net.coalcube.moneysystem.command;

import net.coalcube.moneysystem.util.ConfigurationManager;
import net.coalcube.moneysystem.util.MoneyManager;
import net.coalcube.moneysystem.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CMDpay implements CommandExecutor {

    private static MoneyManager moneyManager;
    private static ConfigurationManager configurationManager;

    public CMDpay(MoneyManager moneyManager, ConfigurationManager configurationManager) {
        this.moneyManager = moneyManager;
        this.configurationManager = configurationManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(configurationManager.getMessage("NoPlayerMessage"));
            return false;
        }

        Player player = (Player) sender;

        if(args.length != 2) {
          player.sendMessage(configurationManager.getMessage("pay.usage"));
          return false;
        }
        double amount = 0;
        try {
            amount = Double.valueOf(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(configurationManager.getMessage("UnsupportedNumberFormat"));
            return false;
        }

        if(UUIDFetcher.getUUID(args[0]) != null) {
            UUID uuid = UUIDFetcher.getUUID(args[0]);
            try {
                if(moneyManager.getMoney(player.getUniqueId()) >= amount) {
                    moneyManager.addMoney(uuid, amount);
                    moneyManager.removeMoney(player.getUniqueId(), amount);
                } else {
                    player.sendMessage(configurationManager.getMessage("pay.notEnoughMoney"));
                }

            } catch (ExecutionException | InterruptedException | SQLException e) {
                player.sendMessage(configurationManager.getMessage("pay.faild"));
                e.printStackTrace();
            }

            player.sendMessage(configurationManager.getMessage("pay.success")
                    .replaceAll("%amount%", String.valueOf(amount))
                    .replaceAll("%player%", UUIDFetcher.getName(uuid)));

            if(Bukkit.getPlayer(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);

                target.sendMessage(configurationManager.getMessage("pay.receive")
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%player%", player.getDisplayName()));
            }
            return true;
        } else if(args[0].equalsIgnoreCase("*")) {
            try {
                if(moneyManager.getMoney(player.getUniqueId()) >= Bukkit.getOnlinePlayers().size() * amount) {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        moneyManager.addMoney(all.getUniqueId(), amount);
                        all.sendMessage(configurationManager.getMessage("pay.receive")
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%player%", player.getDisplayName()));
                    }
                    moneyManager.removeMoney(player.getUniqueId(), Bukkit.getOnlinePlayers().size()*amount);
                    player.sendMessage(configurationManager.getMessage("pay.all")
                            .replaceAll("%amount%", String.valueOf(amount)));
                    return true;
                } else {
                    player.sendMessage(configurationManager.getMessage("NotEnoughMoney"));
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage(configurationManager.getMessage("PlayerNotFound"));
        }

        return false;
    }
}
