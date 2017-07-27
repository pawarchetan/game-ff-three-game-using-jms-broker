package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.jms.GameOfThreeClient;
import com.takeaway.gameofthree.model.Move;
import com.takeaway.gameofthree.model.Session;
import com.takeaway.gameofthree.services.GameOfThreeService;
import com.takeaway.gameofthree.services.GameOfThreeServiceImpl;
import com.takeaway.gameofthree.storage.GameOfThreeSession;
import com.takeaway.gameofthree.storage.GameOfThreeSessionImpl;
import com.takeaway.gameofthree.util.UserNumberGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class GameOfThreeServiceImplTest {
    private GameOfThreeSession gameOfThreeSession;
    private GameOfThreeClient gameOfThreeClient;
    private GameOfThreeService gameOfThreeService;
    private InputStream stdin;

    @Before
    public void setup() {
        gameOfThreeSession = new GameOfThreeSessionImpl();
        gameOfThreeClient = mock(GameOfThreeClient.class);
        UserNumberGenerator userNumberGenerator = mock(UserNumberGenerator.class);
        gameOfThreeService = new GameOfThreeServiceImpl(gameOfThreeSession, gameOfThreeClient, userNumberGenerator);

        ReflectionTestUtils.setField(gameOfThreeService, "player", "p1");
        ReflectionTestUtils.setField(gameOfThreeService, "activePlayer", true);

        stdin = System.in;
    }

    @After
    public void tearDown() {
        reset(gameOfThreeClient);
        System.setIn(stdin);
    }

    @Test
    public void testStartGame() {
        // Test with auto numbering
        enterDataInSystemIn("n");

        gameOfThreeService.startGame();

        ArgumentCaptor<Move> startGameMoveArg = ArgumentCaptor.forClass(Move.class);
        verify(gameOfThreeClient).sendNextNumber(startGameMoveArg.capture());

        Move move = startGameMoveArg.getValue();
        Session session = gameOfThreeSession.getSession(move.getGameId());

        assertThat(move.getGameId(), not(nullValue()));
        assertThat(move.getNumber(), greaterThanOrEqualTo(10));
        assertThat(move.getNumber(), lessThanOrEqualTo(1000));
        assertThat(move.getPlayer(), equalTo("p1"));

        assertThat(session.getId(), equalTo(move.getGameId()));
        assertThat(session.getLastTurnActor(), equalTo("p1"));
    }

    @Test
    public void testNextNumber() {
        Session session = gameOfThreeSession.createNewSession();
        session.getMoves().add(90);
        session.setLastTurnActor("p1");

        gameOfThreeService.processMove(new Move(session.getId(), 30, "p2"));

        ArgumentCaptor<Move> responseGameMoveArg = ArgumentCaptor.forClass(Move.class);
        verify(gameOfThreeClient).sendNextNumber(responseGameMoveArg.capture());

        Move move = responseGameMoveArg.getValue();

        assertThat(move.getGameId(), equalTo(session.getId()));
        assertThat(move.getNumber(), equalTo(10));
        assertThat(move.getPlayer(), equalTo("p1"));

        assertThat(session.getLastTurnActor(), equalTo("p1"));
        assertThat(session.getMoves(), equalTo(Arrays.asList(90, 30, 10)));
    }

    @Test
    public void testNextNumberSameUserTwiceNotAllowed() {
        Session session = gameOfThreeSession.createNewSession();
        session.getMoves().add(90);
        session.setLastTurnActor("p2");

        verify(gameOfThreeClient, never()).sendNextNumber(Matchers.any(Move.class));

        assertThat(session.getLastTurnActor(), equalTo("p2"));
        assertThat(session.getMoves(), equalTo(Collections.singletonList(90)));
    }

    @Test
    public void testNextNumberInvalidNumberNotAllowed() {
        Session session = gameOfThreeSession.createNewSession();
        session.getMoves().add(90);
        session.setLastTurnActor("p1");

        gameOfThreeService.processMove(new Move(session.getId(), 20, "p2"));

        verify(gameOfThreeClient, never()).sendNextNumber(Matchers.any(Move.class));

        assertThat(session.getLastTurnActor(), equalTo("p1"));
        assertThat(session.getMoves(), equalTo(Collections.singletonList(90)));
    }

    private void enterDataInSystemIn(String data) {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    public void testNewGameRequest() {
        String newGameUuid = UUID.randomUUID().toString();
        gameOfThreeService.processMove(new Move(newGameUuid, 20, "p2"));

        Session session = gameOfThreeSession.getSession(newGameUuid);

        ArgumentCaptor<Move> responseGameMoveArg = ArgumentCaptor.forClass(Move.class);
        verify(gameOfThreeClient).sendNextNumber(responseGameMoveArg.capture());

        assertThat(session.getLastTurnActor(), equalTo("p1"));
        assertThat(session.getMoves(), equalTo(Arrays.asList(20, 7)));

        assertThat(responseGameMoveArg.getValue().getNumber(), equalTo(7));
        assertThat(responseGameMoveArg.getValue().getPlayer(), equalTo("p1"));
        assertThat(responseGameMoveArg.getValue().getGameId(), equalTo(newGameUuid));
    }

    @Test
    public void testGameLost() {
        GameOfThreeService gameOfThreeServiceSpy = spy(gameOfThreeService);
        doNothing().when(gameOfThreeServiceSpy).startGame();

        Session session = gameOfThreeSession.createNewSession();
        session.getMoves().addAll(Arrays.asList(9, 3));
        session.setLastTurnActor("p1");

        gameOfThreeServiceSpy.processMove(new Move(session.getId(), 1, "p2"));

        verify(gameOfThreeClient, never()).sendNextNumber(Matchers.any(Move.class));

        assertThat(session.getLastTurnActor(), equalTo("p2"));
        assertThat(session.getMoves(), equalTo(Arrays.asList(9, 3, 1)));
    }

    @Test
    public void testInvalidNumber() {
        Session session = gameOfThreeSession.createNewSession();
        session.getMoves().addAll(Collections.singletonList(9));
        session.setLastTurnActor("p1");

        gameOfThreeService.processMove(new Move(session.getId(), -1, "p2"));

        verify(gameOfThreeClient, never()).sendNextNumber(Matchers.any(Move.class));

        assertThat(session.getLastTurnActor(), equalTo("p1"));
        assertThat(session.getMoves(), equalTo(Collections.singletonList(9)));
    }

}
