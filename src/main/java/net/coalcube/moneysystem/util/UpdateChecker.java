package net.coalcube.moneysystem.util;

import net.coalcube.moneysystem.MoneySystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {
    private int project;
    private URL checkURL;
    private String newVersion;

    public UpdateChecker(int projectID) {
        project = projectID;
        newVersion = MoneySystem.getInstance().getDescription().getVersion();
        try {
            checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
        } catch(MalformedURLException e) {
            MoneySystem.getInstance().getLogger().severe("§7Could not connect to Spigotmc.org!");
        }
    }
    public String getResourceUrl() {
        return "https://spigotmc.org/resources/" + project;
    }
    public boolean checkForUpdates() throws Exception {
        URLConnection con = checkURL.openConnection();
        newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        return !MoneySystem.getInstance().getDescription().getVersion().equals(newVersion);
    }
}