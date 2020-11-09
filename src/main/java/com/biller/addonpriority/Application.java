package com.biller.addonpriority;

import com.biller.addonpriority.database.ParsingDBHelper;
import com.biller.addonpriority.util.Settings;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

//@EnableConfigServer
@SpringBootApplication
public class Application {

    //FINALS
    private static final String SORTING_MODE_ALL = "0";
    private static final String SORTING_MODE_SPECIFIED = "1";

    private static final String PROPERTY_INTERVAL = "interval.update";
    private static final String PROPERTY_PRODUCT_CODE = "kode.produk";
    private static final String PROPERTY_SORTING_MODE = "mode.sorting";


    public static org.apache.log4j.Logger log = Logger.getLogger(Application.class);

    static String propertyProductCodes;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        while (true) {

            try {
                log.info("Modul Running");
                getProductCodeAndUpdate();
                showIntervalSetting();
                Thread.sleep(Integer.parseInt(Settings.getProperty(PROPERTY_INTERVAL)) * 1000); /*Sleep Thread in seconds*/
            } catch (Exception e) {
                e.printStackTrace();
                log.fatal(e);
                log.info("Invalid Settings. Please Check Settings Again");
                Thread.sleep(5000);
            }
        }

    }

    private static void showIntervalSetting() {
        log.info("Interval Update " + Settings.getProperty(PROPERTY_INTERVAL));
    }

    private static void getProductCodeAndUpdate() {
        String[] arrayproductCodes = getProductCodes();
        updatePriority(arrayproductCodes);
    }

    private static boolean isSortingAll() {
        if (Settings.getProperty(PROPERTY_SORTING_MODE).equalsIgnoreCase(SORTING_MODE_ALL))
            return true;
        else if (Settings.getProperty(PROPERTY_SORTING_MODE).equalsIgnoreCase(SORTING_MODE_SPECIFIED))
            return false;
        else
            return false;
    }

    private static String[] getProductCodes() {
        if (isSortingAll())
            return getAllProductCodes();
        else
            return getSpecificProductCodes();
    }

    private static String[] getAllProductCodes() {
        ParsingDBHelper parsingdb = new ParsingDBHelper();
        String[] productCodes = parsingdb.getAllProductCodes();
        if (productCodes != null) {
            return productCodes;
        } else {
            return null;
        }
    }

    private static String[] getSpecificProductCodes() {
        propertyProductCodes = Settings.getProperty(PROPERTY_PRODUCT_CODE);
        return propertyProductCodes.split(" ");
    }

    private static void updatePriority(String[] arrayProductCodes) {
        try {
            List<Object[]> parsing;
            ParsingDBHelper parsingdb = new ParsingDBHelper();
            for (int i = 0; i < arrayProductCodes.length; i++) {
                parsing = parsingdb.getParsing(arrayProductCodes[i]);
                if (parsing.isEmpty()) {
                    log.info("No Active Modules for " + arrayProductCodes[i]);
                } else {
                    log.info("List Of Actives Modules for Product Code " + arrayProductCodes[i]);

                    log.info("kode_modul|kode_produk|harga_beli|prioritas");
                    HashMap<String, String> productsToBeUpdate = new LinkedHashMap<>();
                    for (Object[] row : parsing) {
                        log.info(row[0] + " | " + row[1] + " | " + row[2] + " | " + row[3]);
                        productsToBeUpdate.put(row[0].toString(), row[2].toString());
                    }
                    if (parsingdb.updatePriority(productsToBeUpdate, arrayProductCodes[i])) {
                        log.info("Update Succeed");
                    } else
                        log.info("Update Failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.fatal(e.getMessage());
        }
    }


}
