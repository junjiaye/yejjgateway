package io.github.junjiaye.yejj.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @program: yejjgateway
 * @ClassName: HelloHandler
 * @description:
 * @author: yejj
 * @create: 2024-05-30 07:16
 */
@Component
public class HelloHandler {
    Mono<ServerResponse> handle(ServerRequest request){
        String url = "http://localhst:8081/yejjrpc";
        String requestJson = """
                {
                    "service":
                    "methodSign":
                    "args":"[100]"
                }
                """;
        //TODO 有那种http处理框架，就会用那种，抽时间看看源码
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .bodyValue(requestJson).retrieve().toEntity(String.class);
        return  ServerResponse.ok().bodyValue(entity.map(ResponseEntity::getBody));
    }
}
