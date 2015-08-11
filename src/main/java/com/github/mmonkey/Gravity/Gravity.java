package com.github.mmonkey.Gravity;

import com.github.mmonkey.Gravity.Configs.DefaultConfig;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;

import java.io.File;

@Plugin(id = Gravity.ID, name = Gravity.NAME, version = Gravity.VERSION)
public class Gravity {

    public static final String ID = "Gravity";
    public static final String NAME = "Gravity";
    public static final String VERSION = "0.0.1";
    public static final int CONFIG_VERSION = 1;
    public static final int DATABASE_VERSION = 1;

    private Game game;
    private Optional<PluginContainer> pluginContainer;
    private static Logger logger;
    private DefaultConfig defaultConfig;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    public Game getGame() {
        return this.game;
    }

    public Optional<PluginContainer> getPluginContainer() {
        return this.pluginContainer;
    }

    public static Logger getLogger() {
        return logger;
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Subscribe
    public void onPreInit(PreInitializationEvent event) {

        this.game = event.getGame();
        this.pluginContainer = game.getPluginManager().getPlugin(Gravity.NAME);
        Gravity.logger = game.getPluginManager().getLogger(pluginContainer.get());

        getLogger().info(String.format("Starting up %s v%s.", Gravity.NAME, Gravity.VERSION));

        if (!this.configDir.isDirectory()) {
            if (this.configDir.mkdirs()) {
                getLogger().info("Gravity config directory successfully created!");
            }
        }

        // Load default config
        this.defaultConfig = new DefaultConfig(this, this.configDir);
        this.defaultConfig.load();

    }

    @Subscribe
    public void onInit(InitializationEvent event) {

    }
}
