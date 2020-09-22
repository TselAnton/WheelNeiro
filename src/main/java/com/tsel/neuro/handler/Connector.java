package com.tsel.neuro.handler;

import static java.lang.String.format;

import io.github.bonigarcia.wdm.WebDriverManager;
import javax.annotation.PreDestroy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Two connectors: chrome and firefox
 */
@Lazy
@Component
@Scope("singleton")
public class Connector {

    private static final String CHROME_NAME = "chrome";
    private static final String FIREFOX_NAME = "firefox";

    private final WebDriver webDriver;

    public Connector(@Autowired HandlerSettings settings) {
        if (settings.getConnectorName().equalsIgnoreCase(CHROME_NAME)) {
            WebDriverManager.chromedriver().setup();
            webDriver = new ChromeDriver();
            webDriver.manage().window().setSize(new Dimension(1, 1));
            webDriver.manage().window().setPosition(new Point(0, 0));
            webDriver.get(settings.getWebSite());
        } else if (settings.getConnectorName().equalsIgnoreCase(FIREFOX_NAME)) {
            WebDriverManager.firefoxdriver().setup();
            webDriver = new FirefoxDriver();
            webDriver.manage().window().setSize(new Dimension(1, 1));
            webDriver.manage().window().setPosition(new Point(0, 0));
            webDriver.get(settings.getWebSite());
        } else {
            throw new BeanInitializationException(
                    format("Can't find connector with name \"%s\"", settings.getConnectorName()));
        }
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
