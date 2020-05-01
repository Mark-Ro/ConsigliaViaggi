package com.consigliaviaggi.DAO;

public class RequestDetailsUtenteInsert {

    private String nickname,email,nome,cognome,nomePubblico;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNomePubblico() {
        return nomePubblico;
    }

    public void setNomePubblico(String nomePubblico) {
        this.nomePubblico = nomePubblico;
    }
}
