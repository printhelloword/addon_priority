package com.biller.addonprior.database;

import com.biller.addonprior.Application;
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

    public List<Object[]> getParsing(String[] arrayModuleCode) {

        Session session = getSession();

        List<Object[]> results = null;
        try {

            List<String> moduleCodes = Arrays.asList(arrayModuleCode);

            Query query = session.
                    createNativeQuery("select kode_modul, kode_produk, harga_beli, prioritas, aktif from parsing " +
                            "where aktif=1 " +
                            "and harga_beli is not null " +
                            "and kode_modul in :moduleCodes " +
                            "order by harga_beli asc");
            query.setParameter("moduleCodes", moduleCodes);

            results = query.getResultList();

        } catch (Exception e) {
            Application.log.debug(" " + e.getMessage());
        } finally {
            session.close();
        }

        return results;
    }

    public boolean updatePriority(Map collectionModuleCode) {
        boolean isSuccess=false;
        Session session = null;
        Transaction tx = null;

        try {
            session = getSession();
            tx = session.beginTransaction();
            List<String> productCodes = new ArrayList<String>();
            String sql = "update parsing "+ "SET  prioritas = CASE ";
            for (Object i : collectionModuleCode.keySet()) {
                productCodes.add(i.toString());
//                Application.log.info("key: " +i.toString()+ " value: " + collectionModuleCode.get(i));
                sql += " WHEN kode_produk = "+i.toString()+" THEN " +collectionModuleCode.get(i)+ " ";
                System.out.println("key: " +i.toString()+ " value: " + collectionModuleCode.get(i));
            }
            sql += " END WHERE kode_produk IN :productCodes";

            System.out.println(sql);

            Query query = session.
                    createNativeQuery(sql);
            query.setParameter("productCodes", productCodes.toArray());
            query.executeUpdate();
            tx.commit();
            isSuccess=true;
        } catch (Exception e) {
            Application.log.fatal(e);
            e.printStackTrace();
        } finally {
            session.close();
        }

        return isSuccess;
    }

}
