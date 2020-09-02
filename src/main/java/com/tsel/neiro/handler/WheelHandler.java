package com.tsel.neiro.handler;

import com.tsel.neiro.data.WheelColor;
import com.tsel.neiro.data.WheelResult;
import com.tsel.neiro.repository.WheelResultRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Log4j2
@Getter
@Setter
@Component
public class WheelHandler implements Runnable {

    private final WebDriver webDriver;

    private WheelResultRepository repository;

    private WheelHandlerSettings settings;

    private List<Integer> lastColors;

    private int counter;

    public WheelHandler(@Autowired WheelHandlerSettings settings, @Autowired WheelResultRepository repository) {
        this.settings = settings;
        this.repository = repository;

        WebDriverManager.chromedriver().setup();
        this.webDriver = new ChromeDriver();
        webDriver.manage().window().setSize(new Dimension(1, 1));
        webDriver.manage().window().setPosition(new Point(0, 0));
        webDriver.get(this.settings.getWebSite());

        counter = 0;
        lastColors = new LinkedList<>();
    }

    @PreDestroy
    public void closeConnection() {
        this.webDriver.close();
    }

    public void refreshPage() {
        this.webDriver.navigate().refresh();
        counter = 0;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            parseLastColorsOfWheel()
                    .stream()
                    .sorted((Collections.reverseOrder()))
                    .forEach(c -> repository.save(new WheelResult(c)));

            Thread.sleep(settings.getTimeout());
        }
    }

    private List<Integer> parseLastColorsOfWheel() {
        long t1 = System.currentTimeMillis();
        List<Integer> newColors;

        try {
            counter += 1;
            String html = webDriver.getPageSource();

            if (html == null) {
                refreshPage();
                return emptyList();
            }

            html = getLastFiveColors(html);
            List<Integer> colors = new LinkedList<>();

            for (int i = 0; i < 20; i++) {
                colors.add(parseHtml(html));
                html = removeLastColor(html);
            }
            newColors = synchronizeInfo(colors);

            if (counter == settings.getTicksToUpdate()) {
                refreshPage();
            }

        } catch (Exception e) {
            return emptyList();
        }

        long t2 = System.currentTimeMillis() - t1;
        if (t2 > 1000) {
            log.warn("Time to collect colors: " + (t2) + " ms");
        } else {
            log.info("Time to collect colors: " + (t2) + " ms");
        }

        return newColors;
    }

    private String removeLastColor(String html) {
        return html.substring(0, html.lastIndexOf("<div class=\"past-color\"></div>") - 34);
    }

    private Integer parseHtml(String html) {
        String number = html.substring(html.lastIndexOf("<div class=\"past-color\"></div>") - 34).substring(17, 18);
        return Integer.valueOf(number);
    }

    private String getLastFiveColors(String html) {
        return html.substring(
                html.indexOf("<div id=\"past-queue-more\" style=\"display: none;\">") - 7000,
                html.indexOf("<div id=\"past-queue-more\" style=\"display: none;\">"));
    }

    private List<Integer> synchronizeInfo(List<Integer> colors) {
        List<Integer> result = emptyList();
        if (!lastColors.equals(colors) && !lastColors.isEmpty()) {
            List<Integer> newColors = new LinkedList<>(singletonList(colors.get(0)));
            int index = 1;

            for (int i = 1; i < colors.size(); i++) {
                if (!colors.subList(index, colors.size()).equals(lastColors.subList(0, lastColors.size() - index))) {
                    newColors.add(colors.get(index++));
                } else {
                    newColors.forEach(c -> log.info("Current color is: {}", WheelColor.getColor(c).toString()));
                    result = newColors;
                    break;
                }
            }
        }
        this.lastColors = colors;
        return result;
    }
}
