package com.midcielab.utility;

import com.midcielab.model.Config;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ConfigUtility {

    private static ConfigUtility instance = new ConfigUtility();
    private Yaml yml = new Yaml(new Constructor(Config.class));

    private ConfigUtility() {

    }

    public static ConfigUtility getInsance() {
        return instance;
    }

    public Config getConfig() {
        return yml.load(this.getClass().getClassLoader().getResourceAsStream("config.yml"));
    }

    public void saveConfig(Config config) {
        // TODO: save to config.yml
    }
}
