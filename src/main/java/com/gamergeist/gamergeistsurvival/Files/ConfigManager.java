package com.gamergeist.gamergeistsurvival.Files;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

public class ConfigManager {

    private static GamerGeistSurvival plugin;
    private static ConfigManager instance;

    private static final HashMap<String, config> configs = new HashMap<>();

    public ConfigManager(GamerGeistSurvival plugin) {
        this.instance = this;
        this.plugin = plugin;
        createConfig();
    }

    public static ConfigManager getinstance() {
        return instance;
    }

    public config getConfig(String configname) {
        return configs.get(configname);
    }

    public void createConfig() {

        new config("TeamSettings.yml");
        new config("messages.yml");

        configs.values().forEach(config::saveDefaultConfig);
    }

    public void reloadConfig() {
        configs.values().forEach(config::reloadConfig);
    }

    public static class config {
        private final String configname;
        private FileConfiguration config = null;
        private File configFile = null;
        private String subfolder;
        private final File parent;

        public config(String ConfigName, String subfolder) {
            this.configname = ConfigName;
            this.subfolder = subfolder;
            this.parent = new File(plugin.getDataFolder(), subfolder);
            if (!parent.exists()) {
                parent.mkdir();
            }
            if (subfolder != "") {
                this.subfolder += "\\";
            }
            configs.put(configname, this);
        }

        public config(String ConfigName) {
            this.configname = ConfigName;
            this.parent = plugin.getDataFolder();
            if (!parent.exists()) {
                parent.mkdir();
            }
            configs.put(configname, this);
        }

        public void reloadConfig() {
            if (this.configFile == null) {
                this.configFile = new File(parent, configname);
            }
            this.config = YamlConfiguration.loadConfiguration(this.configFile);
            InputStream defaultStream = plugin.getResource(configname);
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                this.config.setDefaults(defaultConfig);
            }
        }

        public FileConfiguration getConfig() {
            if (config == null)
                reloadConfig();
            return config;
        }

        public void saveConfig() {
            if (this.config == null || this.configFile == null)
                return;
            try {
                getConfig().save(this.configFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "could not save config to" + this.configFile, e);
            }
        }

        public void saveDefaultConfig() {
            if (this.config == null) {
                this.configFile = new File(parent, configname);
            }
            if (!this.configFile.exists()) {
                if(subfolder != null){
                    plugin.saveResource(subfolder + configname, false);
                    }
                else{
                    plugin.saveResource(configname, false);
                }
            }
        }
    }
}
