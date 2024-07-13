package io.github.junjiaye.yejj.gateway;

import cn.yejj.yejjrpc.core.registry.RegistryCenter;
import io.github.junjiaye.yejj.gateway.plugin.Gatewayplugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import cn.yejj.yejjrpc.core.registry.yejjregistry.YeJJRegistryCenter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * @program: yejjgateway
 * @ClassName: GatewayConfig
 * @description:
 * @author: yejj
 * @create: 2024-06-02 09:57
 */
@Configuration
public class GatewayConfig {
    @Bean
    public RegistryCenter rc(){
        return new YeJJRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context){
        return args -> {
            SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put(Gatewayplugin.GATEWAY_PREFIX + "/**","gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            System.out.println("yejjgateway start");
        };
    }
}
