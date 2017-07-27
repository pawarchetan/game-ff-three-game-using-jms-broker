package com.takeaway.gameofthree.jms;

import com.takeaway.gameofthree.model.Move;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class GameOfThreeClientImpl implements GameOfThreeClient {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.send.to.queue}")
    private String destinationQueue;

    @Override
    public void sendNextNumber(Move move) {
        log.info("Player: " + move.getPlayer() + " - Number: " + move.getNumber() + " - game ID: " + move.getGameId(), move.getPlayer());
        jmsTemplate.convertAndSend(destinationQueue, move);
    }
}
