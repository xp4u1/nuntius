package com.xp4u1.nuntius;

import com.xp4u1.nuntius.command.ReportCommand;
import com.xp4u1.nuntius.listener.ReportListener;

import java.util.ArrayList;

import net.md_5.bungee.api.plugin.Plugin;

public final class Nuntius extends Plugin {
    private static Nuntius instance;
    private final Config config = new Config();
    /**
     * List of members who do not want to receive reports.
     */
    private final ArrayList<String> teamBlacklist = new ArrayList<>();

    public Nuntius() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading Nuntius...");

        config.loadConfig();

        getProxy()
                .getPluginManager()
                .registerCommand(this, new ReportCommand(this));
        getProxy().getPluginManager().registerListener(this, new ReportListener());

        getLogger().info("Successfully started Nuntius.");
    }

    public Config getConfig() {
        return config;
    }

    public ArrayList<String> getTeamBlacklist() {
        return teamBlacklist;
    }

    public static String getMessage(String message) {
        return getInstance().getConfig().getMessages().getString(message);
    }

    public static Nuntius getInstance() {
        return instance;
    }
}
