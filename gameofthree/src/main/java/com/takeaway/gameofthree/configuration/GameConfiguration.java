package com.takeaway.gameofthree.configuration;

import com.takeaway.gameofthree.GameOfThree;
import com.takeaway.gameofthree.jms.GameOfThreeClient;
import com.takeaway.gameofthree.jms.GameOfThreeClientImpl;
import com.takeaway.gameofthree.services.GameOfThreeService;
import com.takeaway.gameofthree.services.GameOfThreeServiceImpl;
import com.takeaway.gameofthree.storage.GameOfThreeSession;
import com.takeaway.gameofthree.storage.GameOfThreeSessionImpl;
import com.takeaway.gameofthree.util.UserNumberGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

@Configuration
public class GameConfiguration {

    @Bean
    public JmsTemplate jmsTemplate(MessageConverter messageConverter, ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }
}
