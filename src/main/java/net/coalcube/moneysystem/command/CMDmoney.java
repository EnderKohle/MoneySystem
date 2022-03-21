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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CMDmoney implements CommandExecutor {

    private static MoneyManager moneyManager;
    private static ConfigurationManager configurationManager;

    public CMDmoney(MoneyManager moneyManager, ConfigurationManager configurationManager) {
        this.moneyManager = moneyManager;
        this.configurationManager = configurationManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {


        if(args.length == 0) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(configurationManager.getMessage("NoPlayerMessage"));
                return false;
            }

            Player p = (Player) sender;

            try {
                if(!moneyManager.existsPlayer(p.getUniqueId())) {
                    moneyManager.registerPlayer(p.getUniqueId(), configurationManager.getRawConfig().getDouble("startCapital"));
                }
                sender.sendMessage(configurationManager.getMessage("money.showBankBalance")
                        .replaceAll("%amount%", moneyManager.getMoney(p.getUniqueId()).toString()));
            } catch (SQLException | InterruptedException | ExecutionException e) {
                sender.sendMessage(configurationManager.getMessage("money.showBankBalanceFail"));
                e.printStackTrace();
            }
        } else if(args.length > 0) {
            if(args[0].equalsIgnoreCase("set")) {
                if(!sender.hasPermission("moneysys.set")) {
                    sender.sendMessage(configurationManager.getMessage("NoPermissionMessage"));
                    return false;
                }
                if(args.length == 3) {
                    if(UUIDFetcher.getUUID(args[1]) != null) {

                        UUID uuid = UUIDFetcher.getUUID(args[1]);

                        int amount = 0;
                        try {
                            amount = Integer.valueOf(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(configurationManager.getMessage("UnsupportedNumberFormat"));
                            return false;
                        }
                        try {
                            moneyManager.setMoney(uuid, amount);
                            if(Bukkit.getPlayer(args[1]) != null) {
                                Player target = Bukkit.getPlayer(args[1]);

                                sender.sendMessage(configurationManager.getMessage("money.set.success")
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%player%", target.getDisplayName()));

                                target.sendMessage(configurationManager.getMessage("money.set.receive")
                                        .replaceAll("%player%", sender.getName())
                                        .replaceAll("%amount%", String.valueOf(amount)));
                            } else
                                sender.sendMessage(configurationManager.getMessage("money.set.success")
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%player%", args[1]));
                        } catch (SQLException e) {
                            sender.sendMessage(configurationManager.getMessage("money.set.faild"));
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(configurationManager.getMessage("PlayerNotFound"));
                        return false;
                    }
                } else {
                    sender.sendMessage(configurationManager.getMessage("money.set.usage"));
                    return false;
                }
            } else if(args[0].equalsIgnoreCase("add")) {
                if(!sender.hasPermission("moneysys.add")) {
                    sender.sendMessage(configurationManager.getMessage("NoPermissionMessage"));
                    return false;
                }
                if(args.length == 3) {
                    if(UUIDFetcher.getUUID(args[1]) != null) {

                        UUID uuid = UUIDFetcher.getUUID(args[1]);

                        int amount = 0;
                        try {
                            amount = Integer.valueOf(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(configurationManager.getMessage("UnsupportedNumberFormat"));
                            return false;
                        }
                        try {
                            moneyManager.addMoney(uuid, amount);
                            if(Bukkit.getPlayer(args[1]) != null) {
                                Player target = Bukkit.getPlayer(args[1]);

                                sender.sendMessage(configurationManager.getMessage("money.add.success")
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%player%", target.getDisplayName()));

                                target.sendMessage(configurationManager.getMessage("money.add.receive")
                                        .replaceAll("%player%", sender.getName())
                                        .replaceAll("%amount%", String.valueOf(amount)));
                            } else
                                sender.sendMessage(configurationManager.getMessage("money.add.success")
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%player%", args[1]));
                        } catch (SQLException | ExecutionException | InterruptedException e) {
                            sender.sendMessage(configurationManager.getMessage("money.add.faild"));
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(configurationManager.getMessage("PlayerNotFound"));
                        return false;
                    }
                } else {
                    sender.sendMessage(configurationManager.getMessage("money.add.usage"));
                    return false;
                }
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(!sender.hasPermission("moneysys.remove")) {
                    sender.sendMessage(configurationManager.getMessage("NoPermissionMessage"));
                    return false;
                }
                if(args.length == 3) {

                    if(UUIDFetcher.getUUID(args[1]) != null) {

                        UUID uuid = UUIDFetcher.getUUID(args[1]);

                        int amount = 0;
                        try {
                            amount = Integer.valueOf(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(configurationManager.getMessage("UnsupportedNumberFormat"));
                            return false;
                        }
                        try {
                            moneyManager.removeMoney(uuid, amount);
                            if(Bukkit.getPlayer(args[1]) != null) {
                                Player target = Bukkit.getPlayer(args[1]);

                                sender.sendMessage(configurationManager.getMessage("money.remove.success")
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%player%", target.getDisplayName()));

                                target.sendMessage(configurationManager.getMessage("money.remove.receive")
                                        .replaceAll("%player%", sender.getName())
                                        .replaceAll("%amount%", String.valueOf(amount)));
                            } else
                                sender.sendMessage(configurationManager.getMessage("money.remove.success")
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%player%", args[1]));
                        } catch (SQLException | ExecutionException | InterruptedException e) {
                            sender.sendMessage(configurationManager.getMessage("money.remove.faild"));
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(configurationManager.getMessage("PlayerNotFound"));
                        return false;
                    }
                } else {
                    sender.sendMessage(configurationManager.getMessage("money.remove.usage"));
                    return false;
                }
            } else if(args[0].equalsIgnoreCase("delete")) {
                if(!sender.hasPermission("moneysys.delete")) {
                    sender.sendMessage(configurationManager.getMessage("NoPermissionMessage"));
                    return false;
                }
                if(args.length == 2) {
                    if(UUIDFetcher.getUUID(args[1]) != null) {
                        UUID uuid = UUIDFetcher.getUUID(args[1]);

                        try {
                            moneyManager.deletePlayer(uuid);
                            if(Bukkit.getPlayer(args[1]) != null) {
                                Player target = Bukkit.getPlayer(args[1]);

                                sender.sendMessage(configurationManager.getMessage("money.delete.success")
                                        .replaceAll("%player%", target.getDisplayName()));

                                target.sendMessage(configurationManager.getMessage("money.delete.receive")
                                        .replaceAll("%player%", sender.getName()));
                            } else
                                sender.sendMessage(configurationManager.getMessage("money.delete.success")
                                        .replaceAll("%player%", args[1]));
                        } catch (SQLException e) {
                            sender.sendMessage(configurationManager.getMessage("money.delete.faild"));
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(configurationManager.getMessage("PlayerNotFound"));
                        return false;
                    }
                } else {
                    sender.sendMessage(configurationManager.getMessage("money.delete.usage"));
                    return false;
                }
            } else if(args[0].equalsIgnoreCase("top")) {
                try {
                    HashMap<UUID, Double> map = moneyManager.getTopTen();

                    sender.sendMessage(configurationManager.getMessage("money.top.header"));

                    int count = 0;

                    for(Map.Entry<UUID, Double> entry : map.entrySet()) {
                        count++;

                        UUID uuid = entry.getKey();
                        Double money = entry.getValue();

                        String playername = UUIDFetcher.getName(uuid);

                        sender.sendMessage(configurationManager.getMessage("money.top.content")
                                .replaceAll("%place%", String.valueOf(count))
                                .replaceAll("%player%", playername)
                                .replaceAll("%money%", String.valueOf(money)));

                    }

                    sender.sendMessage(configurationManager.getMessage("money.top.footer"));

                } catch (SQLException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else if(args[0].equalsIgnoreCase("help")) {
                sendHelp(sender);
            } else if(UUIDFetcher.getUUID(args[0]) != null) {
                UUID target = UUIDFetcher.getUUID(args[0]);

                try {
                    if(moneyManager.existsPlayer(target)) {
                        sender.sendMessage(configurationManager.getMessage("money.showBankBalanceOther")
                                .replaceAll("%player%", args[0])
                                .replaceAll("%amount%", String.valueOf(moneyManager.getMoney(target))));
                    } else {
                        sender.sendMessage(configurationManager.getMessage("BankAccountNotExists"));
                        return false;
                    }
                } catch (SQLException | ExecutionException | InterruptedException e) {
                    sender.sendMessage(configurationManager.getMessage("money.showBankBalanceOtherFail").replaceAll("%player%", args[0]));
                    e.printStackTrace();
                }
            } else {
                sendHelp(sender);
            }
        }
        return false;
    }

    private static void sendHelp(CommandSender player) {

        Map<String, String> helpCommands = new TreeMap<>();

        helpCommands.put("/money §8[§7Spieler§8]", "Zeigt dir deinen oder einen Kontostand eines spielers an.");
        helpCommands.put("/money set §8<§7Spieler§8> <§7Betrag§8>", "Setze den Kontostand eines Spielers neu.");
        helpCommands.put("/money add §8<§7Spieler§8> <§7Betrag§8>", "Bestimmten Betrag einem Spieler hinzufügen.");
        helpCommands.put("/money remove §8<§7Spieler§8> <§7Betrag§8>", "Bestimmten Betrag einem Spieler abziehen.");
        helpCommands.put("/money delete §8<§7Spieler§8>", "Bestimmten Betrag einem Spieler abziehen.");
        helpCommands.put("/money top", "Zeigt dir die Top 10 Spieler.");
        helpCommands.put("/pay §8<§7Spieler§8> <§7Betrag§8>", "Überweise einem Spieler einen bestimmten Betrag.");

        player.sendMessage(configurationManager.getMessage("money.help.header"));

        for(Map.Entry<String, String> entry : helpCommands.entrySet()) {
            String command = entry.getKey();
            String description = entry.getValue();

            player.sendMessage(configurationManager.getMessage("money.help.content")
                    .replaceAll("%command%", command)
                    .replaceAll("%description%", description));
        }

        player.sendMessage(configurationManager.getMessage("money.help.footer"));
    }
}
