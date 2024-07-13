package io.github.junjiaye.yejj.gateway.plugin;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @program: yejjgateway
 * @ClassName: DirectPlugin
 * @description:
 * @author: yejj
 * @create: 2024-06-03 07:27
 */
@Component("direct")
public class DirectPlugin extends AbstractGatewayPlugin{

    public static final String NAME = "direct";

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange,GatewayPluginChain pluginChain) {
        String backend = exchange.getRequest().getQueryParams().getFirst("backend");
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();
        //组装响应报文头
        exchange.getResponse().getHeaders().add("Content-Type","application/json");
        exchange.getResponse().getHeaders().add("yejj.gw.version","v3.0.0");
        exchange.getResponse().getHeaders().add("yejj.gw.plugin",getName());

        if(backend == null || backend.isEmpty()){
            return requestBody.flatMap(x -> exchange.getResponse().writeWith(Mono.just(x)))
                    .then(pluginChain.handle(exchange));
        }

        WebClient client = WebClient.create(backend);
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        //通过entity获取响应报文
        Mono<String> body = entity.map(ResponseEntity::getBody);


        return body.flatMap(x -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(pluginChain.handle(exchange));
    }

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        String prefix = GATEWAY_PREFIX + "/" + NAME + "/";
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
