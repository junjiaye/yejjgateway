package io.github.junjiaye.yejj.gateway.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @program: yejjgateway
 * @ClassName: GatewayPostWebFilter
 * @description:
 * @author: yejj
 * @create: 2024-06-02 18:14
 */
@Component
public class GatewayPostWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(s -> {
            System.out.println("====>>>>> finally");
            exchange.getAttributes().forEach((k,v) ->{
                System.out.println("key == "+k+"，value ==="+v);
            });
        });
    }
}
