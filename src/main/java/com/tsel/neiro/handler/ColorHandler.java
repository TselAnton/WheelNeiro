package com.tsel.neiro.handler;

import static com.tsel.neiro.utils.HandlerUtils.getColorName;
import static java.util.Optional.ofNullable;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.tsel.neiro.data.Result;
import com.tsel.neiro.exception.HandleColorException;
import com.tsel.neiro.handler.connector.Connector;
import com.tsel.neiro.repository.ResultRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Scope("singleton")
public class ColorHandler extends Thread {

    private static final Integer BLOCK_OF_COLOR = 450;
    private static final Integer PAST_BLOCK = 34;

    private final ResultRepository repository;
    private final HandlerSettings settings;
    private final Connector connector;

    private String html;
    private List<Integer> lastColors;
    private boolean isInterrupt;

    public ColorHandler(@Autowired HandlerSettings settings, @Autowired ResultRepository repository,
                        @Qualifier("FireFoxConnector") Connector connector) {
        this.settings = settings;
        this.repository = repository;
        this.connector = connector;
    }

    @Override
    @SneakyThrows
    public void run() {
        isInterrupt = false;
        while (!isInterrupt) {
            try {
                Optional<String> newHtml = updateHtml();
                if (newHtml.isPresent()) {
                    this.html = newHtml.get();
                    Integer newColor = getCurrentColor();

                    if (newColor != null) {
                        log.info("Current color: {}", getColorName(newColor));
                        repository.save(new Result(newColor));
                    }

                    connector.refreshPage();
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (HandleColorException e) {
                log.debug("Exception while handle colors = ", e);
            } catch (Exception e) {
                log.debug("Some other exception: ", e);
            }
        }
    }

    @Override
    public void interrupt() {
        this.isInterrupt = true;
        super.interrupt();
    }

    private Optional<String> updateHtml() throws InterruptedException, HandleColorException {
        if (isBlank(html)) {
            return getLastColorsHtml(connector.getHtml());
        } else {
            Optional<String> newHtml = getLastColorsHtml(connector.getHtml());
            while (!newHtml.isPresent() || html.equals(newHtml.get()) ) {
                newHtml = getLastColorsHtml(connector.getHtml());
                TimeUnit.SECONDS.sleep(1);
            }
            return newHtml;
        }
    }

    private Integer getCurrentColor() throws HandleColorException {
        String parsedHtml = html;
        List<Integer> parsedColors = new ArrayList<>();
        for (int i = 0; i < settings.getPointCount(); i++) {
            getColorFromHtmlBlock(parsedHtml)
                    .ifPresent(parsedColors::add);
            parsedHtml = removeLastColorBlock(parsedHtml).orElse(null);
        }
        if (isNewColor(parsedColors)) {
            lastColors = parsedColors;
            return parsedColors.get(0);
        }
        return null;
    }

    private boolean isNewColor(List<Integer> parsedColors) {
        if (lastColors == null || lastColors.isEmpty()) {
            return true;
        }
        return !lastColors.equals(parsedColors) &&
                lastColors.subList(0, lastColors.size() - 1).equals(parsedColors.subList(1, lastColors.size()));
    }

    private Optional<Integer> getColorFromHtmlBlock(String html) throws HandleColorException {
        try {
            return ofNullable(html)
                    .map(page -> page
                            .substring(page.lastIndexOf("<div class=\"past-color\"></div>") - PAST_BLOCK)
                            .substring(17, 18))
                    .map(Integer::parseInt);
        } catch (Exception e) {
            throw new HandleColorException(e);
        }
    }

    private Optional<String> removeLastColorBlock(String html) throws HandleColorException {
        try {
            return ofNullable(html)
                    .map(page -> page.substring(0,
                            page.lastIndexOf("<div class=\"past-color\"></div>") - PAST_BLOCK));
        } catch (Exception e) {
            throw new HandleColorException(e);
        }
    }

    private Optional<String> getLastColorsHtml(String html) throws HandleColorException {
        try {
            return ofNullable(html)
                    .map(page -> page.substring(
                            page.indexOf("<div id=\"past-queue-more\" style=\"display: none;\">") -
                                (BLOCK_OF_COLOR * settings.getPointCount()),
                            page.indexOf("<div id=\"past-queue-more\" style=\"display: none;\">")));
        } catch (Exception e) {
            throw new HandleColorException(e);
        }
    }
}
