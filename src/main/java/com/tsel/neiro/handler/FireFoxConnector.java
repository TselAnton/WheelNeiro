package com.tsel.neiro.handler;

import com.tsel.neiro.handler.connector.Connector;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Lazy
@Scope("singleton")
@Component("FireFoxConnector")
public class FireFoxConnector implements Connector {

    private final WebDriver webDriver;

    public FireFoxConnector(@Autowired HandlerSettings settings) {
        WebDriverManager.firefoxdriver().setup();
        this.webDriver = new FirefoxDriver();
        webDriver.manage().window().setSize(new Dimension(1, 1));
        webDriver.manage().window().setPosition(new Point(0, 0));
        webDriver.get(settings.getWebSite());
    }

    @Override
    public String getHtml() {
        return webDriver.getPageSource();
    }

    @Override
    public void refreshPage() {
        this.webDriver.navigate().refresh();
    }

    @Override
    @PreDestroy
    public void closeConnection() {
        this.webDriver.close();
    }
}
