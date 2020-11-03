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

    public static org.apache.log4j.Logger log = Logger.getLogger(Application.class);

    static String propertyInterval;
    static String propertyProductCodes;
//    static List<String> listModuleCodes = Arrays.asList(arrayModuleCodes);


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        while (true) {

            try {
                log.info("Interval Update " + Settings.getProperty("interval.update"));
                updatePriority();
                Thread.sleep(Integer.parseInt(Settings.getProperty("interval.update")) * 1000); /*Sleep Thread in seconds*/
            } catch (Exception e) {
                e.printStackTrace();
                log.fatal(e);
                log.info("Invalid Settings. Please Check Settings Again");
                Thread.sleep(5000);
            }

        }

    }

    private static void updatePriority() {
        try {
            propertyProductCodes = Settings.getProperty("kode.produk");
            String[] arrayProductCodes = propertyProductCodes.split(" ");

            HashMap<String, String> productsToBeUpdate = new LinkedHashMap<>();

            List<Object[]> parsing;
            ParsingDBHelper parsingdb = new ParsingDBHelper();
            /*parsing = parsingdb.getParsing(arrayProductCodes);*/
            for (int i = 0; i < arrayProductCodes.length; i++) {
                parsing = parsingdb.getParsing(arrayProductCodes[i]);
                if (parsing.isEmpty()) {
                    log.info("No Active Modules for " + arrayProductCodes[i]);
                } else {
                    log.info("List Of Actives Modules for Product Code " + arrayProductCodes[i]);

                    log.info("kode_modul|kode_produk|harga_beli|prioritas");
                    /*int j = 1;*/
                    for (Object[] row : parsing) {
                        log.info(row[0] + " | " + row[1] + " | " + String.format("%,.2f", row[2])+ " | " +row[3]);
                        productsToBeUpdate.put(row[0].toString(), row[2].toString());
                        /*j++;*/
                    }
                    if (parsingdb.updatePriority(productsToBeUpdate)) {
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
