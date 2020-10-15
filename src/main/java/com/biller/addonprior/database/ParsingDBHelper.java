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

    public List<Object[]> getParsing(String[] arrayProductCode) {

        Session session = getSession();

        List<Object[]> results = null;
        try {

            List<String> productCodes = Arrays.asList(arrayProductCode);

            Query query = session.
                    createNativeQuery("select kode_modul, kode_produk, harga_beli, prioritas, aktif from parsing " +
                            "where aktif=1 " +
                            "and harga_beli is not null " +
                            "and kode_produk in :productCodes " +
                            "order by harga_beli asc");
            query.setParameter("productCodes", productCodes);

            results = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            Application.log.fatal(e);
        } finally {
            session.close();
        }

        return results;
    }

    public boolean updatePriority(Map collectionProductCode) {
        boolean isSuccess = false;
        Session session = null;
        Transaction tx = null;

        try {
            session = getSession();
            tx = session.beginTransaction();
            List<String> productCodes = new ArrayList<String>();
            String[] arrayProductCodes = new String[collectionProductCode.size()];
            String sql = "UPDATE dbo.parsing " + "SET prioritas = CASE ";
            int iter = 0;
            for (Object i : collectionProductCode.keySet()) {
                arrayProductCodes[iter] = i.toString();
                productCodes.add(i.toString());
                sql += "WHEN kode_produk = '" + i.toString() + "' THEN " + collectionProductCode.get(i) + " ";
                iter++;
            }
            sql += " END WHERE kode_produk IN :productCodes";
            Application.log.info("Product Code Size " + productCodes.size());

            Query query = session.
                    createNativeQuery(sql);
            query.setParameter("productCodes", Arrays.asList(arrayProductCodes));
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
