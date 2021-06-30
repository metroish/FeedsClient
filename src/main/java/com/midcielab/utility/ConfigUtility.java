package com.midcielab.utility;

import java.io.File;
import java.io.FileWriter;
import com.midcielab.model.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ConfigUtility {

    private static ConfigUtility instance = new ConfigUtility();
    private static final Logger logger = LogManager.getLogger();
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
        var options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        yml = new Yaml(options);
        try {
            var file = new File(this.getClass().getClassLoader().getResource("config.yml").toURI().getPath());
            yml.dump(config, new FileWriter(file.getAbsolutePath()));
        } catch (Exception e) {
            logger.error("Dump result to config.yml fail", e);
        }
    }
}
