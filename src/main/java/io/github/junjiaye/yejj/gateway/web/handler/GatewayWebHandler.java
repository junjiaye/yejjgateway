package io.github.junjiaye.yejj.gateway.web.handler;

import io.github.junjiaye.yejj.gateway.plugin.DefaultGatewayPluginChain;
import io.github.junjiaye.yejj.gateway.plugin.Gatewayplugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: yejjgateway
 * @ClassName: GatewayWebHandler
 * @description:
 * @author: yejj
 * @create: 2024-06-02 16:34
 */
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    //spring 会自己找到实现Gatewayplugin接口的所有bean，放到集合中
    @Autowired
    List<Gatewayplugin> plugins;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println("====>> YeJJ Gateway web handler ...");
        if (plugins == null || plugins.isEmpty()){
            String mock = """
                    {
                        "result":"no plugins"
                    }
                    """;
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }
        return new DefaultGatewayPluginChain(plugins).handle(exchange);
//        for (Gatewayplugin plugin : plugins){
//            if(plugin.support(exchange)){
//                return plugin.handle(exchange);
//            }
//        }
//        String mock = """
//                    {
//                        "result":"no suppoted plugins"
//                    }
//                    """;
//        return exchange.getResponse()
//                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
    }
}
