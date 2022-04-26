package net.coalcube.moneysystem;

import net.coalcube.moneysystem.command.CMDmoney;
import net.coalcube.moneysystem.command.CMDpay;
import net.coalcube.moneysystem.listener.JoinEvent;
import net.coalcube.moneysystem.util.ConfigurationManager;
import net.coalcube.moneysystem.util.MoneyManager;
import net.coalcube.moneysystem.util.MySQL;
import net.coalcube.moneysystem.util.UpdateChecker;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MoneySystem extends JavaPlugin {

    private static Plugin instance;
    private ConsoleCommandSender console;
    private MoneyManager moneyManager;
    private ConfigurationManager configurationManager;
    private FileConfiguration config;
    private FileConfiguration messages;
    private MySQL mySQL;

    @Override
    public void onEnable() {
        instance = this;
        console = Bukkit.getConsoleSender();
        UpdateChecker updateChecker = new UpdateChecker(101672);
        PluginManager pluginManager = Bukkit.getPluginManager();

        console.sendMessage("§e  __  __                                  ____                  _                      ");
        console.sendMessage("§e |  \\/  |   ___    _ __     ___   _   _  / ___|   _   _   ___  | |_    ___   _ __ ___  ");
        console.sendMessage("§e | |\\/| |  / _ \\  | '_ \\   / _ \\ | | | | \\___ \\  | | | | / __| | __|  / _ \\ | '_ ` _ \\ ");
        console.sendMessage("§e | |  | | | (_) | | | | | |  __/ | |_| |  ___) | | |_| | \\__ \\ | |_  |  __/ | | | | | |");
        console.sendMessage("§e |_|  |_|  \\___/  |_| |_|  \\___|  \\__, | |____/   \\__, | |___/  \\__|  \\___| |_| |_| |_|");
        console.sendMessage("§e                                  |___/           |___/                             §fv"
                + this.getDescription().getVersion());

        try {
            createConfigurations();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mySQL = new MySQL(
                config.getString("mysql.host"),
                config.getInt("mysql.port"),
                config.getString("mysql.username"),
                config.getString("mysql.password"),
                config.getString("mysql.database"));

        try {
            mySQL.connect();
            console.sendMessage(messages.get("prefix") + "§7Die Datenbankverbindung wurde §2erfolgreich hergestellt§7.");
        } catch (SQLException e) {
            console.sendMessage(messages.get("prefix") + "§7Datenbankverbindung konnte §4nicht §7hergestellt werden.");
            console.sendMessage(messages.get("prefix") + "§cBitte überprüfe die eingetragenen MySQL daten in der Config.yml.");
            console.sendMessage(messages.get("prefix") + "§cDebug Message: §e" + e.getMessage());
        }

        if(mySQL.isConnected()) {
            try {
                mySQL.createTable();
                console.sendMessage(messages.get("prefix") + "§7Die MySQL Tabelle wurde §2erfolgreich erstellt§7.");
            } catch (SQLException e) {
                console.sendMessage(messages.get("prefix") + "§7Die MySQL Tabelle §ckonnte nicht erstellt werden§7.");
                console.sendMessage(messages.get("prefix") + "§cDebug Message: §e" + e.getMessage());
            }
        }

        moneyManager = new MoneyManager(mySQL);
        init(pluginManager);

        console.sendMessage(messages.get("prefix") + "§7Das Plugin wurde §2erfolgreich gestartet§7.");

        try {
            if (updateChecker.checkForUpdates()) {
                console.sendMessage(String.valueOf(new TextComponent(messages.get("prefix") + "§cEin neues Update ist verfügbar.")));
                console.sendMessage(String.valueOf(new TextComponent(messages.get("prefix") + "§7Lade es dir unter " +
                        "§ehttps://www.spigotmc.org/resources/bansystem-mit-ids.65863/ §7runter um aktuell zu bleiben.")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {

        try {
            mySQL.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        console.sendMessage(messages.get("prefix") + "§7Das Plugin wurde §cgestoppt§7.");
        super.onDisable();
    }

    private void init(PluginManager pluginManager) {
        getCommand("money").setExecutor(new CMDmoney(moneyManager, configurationManager));
        getCommand("pay").setExecutor(new CMDpay(moneyManager, configurationManager));

        pluginManager.registerEvents(new JoinEvent(moneyManager, config, configurationManager), this);
    }

    private void createConfigurations() throws IOException {
        File configFile = new File(this.getDataFolder(), "config.yml");
        File messagesFile = new File(this.getDataFolder(), "messages.yml");

        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        if(!configFile.exists()) {
            configFile.createNewFile();
            config = YamlConfiguration.loadConfiguration(configFile);
            ConfigurationManager.initConfig(config);
            config.save(configFile);
        }

        if(!messagesFile.exists()) {
            messagesFile.createNewFile();
            messages = YamlConfiguration.loadConfiguration(messagesFile);
            ConfigurationManager.initMessages(messages);
            messages.save(messagesFile);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        configurationManager = new ConfigurationManager(config, messages);
    }


    public ConsoleCommandSender getConsole() {
        return console;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public MoneyManager getMoneyManager() {
        return moneyManager;
    }

    public static Plugin getInstance() {
        return instance;
    }

}
