package dev._2lstudios.advancedauth.i18n;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.config.Configuration;
import dev._2lstudios.advancedauth.utils.FileUtils;

public class LanguageManager {
    private Map<String, Configuration> languages;
    private String defaultLanguage;
    private File directory;

    public LanguageManager(String defaultLanguage, File directory) {
        this.languages = new HashMap<>();
        this.defaultLanguage = defaultLanguage;
        this.directory = directory;

        LanguageExtractor.extractAll(directory);
    }

    public LanguageManager(AdvancedAuth plugin) {
        this(
            plugin.getConfig().getString("settings.default-lang"), 
            new File(plugin.getDataFolder(), "lang")
        );
    }

    public void loadLanguage(File file) throws IOException, InvalidConfigurationException {
        Configuration lang = new Configuration(file);
        lang.load();

        String name = FileUtils.getBaseName(file).toLowerCase();
        this.languages.put(name, lang);
    }

    public Configuration getLanguage(String name) {
        name = name.toLowerCase();

        if (languages.containsKey(name)) {
            return languages.get(name);
        } else if (languages.containsKey(name)) {
            return languages.get(name);
        } else if (languages.containsKey(name.split("[_]")[0])) {
            return languages.get(name.split("[_]")[0]);
        } else {
            return languages.get(this.getDefaultLocale());
        }
    }

    public void loadLanguages() throws IOException, InvalidConfigurationException {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                try {
                    this.loadLanguage(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadLanguagesSafe() {
        try {
            this.loadLanguages();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getDefaultLocale() {
        return this.defaultLanguage;
    }
}