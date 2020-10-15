package com.biller.addonprior.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(ParsingPK.class)
public class Parsing {
    private int kodeModul;
    private String kodeProduk;
    private String perintah;
    private byte aktif;
    private short prioritas;
    private Double hargaBeli;
    private String keterangan;
    private String kodeHlr;

    @Id
    @Column(name = "kode_modul", nullable = false)
    public int getKodeModul() {
        return kodeModul;
    }

    public void setKodeModul(int kodeModul) {
        this.kodeModul = kodeModul;
    }

    @Id
    @Column(name = "kode_produk", nullable = false, length = 10)
    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    @Basic
    @Column(name = "perintah", nullable = true, length = 4000)
    public String getPerintah() {
        return perintah;
    }

    public void setPerintah(String perintah) {
        this.perintah = perintah;
    }

    @Basic
    @Column(name = "aktif", nullable = false)
    public byte getAktif() {
        return aktif;
    }

    public void setAktif(byte aktif) {
        this.aktif = aktif;
    }

    @Basic
    @Column(name = "prioritas", nullable = false)
    public short getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(short prioritas) {
        this.prioritas = prioritas;
    }

    @Basic
    @Column(name = "harga_beli", nullable = true)
    public Double getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(Double hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    @Basic
    @Column(name = "keterangan", nullable = true, length = 255)
    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Basic
    @Column(name = "kode_hlr", nullable = true, length = 30)
    public String getKodeHlr() {
        return kodeHlr;
    }

    public void setKodeHlr(String kodeHlr) {
        this.kodeHlr = kodeHlr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parsing parsing = (Parsing) o;
        return kodeModul == parsing.kodeModul &&
                aktif == parsing.aktif &&
                prioritas == parsing.prioritas &&
                Objects.equals(kodeProduk, parsing.kodeProduk) &&
                Objects.equals(perintah, parsing.perintah) &&
                Objects.equals(hargaBeli, parsing.hargaBeli) &&
                Objects.equals(keterangan, parsing.keterangan) &&
                Objects.equals(kodeHlr, parsing.kodeHlr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kodeModul, kodeProduk, perintah, aktif, prioritas, hargaBeli, keterangan, kodeHlr);
    }
}
