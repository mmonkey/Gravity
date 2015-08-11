package com.github.mmonkey.Gravity.Configs;

import com.github.mmonkey.Gravity.Gravity;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class DefaultConfig extends Config {

    public static final String CONFIG_VERSION = "configVersion";
    public static final String DATABASE_VERSION = "databaseVersion";

    @Override
    public void load() {

        setConfigLoader(HoconConfigurationLoader.builder().setFile(getConfigFile()).build());

        try {

            if (!getConfigFile().isFile()) {
                getConfigFile().createNewFile();
                saveDefaults();
            }

            setConfig(getConfigLoader().load());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void saveDefaults() {

        try {

            setConfig(getConfigLoader().load());

        } catch (IOException e) {

            e.printStackTrace();

        }

        get().getNode(CONFIG_VERSION).setValue(1);
        get().getNode(DATABASE_VERSION).setValue(1);

        save();

    }

    public DefaultConfig(Gravity plugin, File configDir) {
        super(plugin, configDir);

        setConfigFile(new File(configDir, Gravity.NAME + ".conf"));
    }

}
