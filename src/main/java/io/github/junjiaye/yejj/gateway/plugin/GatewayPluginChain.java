package io.github.junjiaye.yejj.gateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: yejjgateway
 * @ClassName: GatewayPluginChain
 * @description:
 * @author: yejj
 * @create: 2024-06-06 07:05
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);

}
