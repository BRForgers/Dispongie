package me.d4rk.dispongie;

import me.d4rk.dispongie.data.Config;
import me.d4rk.dispongie.data.WhitelistLink;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Manager {

    private static Dispongie plugin = Dispongie.getInstance();

    public static Config config;
    public static WhitelistLink whitelistLink;

    public static Path getSaveFile(String nome) {
        String location = plugin.privateConfigDir.toString() + File.separator;
        plugin.getLogger().debug(location);
        return getPath(location + nome);
    }

    public static Path getPath(String file) {
        try {
            return Paths.get(file + ".json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadConfig() {
        try {
            config = Dispongie.JSON.fromJson(new String(Files.readAllBytes(getSaveFile("config")), Charset.forName("UTF-8")), Config.class);
            plugin.getLogger().info("Config loaded!");
        } catch (Exception e) {
            config = new Config();
            saveConfig();
            plugin.getLogger().warn("Creating config!");
        }
        Dispongie.config = config;
    }
    public static void saveConfig() {
        config = new Config();
        config = Dispongie.config;
        try {
            Files.write(getSaveFile("config"), Dispongie.JSON.toJson(config).getBytes(Charset.forName("UTF-8")));
            plugin.getLogger().info("Config saved!");
        } catch (Exception e) {
            plugin.getLogger().warn("Failed to save config!");
            throw new RuntimeException(e);
        }
    }
    public static void loadWhitelist() {
        try {
            whitelistLink = Dispongie.JSON.fromJson(new String(Files.readAllBytes(getSaveFile("whitelistlink")), Charset.forName("UTF-8")), WhitelistLink.class);
            plugin.getLogger().info("whitelistLink loaded!");
        } catch (Exception e) {
            whitelistLink = new WhitelistLink();
            saveWhitelist();
            plugin.getLogger().warn("Creating whitelistLink!");
        }
        Dispongie.whitelistLink = whitelistLink;
    }
    public static void saveWhitelist() {
        whitelistLink = new WhitelistLink();
        whitelistLink = Dispongie.whitelistLink;
        try {
            Files.write(getSaveFile("whitelistlink"), Dispongie.JSON.toJson(whitelistLink).getBytes(Charset.forName("UTF-8")));
            plugin.getLogger().info("whitelistLink saved!");
        } catch (Exception e) {
            plugin.getLogger().warn("Failed to save whitelistLink!");
            throw new RuntimeException(e);
        }
    }

}
