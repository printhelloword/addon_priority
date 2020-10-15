package com.biller.addonprior.util;

import com.biller.addonprior.Application;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class Settings {
    private static PropertiesConfiguration configuration = null;

    static {
        try {
            configuration = new PropertiesConfiguration(new File("setting.properties"));
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.fatal(e);
        }
        configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
    }

    public static synchronized String getProperty(final String key) {
        return (String) configuration.getProperty(key);
    }

    public static synchronized List<String> getPropertyArray(final String key) {
        return (List<String>) configuration.getProperty(key);
    }

}
