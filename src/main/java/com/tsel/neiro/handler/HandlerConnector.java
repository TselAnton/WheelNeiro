package com.tsel.neiro.handler;

import io.github.bonigarcia.wdm.WebDriverManager;
import javax.annotation.PreDestroy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class HandlerConnector {

    private final WebDriver webDriver;

    public HandlerConnector(@Autowired HandlerSettings settings) {
        WebDriverManager.chromedriver().setup();
        this.webDriver = new ChromeDriver();
        webDriver.manage().window().setSize(new Dimension(1, 1));
        webDriver.manage().window().setPosition(new Point(0, 0));
        webDriver.get(settings.getWebSite());
    }

    public String getHtml() {
        return webDriver.getPageSource();
    }

    public void refreshPage() {
        this.webDriver.navigate().refresh();
    }

    @PreDestroy
    public void closeConnection() {
        this.webDriver.close();
    }
}
