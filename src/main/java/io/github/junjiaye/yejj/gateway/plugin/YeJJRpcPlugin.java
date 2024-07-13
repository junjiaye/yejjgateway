package io.github.junjiaye.yejj.gateway.plugin;

import cn.yejj.yejjrpc.core.api.LoaderBalancer;
import cn.yejj.yejjrpc.core.cluster.RoundLoadbalancer;
import cn.yejj.yejjrpc.core.meta.InstanceMeta;
import cn.yejj.yejjrpc.core.meta.ServiceMeta;
import cn.yejj.yejjrpc.core.registry.RegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: yejjgateway
 * @ClassName: YeJJRpcPlugin
 * @description:
 * @author: yejj
 * @create: 2024-06-03 07:23
 */
@Component("yejjrpc")
public class YeJJRpcPlugin extends AbstractGatewayPlugin{

    public static final String NAME = "yejjrpc";

    private String prefix = GATEWAY_PREFIX + "/" + NAME + "/";

    @Autowired
    RegistryCenter rc;

    LoaderBalancer<InstanceMeta> loaderBalancer = new RoundLoadbalancer<>();
    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange,GatewayPluginChain pluginChain) {
        //通过请求的路径，获取到请求的服务信息
        String service = exchange.getRequest().getPath().value().substring(prefix.length());
        ServiceMeta serviceMeta = ServiceMeta.builder().name(service).app("app1").env("dev").namespace("public").build();

        //获取到活着的注册中心实例
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        System.out.println(instanceMetas);

        InstanceMeta ins = loaderBalancer.choose(instanceMetas);
        String url = ins.toUrl();
        System.out.println(url);
        //获取请求报文
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();


        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        //通过entity获取响应报文
        Mono<String> body = entity.map(ResponseEntity::getBody);

        //这里不注释会报错，可能是被消费了，后面就获取不到了
        //body.subscribe(souce -> System.out.println("response:" + souce));
        //组装响应报文并返回
        exchange.getResponse().getHeaders().add("Content-Type","application/json");
        exchange.getResponse().getHeaders().add("yejj.ga.version","v2.0.0");
        exchange.getResponse().getHeaders().add("yejj.gw.plugin",getName());

        return body.flatMap(x -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(pluginChain.handle(exchange));
    }

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
