package me.d4rk.dispongie;

import me.d4rk.dispongie.data.Config;
import org.slf4j.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Manager {

    private static Dispongie plugin = Dispongie.getInstance();

    public static Config config;

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
}
