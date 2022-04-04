package com.xp4u1.nuntius;

import com.xp4u1.nuntius.api.report.Reason;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Config {
    private Configuration configuration;

    private final ArrayList<Reason> reasons = new ArrayList<>();
    private String teamPermission;
    private String jumpCommand;
    private Configuration messages;

    public void loadConfig() {
        if (!Nuntius.getInstance().getDataFolder().exists())
            //noinspection ResultOfMethodCallIgnored
            Nuntius.getInstance().getDataFolder().mkdir();

        File file = new File(Nuntius.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = Nuntius.getInstance().getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                Nuntius.getInstance().getLogger().severe("Cannot create new config.");
                e.printStackTrace();
            }
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Nuntius.getInstance().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            Nuntius.getInstance().getLogger().severe("Cannot load configuration.");
            e.printStackTrace();
        }

        readConfig();
    }

    private void readConfig() {
        teamPermission = configuration.getString("plugin.teamPermission");
        jumpCommand = configuration.getString("plugin.jumpCommand");
        for (Object entry : configuration.getList("plugin.reasons")) {
            @SuppressWarnings("unchecked") LinkedHashMap<String, String> hashMap = (LinkedHashMap<String, String>) entry;
            reasons.add(new Reason(
                    hashMap.get("title"),
                    hashMap.get("description")));
        }
        messages = configuration.getSection("messages");
    }

    public ArrayList<Reason> getReasons() {
        return reasons;
    }

    public String getTeamPermission() {
        return teamPermission;
    }

    public String getJumpCommand() {
        return jumpCommand;
    }

    public Configuration getMessages() {
        return messages;
    }
}
