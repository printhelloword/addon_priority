package com.biller.addonpriority.database;

import com.biller.addonpriority.Application;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.*;

public class ParsingDBHelper {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public Session getSession() {
        return factory.openSession();
    }

    //    public List<Object[]> getParsing(String[] arrayProductCode) {
    public List<Object[]> getParsing(String productCode) {

        Session session = getSession();

        List<Object[]> results = null;
        try {

//            List<String> productCodes = Arrays.asList(arrayProductCode);

           /* Query query = session.
                    createNativeQuery("select kode_modul, kode_produk, harga_beli, prioritas, aktif from parsing " +
                            "where aktif=1 " +
                            "and harga_beli is not null " +
                            "and kode_produk in :productCodes " +
                            "order by harga_beli asc");*/
//            query.setParameter("productCodes", productCodes);
            Query query = session.
                    createNativeQuery("select kode_modul, kode_produk, harga_beli, prioritas\n" +
                            "from parsing " +
                            "where aktif=1 " +
                            "and kode_produk=:productCode " +
                            "and harga_beli is not null " +
                            "order by harga_beli asc");
            query.setParameter("productCode", productCode);

            results = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            Application.log.fatal(e);
        } finally {
            session.close();
        }

        return results;
    }

    public boolean updatePriority(Map collectionModuleCodes) {
        boolean isSuccess = false;
        Session session = null;
        Transaction tx = null;

        try {
            session = getSession();
            tx = session.beginTransaction();
            List<String> moduleCodes = new ArrayList<>();
            String[] arrayModuleCodes = new String[collectionModuleCodes.size()];
            String sql = "UPDATE dbo.parsing " + "SET prioritas = CASE ";
            int iter = 1;
            String tempModuleCode="";
            int tempIter=1;
            for (Object i : collectionModuleCodes.keySet()) {
//                arrayModuleCodes[iter] = i.toString();

                System.out.println("Current Itteration = " +iter);
                if (iter == 1) {
                    sql += "WHEN kode_modul = '" + i.toString() + "' THEN " + iter + " ";
                }else{
                    if (collectionModuleCodes.get(i).toString().equalsIgnoreCase(tempModuleCode)){
                        sql += "WHEN kode_modul = '" + i.toString() + "' THEN " + (iter-1) + " ";
                        tempIter=iter;
                    }else
                        sql += "WHEN kode_modul = '" + i.toString() + "' THEN " + tempIter + " ";
                }

                tempModuleCode = collectionModuleCodes.get(i).toString();
                moduleCodes.add(i.toString());
//                sql += "WHEN kode_modul = '" + i.toString() + "' THEN " + collectionModuleCodes.get(i) + " ";
                iter++;
            }
            sql += " END WHERE kode_modul IN :moduleCodes";
            Application.log.info("Module Codes Array Length -> " + moduleCodes.size());

            Query query = session.
                    createNativeQuery(sql);
            query.setParameter("moduleCodes", Arrays.asList(arrayModuleCodes));
            query.executeUpdate();
            tx.commit();
            isSuccess = true;
        } catch (Exception e) {
            Application.log.fatal(e);
            e.printStackTrace();
        } finally {
            session.close();
        }
        return isSuccess;
    }

}
