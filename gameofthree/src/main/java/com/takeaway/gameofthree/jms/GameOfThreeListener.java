package com.takeaway.gameofthree.jms;

import com.takeaway.gameofthree.model.Move;
import com.takeaway.gameofthree.services.GameOfThreeService;
import com.takeaway.gameofthree.util.MoveConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
@Log4j2
public class GameOfThreeListener {

    private MoveConverter moveConverter;

    private GameOfThreeService gameOfThreeService;

    @Autowired
    public GameOfThreeListener(MoveConverter moveConverter, GameOfThreeService gameOfThreeService) {
        this.moveConverter = moveConverter;
        this.gameOfThreeService = gameOfThreeService;
    }

    @JmsListener(destination = "${jms.listen.to.queue}")
    public void receiveMessage(Message message) throws JMSException {
        final Move move = (Move) moveConverter.fromMessage(message);
        log.info("Player: " + move.getPlayer() + " - Number: " + move.getNumber() + " - game ID: " + move.getGameId());
        new Thread(() -> gameOfThreeService.processMove(move)).start();
    }
}
