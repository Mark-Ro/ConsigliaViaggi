package com.consigliaviaggi.DAO;

public class RequestDetailsStrutturaCitta {

    private String nomeStruttura,citta,nazione,massimoPrezzo,voto;

    public String getNomeStruttura() {
        return nomeStruttura;
    }

    public void setNomeStruttura(String nomeStruttura) {
        this.nomeStruttura = nomeStruttura;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
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
