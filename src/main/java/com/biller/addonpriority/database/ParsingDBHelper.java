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

    public List<Object[]> getParsing(String productCode) {

        Session session = getSession();

        List<Object[]> results = null;
        try {
            Query query = session.
                    createNativeQuery("select kode_modul, kode_produk, harga_beli, prioritas\n" +
                            "from parsing " +
                            "where aktif=1 " +
                            "and kode_produk=:productCode " +
                            "and harga_beli is not null " +
                            "and harga_beli > 0 " +
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

    public boolean updatePriority(Map collectionModuleCodes, String productCode) {
        boolean isSuccess = false;
        Session session = null;
        Transaction tx = null;

        try {
            session = getSession();
            tx = session.beginTransaction();
            List<String> moduleCodes = new ArrayList<>();

            String sql = "UPDATE dbo.parsing " + "SET prioritas = CASE ";
            int iter = 1;
            String tempModuleCode = "";
            int tempIter = 0;
            String moduleCodesArr="";
            for (Object i : collectionModuleCodes.keySet()) {

                if (iter == 1) {
                    sql += "WHEN kode_modul = '" + i.toString() + "' THEN " + iter + " ";
                    System.out.println("set prioritas = "+iter);
                } else {
                    if (collectionModuleCodes.get(i).toString().equalsIgnoreCase(tempModuleCode)) {
                        tempIter++;
                    }
                    sql += "WHEN kode_modul = '" + i.toString() + "' THEN " + (iter - tempIter) + " ";
                    System.out.println("set prioritas = "+(iter-tempIter));
                }

                tempModuleCode = collectionModuleCodes.get(i).toString();
                moduleCodes.add(i.toString());
                iter++;
            }
//            sql += " END WHERE kode_modul IN :moduleCodes";
            String comaSeparatedModuleCodes = String.join(", ", moduleCodes);
            String productCodeParam = "'"+productCode+"'";
            System.out.println("module codes ->> "+comaSeparatedModuleCodes);
            sql += " END WHERE kode_modul IN ("+comaSeparatedModuleCodes+") AND kode_produk='"+productCode+"'";
//            sql += "END WHERE kode_modul IN ("+comaSeparatedModuleCodes+") AND kode_produk='"+productCode+"'";
            Application.log.info("Query -->>>> "+sql);
            Application.log.info("Module Codes Array Length -> " + moduleCodes.size());

            Query query = session.
                    createNativeQuery(sql);
//            query.setParameter("moduleCodes", comaSeparatedModuleCodes);
//            query.setParameter("productCode", productCodeParam);
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
