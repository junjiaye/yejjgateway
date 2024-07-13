package io.github.junjiaye.yejj.gateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: yejjgateway
 * @ClassName: AbstractGatewayPlugin
 * @description:
 * @author: yejj
 * @create: 2024-06-03 07:17
 */
public abstract class AbstractGatewayPlugin implements Gatewayplugin {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


    @Override
    public Mono<Void> handle(ServerWebExchange exchange,GatewayPluginChain pluginChain) {
        boolean supported = support(exchange);
        System.out.println("======>>> plugin:" + this.getName() + ",support =" + supported);
        return supported ? doHandle(exchange,pluginChain) : pluginChain.handle(exchange);
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    public abstract Mono<Void> doHandle(ServerWebExchange exchange,GatewayPluginChain pluginChain);

    public abstract boolean doSupport(ServerWebExchange exchange);

}
