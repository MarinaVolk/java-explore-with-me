package ru.practicum;

import lombok.extern.slf4j.Slf4j;
/*import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * File Name: ru.practicum.StatsClient.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   9:06 PM (UTC+3)
 * Description:
 */

@Slf4j
@Service
public class StatsClient extends BaseClient {


    @Autowired
    public StatsClient(@Value("http://stats-server:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void saveStats(String app, String uri, String ip, LocalDateTime timestamp) {
        final HitDto hitDto = new HitDto(app, uri, ip, timestamp);
        log.info("Sending POST to /hit");
        post("/hit", hitDto);
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(String start, String end, String uris, Boolean uniq) {
        return getStats("/stats" + "?start=" + start + "&end=" + end + "&uris=" + uris + "&unique=" + uniq);
    }

}


//@Service
//@Slf4j
//@PropertySource(value = {"classpath:application.properties"})
//public class StatsClient {
    //@Value("http://localhost:9090"/*"${stats.server.url}"*/)
    /*private String baseUrl;

    private final WebClient client;

    public StatsClient() {
        this.client = WebClient.create(baseUrl);
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(String start, String end, String[] uris, Boolean isUnique) {
        log.info("получена статистика за период с {}, по {}, uris {}, unique {}", start, end, uris, isUnique);
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(/*baseUrl + "/stats" */ /*"http://localhost:9090/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", isUnique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .block();
    }

    public void saveStats(String app, String uri, String ip, LocalDateTime timestamp) {
        final HitDto hitDto = new HitDto(app, uri, ip, timestamp);
        log.info("Статистика сохранена {}", hitDto);
        this.client.post()
                .uri("http://localhost:9090/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(hitDto), HitDto.class)
                .retrieve()
                .toBodilessEntity()
                //.bodyToMono(HitDto.class)
                .block();
    }

} */
