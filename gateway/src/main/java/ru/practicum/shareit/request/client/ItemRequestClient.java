package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    public static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String baseUrl, WebClient.Builder webClientBuilder) {
        super(webClientBuilder.baseUrl(baseUrl + API_PREFIX).build());
    }

    public Mono<ResponseEntity<String>> post(String path, Integer userId, String body) {
        return super.post(path, userId, null, body);
    }

    public Mono<ResponseEntity<String>> get(String path, Integer userId, Map<String, Object> params) {
        return super.get(path, userId, params);
    }
}
