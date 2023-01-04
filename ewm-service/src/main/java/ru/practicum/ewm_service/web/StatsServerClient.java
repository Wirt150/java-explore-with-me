package ru.practicum.ewm_service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm_service.config.client.BaseClient;
import ru.practicum.ewm_service.entity.model.server.Hit;

import javax.servlet.http.HttpServletRequest;

@Component
public class StatsServerClient extends BaseClient {
    private static final String APP_NAME = "ewm-main-service";

    @Autowired
    public StatsServerClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void createHit(HttpServletRequest request) {
        final Hit hit = Hit.builder()
                .app(APP_NAME)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build();
        post("/hit", hit);
    }
}
