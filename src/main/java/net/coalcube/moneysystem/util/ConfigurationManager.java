package net.coalcube.moneysystem.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public class ConfigurationManager {

    private static FileConfiguration config;
    private static FileConfiguration messages;

    public ConfigurationManager(FileConfiguration config, FileConfiguration messages) {
        this.config = config;
        this.messages = messages;
    }

    public static void initConfig(FileConfiguration config) {
        config.set("mysql.host", "localhost");
        config.set("mysql.port", 3306);
        config.set("mysql.username", "root");
        config.set("mysql.password", "SicheresPasswort");
        config.set("mysql.database", "moneysystem");

        config.set("startCapital", 1000);
        config.set("currency", "$");
    }

    public static void initMessages(FileConfiguration messages) {
        messages.set("prefix", "§8§l┃ §eMoneySystem §8» §7");
        messages.set("NoPermissionMessage", "%P%§cDafür hast du keine Rechte!");
        messages.set("NoPlayerMessage", "%P%§cUm Diese Aktion ausführen zu können musst du ein Spieler sein!");
        messages.set("PlayerNotFound", "%P%§cDer Spieler wurde nicht gefunden.");
        messages.set("NoDBConnection", "%P%§cDie Datenbankverbindung besteht nicht. Wende dich bitte an einen Administrator.");
        messages.set("NotEnoughMoney", "%P%§cDu hast dafür nicht genug Geld.");
        messages.set("UnsupportedNumberFormat", "%P%§cBitte gib einen gültigen wert ein.");
        messages.set("BankAccountNotExists", "%P%§cDer angegebene Spieler hat kein Bankkonto.");


        messages.set("money.showBankBalance", "%P%Dein Kontostand beträgt §e%amount% %currency%§7.");
        messages.set("money.showBankBalanceFail", "%P%§cDein Kontostand konnte nicht aufgerufen werden. " +
                "Bitte wende dich an einen Administrator für mehr Informationen.");
        messages.set("money.showBankBalanceOther", "%P%Der Kontostand von §e%player% §7beträgt §e%amount% %currency%§7.");
        messages.set("money.showBankBalanceOtherFail", "%P%§cDer Kontostand von §e%player% §ckonnte nicht aufgerufen werden. " +
                "Bitte wende dich an einen Administrator für mehr Informationen.");

        messages.set("money.set.receive", "%P%Dein Konostand wurde von §e%player% §7auf §e%amount% %currency% §7gesetzt.");
        messages.set("money.set.success", "%P%Du hast den Konostand von §e%player% §7auf §e%amount% %currency% §7gesetzt.");
        messages.set("money.set.usage", "%P%Benutze §8» §e/money set §8<§7Betrag§8> §8<§7Spieler§8>");
        messages.set("money.set.faild", "%P%§cEs ist ein Fehler aufgetreten. Bitte wende dich an einen Administrator oder überprüfe die Konsole für mehr Informationen.");

        messages.set("money.add.receive", "%P%Dir wurden §e%amount% %currency% §2gutgeschrieben§7.");
        messages.set("money.add.success", "%P%Du hast %player% §e%amount% %currency% §2gutgeschrieben§7.");
        messages.set("money.add.usage", "%P%Benutze §8» §e/money add §8<§7Betrag§8> §8<§7Spieler§8>");
        messages.set("money.add.faild", "%P%§cEs ist ein Fehler aufgetreten. Bitte wende dich an einen Administrator oder überprüfe die Konsole für mehr Informationen.");

        messages.set("money.remove.receive", "%P%Dir wurden §e%amount% %currency% §centfernt§7.");
        messages.set("money.remove.success", "%P%Du hast %player% §e%amount% %currency% §centfernt§7.");
        messages.set("money.remove.usage", "%P%Benutze §8» §e/money remove §8<§7Betrag§8> §8<§7Spieler§8>");
        messages.set("money.remove.faild", "%P%§cEs ist ein Fehler aufgetreten. Bitte wende dich an einen Administrator oder überprüfe die Konsole für mehr Informationen.");

        messages.set("money.delete.receive", "%P%§cDein Bankkonto wurde gelöscht.");
        messages.set("money.delete.success", "%P%Du hast das Bankkonto von §e%player% §eerfolgreich §cgelöscht§7.");
        messages.set("money.delete.usage", "%P%Benutze §8» §e/money delete §8<§7Spieler§8>");
        messages.set("money.delete.faild", "%P%§cEs ist ein Fehler aufgetreten. Bitte wende dich an einen Administrator oder überprüfe die Konsole für mehr Informationen.");

        messages.set("money.top.header", Arrays.asList(new String[] {"%P%§m-----------§7» §eTop Liste §7«§7§m-----------", "%P%"}));
        messages.set("money.top.content", "%P%§e%place%. %player% §8- §7%money% %currency%");
        messages.set("money.top.footer", Arrays.asList(new String[] {"%P%", "%P%§m----------------------"}));

        messages.set("money.help.header", Arrays.asList(new String[] {"%P%§m-----------§7» §eMoney Befehle §7«§7§m-----------", "%P%"}));
        messages.set("money.help.content", "%P%§e%command% §8» §7%description%");
        messages.set("money.help.footer", Arrays.asList(new String[] {"%P%", "%P%§m----------------------"}));

        messages.set("pay.receive", "%P%Du hast §e%amount% %currency% §7 von §e%player% §2erhalten§7.");
        messages.set("pay.success", "%P%Du hast §e%amount% %currency% §7an §e%player% §7gezahlt.");
        messages.set("pay.all", "%P%Du hast §eallen Spielern §7einen Betrag von §e%amount% %currency% §7gezahlt.");
        messages.set("pay.usage", "%P%Benutze §8» §e/pay §8<§7Spieler§8> §8<§7Betrag§8>");
        messages.set("pay.faild", "%P%§cDer Betrag konnte nicht gezahlt werden.");
        messages.set("pay.notEnoughMoney", "%P%§cFür diese Aktion hast du nicht genug geld.");



    }

    public String getMessage(String path) {
        String msg = "";

        if(messages.get(path) instanceof List) {
            int count = 0;
            for(String line : messages.getStringList(path)) {
                if(messages.getStringList(path).size()-1 == count) {
                    msg = msg + line;
                } else {
                    msg = msg + line + "\n";
                }
                count ++;
            }
        } else
            msg = messages.getString(path);

        if(msg.contains("&"))
            msg = msg.replaceAll("&", "§");

        if(msg.contains("%P%"))
            msg = msg.replaceAll("%P%", messages.getString("prefix").replaceAll("&", "§"));

        if(msg.contains("%currency%"))
            if(config.getString("currency").equalsIgnoreCase("$"))
                msg = msg.replaceAll("%currency%", "\\$");
            else
                msg = msg.replaceAll("%currency%", config.getString("currency"));

        return msg;
    }

    public FileConfiguration getRawConfig() {
        return config;
    }

    public FileConfiguration getRawMessages() {
        return messages;
    }

}
