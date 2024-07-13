package io.github.junjiaye.yejj.gateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: yejjgateway
 * @ClassName: DefaultGatewayPluginChain
 * @description:
 * @author: yejj
 * @create: 2024-06-06 07:06
 */
public class DefaultGatewayPluginChain implements GatewayPluginChain {

    List<Gatewayplugin> plugins;

    int index = 0;

    public DefaultGatewayPluginChain(List<Gatewayplugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        return Mono.defer(()->{
            if (index < plugins.size()){
                plugins.get(index++).handle(exchange,this);
            }
            return Mono.empty();
        });

    }
}
