package io.github.junjiaye.yejj.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @program: yejjgateway
 * @ClassName: GatewayRouter
 * @description:
 * @author: yejj
 * @create: 2024-05-30 07:07
 */
@Component
public class GatewayRouter {
    @Autowired
    HelloHandler helloHandler;
    @Autowired
    GatewayHandler gatewayHandler;
    @Bean
    public RouterFunction<?> helloRooterFunction(){
        return route(GET("hello"),helloHandler::handle);
    }

    @Bean
    public RouterFunction<?> gatewayRooterFunction(){
        return route(GET("gw"),gatewayHandler::handle);
    }
}
