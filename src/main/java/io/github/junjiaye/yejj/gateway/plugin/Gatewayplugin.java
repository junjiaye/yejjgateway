package io.github.junjiaye.yejj.gateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: yejjgateway
 * @ClassName: Gatewayplugin
 * @description:
 * @author: yejj
 * @create: 2024-06-03 07:15
 */
public interface Gatewayplugin {

    String GATEWAY_PREFIX = "/gw";

    void start();
    void stop();

    String getName();
    boolean support(ServerWebExchange exchange);
    Mono<Void> handle(ServerWebExchange exchange,GatewayPluginChain pluginChain);
}
