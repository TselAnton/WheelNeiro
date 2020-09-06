package com.tsel.neiro.handler;

import com.tsel.neiro.data.WheelColor;
import com.tsel.neiro.repository.WheelResultRepository;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Log4j2
@Component
public class ColorHandler {

    private static final Integer BLOCK_OF_COLOR = 350;
    private static final Integer PAST_BLOCK = 34;

    private final WheelResultRepository repository;
    private final HandlerSettings settings;
    private final Connector connector;

    private String html;

    public ColorHandler(@Autowired HandlerSettings settings, @Autowired WheelResultRepository repository,
                        @Autowired Connector connector) {
        this.settings = settings;
        this.repository = repository;
        this.connector = connector;
    }

    //TODO: Подумать над тем, чтобы запихнуть сюда Thread, либо сделать отдельный Runner для запуска/остановки работы хендлера

    @SneakyThrows
    public void handle() {
        this.html = updateHtml();

        Integer newColor = getCurrentColor();
        if (newColor == null) {
            log.warn("Can't handle last color");
        } else {
            log.info("Current color: {}", WheelColor.getColor(newColor));
            //TODO: Добавить добавление в БД, либо подумать над тем, чтобы отправлять евент во все возможные сервисы
        }

        connector.refreshPage();
        TimeUnit.SECONDS.sleep(3);
    }

    private String updateHtml() throws InterruptedException {
        if (isBlank(html)) {
            return getLastColorsHtml(connector.getHtml());
        } else {
            String newHtml = getLastColorsHtml(connector.getHtml());
            while (html.equals(newHtml)) {
                newHtml = getLastColorsHtml(connector.getHtml());
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
