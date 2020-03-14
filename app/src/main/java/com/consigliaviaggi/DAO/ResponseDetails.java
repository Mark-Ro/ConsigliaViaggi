package com.consigliaviaggi.DAO;

public class ResponseDetails {
    private String messageID;
    private String messageReason;
    private String resultQuery;

    public String getMessageID() {
        return messageID;
    }

    public String getMessageReason() {
        return messageReason;
    }

    public String getResultQuery() {
        return  resultQuery;
    }

    public void setMessageID(String messageID) {
        this.messageID=messageID;
    }

    public void setMessageReason(String messageReason) {
        this.messageReason=messageReason;
    }

    public void setResultQuery(String resultQuery) {
        this.resultQuery=resultQuery;
    }
}
