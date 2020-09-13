package com.tsel.neiro.handler.connector;

public interface Connector {

    public String getHtml();

    public void refreshPage();

    public void closeConnection();
}
