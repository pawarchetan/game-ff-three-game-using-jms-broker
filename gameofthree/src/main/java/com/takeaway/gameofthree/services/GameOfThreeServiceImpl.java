package com.takeaway.gameofthree.services;

import com.takeaway.gameofthree.jms.GameOfThreeClient;
import com.takeaway.gameofthree.model.Move;
import com.takeaway.gameofthree.model.Session;
import com.takeaway.gameofthree.storage.GameOfThreeSession;
import com.takeaway.gameofthree.util.MoveValidator;
import com.takeaway.gameofthree.util.NumberGenerator;
import com.takeaway.gameofthree.util.UserNumberGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class GameOfThreeServiceImpl implements GameOfThreeService {

    @Value("${game.player}")
    private String player;

    @Value("${game.active}")
    private boolean activePlayer;

    private GameOfThreeSession gameOfThreeSession;
    private GameOfThreeClient gameOfThreeClient;
    private UserNumberGenerator userNumberGenerator;

    @Autowired
    public GameOfThreeServiceImpl(GameOfThreeSession gameOfThreeSession, GameOfThreeClient gameOfThreeClient,
                                  UserNumberGenerator userNumberGenerator) {
        this.gameOfThreeSession = gameOfThreeSession;
        this.gameOfThreeClient = gameOfThreeClient;
        this.userNumberGenerator = userNumberGenerator;
    }

    @Override
    public void processMove(Move move) {
        Session session = gameOfThreeSession.getSession(move.getGameId());

        if (!MoveValidator.validateMove(session, move)) {
            log.info("Invalid Move.");
            return;
        }

        session.getMoves().add(move.getNumber());
        session.setLastTurnActor(move.getPlayer());

        if (move.getNumber() == 1) {
            log.info("You lost!!!!!!!");
            startGame();
            return;
        }

        NumberGenerator numberGenerator = new UserNumberGenerator();
        int responseNumber = numberGenerator.getNextNumber(move.getNumber());
        session.getMoves().add(responseNumber);
        session.setLastTurnActor(player);

        gameOfThreeClient.sendNextNumber(new Move(session.getId(), responseNumber, player));

        if (responseNumber == 1) {
            log.info("You won!!!!!!!!");
            startGame();
        }
    }

    @Override
    public void startGame() {
        if (!activePlayer) {
            return;
        }

        int initialNumber = userNumberGenerator.getInitialNumber();

        Session session = gameOfThreeSession.createNewSession();
        session.getMoves().add(initialNumber);
        session.setLastTurnActor(player);

        log.info("New Game started with Session ID : " + session.getId() + " !!!!!!!!!!!");
        gameOfThreeClient.sendNextNumber(new Move(session.getId(), initialNumber, player));
    }

}
