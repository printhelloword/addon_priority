package com.biller.addonpriority;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.io.File;

public class MyApplicationProperties {
    private static PropertiesConfiguration configuration = null;

    static {
        try {
//            File f = new File("setting.properties");
            configuration = new PropertiesConfiguration(new File("setting.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
    }

    public static synchronized String getProperty(final String key) {
        return (String) configuration.getProperty(key);
    }
}
