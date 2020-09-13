package com.tsel.neiro.handler;

import com.tsel.neiro.handler.connector.Connector;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Lazy
@Scope("singleton")
@Component("ChromeConnector")
public class ChromeConnector implements Connector {

    private final WebDriver webDriver;

    public ChromeConnector(@Autowired HandlerSettings settings) {
        WebDriverManager.chromedriver().setup();
        this.webDriver = new ChromeDriver();
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
