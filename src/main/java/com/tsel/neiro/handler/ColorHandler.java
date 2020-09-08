package com.tsel.neiro.handler;

import static com.tsel.neiro.utils.HandlerUtils.getColorName;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.tsel.neiro.data.Result;
import com.tsel.neiro.repository.ResultRepository;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Scope("singleton")
public class ColorHandler extends Thread {

    private static final Integer BLOCK_OF_COLOR = 350;
    private static final Integer PAST_BLOCK = 34;

    private final ResultRepository repository;
    private final HandlerSettings settings;
    private final HandlerConnector handlerConnector;

    private String html;
    private boolean isInterrupt;

    public ColorHandler(@Autowired HandlerSettings settings, @Autowired ResultRepository repository,
                        @Autowired HandlerConnector handlerConnector) {
        this.settings = settings;
        this.repository = repository;
        this.handlerConnector = handlerConnector;
    }

    @Override
    @SneakyThrows
    public void run() {
        isInterrupt = false;
        while (!isInterrupt) {
            this.html = updateHtml();

            Integer newColor = getCurrentColor();
            if (newColor == null) {
                log.warn("Can't handle last color");
            } else {
                log.info("Current color: {}", getColorName(newColor));
                repository.save(new Result(newColor));
            }

            handlerConnector.refreshPage();
            TimeUnit.SECONDS.sleep(3);
        }
    }

    @Override
    public void interrupt() {
        this.isInterrupt = true;
        super.interrupt();
    }

    private String updateHtml() throws InterruptedException {
        if (isBlank(html)) {
            return getLastColorsHtml(handlerConnector.getHtml());
        } else {
            String newHtml = getLastColorsHtml(handlerConnector.getHtml());
            while (html.equals(newHtml)) {
                newHtml = getLastColorsHtml(handlerConnector.getHtml());
                TimeUnit.SECONDS.sleep(1);
            }
            return newHtml;
        }
    }

    private Integer getCurrentColor() {
        try {
            String parsedHtml = this.html;
            return getColorFromHtmlBlock(parsedHtml);
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    private Integer getColorFromHtmlBlock(String html) {
        String number = html.substring(
                html.lastIndexOf("<div class=\"past-color\"></div>") - PAST_BLOCK).substring(17, 18);
        return Integer.parseInt(number);
    }

    private String getLastColorsHtml(String html) {
        try {
            return html.substring(
                    html.indexOf("<div id=\"past-queue-more\" style=\"display: none;\">") -
                            (BLOCK_OF_COLOR * settings.getPointCount()),
                    html.indexOf("<div id=\"past-queue-more\" style=\"display: none;\">"));
        } catch (Exception e) {
            log.error("Html size = {}, {}", html.length(), e);
            return null;
        }
    }
}
