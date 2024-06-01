package io.github.junjiaye.yejj.gateway;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @program: yejjgateway
 * @ClassName: GatewayHandler
 * @description:
 * @author: yejj
 * @create: 2024-05-30 07:22
 */
@Component
public class GatewayHandler {
    Mono<ServerResponse> handle(ServerRequest request){
        return  ServerResponse.ok().body(Mono.just("hellt gatewayhandler"),String.class);
    }
}
