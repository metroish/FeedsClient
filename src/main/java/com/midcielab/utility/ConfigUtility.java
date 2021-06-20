package com.midcielab.utility;

import java.io.File;
import java.io.FileWriter;
import com.midcielab.model.Config;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
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
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        options.setPrettyFlow(true);        
        Yaml yml = new Yaml(options);
        try {
            File file = new File(this.getClass().getClassLoader().getResource("config.yml").toURI().getPath());
            yml.dump(config, new FileWriter(file.getAbsolutePath()));
        } catch (Exception e) {          
            e.printStackTrace();
        } 
    }
}
