package com.takeaway.gameofthree;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.hooks.SpringContextHook;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class GameOfThreeJmsBroker {
    @Value("${jms.broker.url}")
    private String jmsBrokerUrl;

    @Value("${jms.broker.name}")
    private String name;

    @Value("${jms.broker.username}")
    private String username;

    @Value("${jms.broker.password}")
    private String password;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService brokerService() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setBrokerName(name);
        broker.addConnector(jmsBrokerUrl);
        broker.setPersistent(false);

        final SimpleAuthenticationPlugin authenticationPlugin = new SimpleAuthenticationPlugin();
        authenticationPlugin.setAnonymousAccessAllowed(false);
        authenticationPlugin.setUsers(Collections.singletonList(new AuthenticationUser(username, password, "")));
        broker.setPlugins(new BrokerPlugin[]{authenticationPlugin});

        broker.addShutdownHook(new SpringContextHook());

        return broker;
    }

    public static void main(String[] args) {
        SpringApplication.run(GameOfThreeJmsBroker.class, args);
    }

}
