package com.biller.addonpriority.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParsingPK implements Serializable {
    private int kodeModul;
    private String kodeProduk;

    @Column(name = "kode_modul", nullable = false)
    @Id
    public int getKodeModul() {
        return kodeModul;
    }

    public void setKodeModul(int kodeModul) {
        this.kodeModul = kodeModul;
    }

    @Column(name = "kode_produk", nullable = false, length = 10)
    @Id
    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsingPK parsingPK = (ParsingPK) o;
        return kodeModul == parsingPK.kodeModul &&
                Objects.equals(kodeProduk, parsingPK.kodeProduk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kodeModul, kodeProduk);
    }
}
