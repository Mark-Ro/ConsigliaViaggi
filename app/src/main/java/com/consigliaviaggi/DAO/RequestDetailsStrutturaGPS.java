package com.consigliaviaggi.DAO;

public class RequestDetailsStrutturaGPS {

    private String nomeStruttura,latitudine,longitudine,massimoPrezzo,voto;

    public String getNomeStruttura() {
        return nomeStruttura;
    }

    public void setNomeStruttura(String nomeStruttura) {
        this.nomeStruttura = nomeStruttura;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public String getMassimoPrezzo() {
        return massimoPrezzo;
    }

    public void setMassimoPrezzo(String massimoPrezzo) {
        this.massimoPrezzo = massimoPrezzo;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }
}
