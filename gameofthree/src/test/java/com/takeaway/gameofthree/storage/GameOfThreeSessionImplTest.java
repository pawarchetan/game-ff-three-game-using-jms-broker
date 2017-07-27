package com.takeaway.gameofthree.storage;


import com.takeaway.gameofthree.model.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GameOfThreeSessionImplTest {

    private GameOfThreeSession gameOfThreeSession = new GameOfThreeSessionImpl();

    @Test
    public void testCreateManualGameSession() {
        Session emptySession = gameOfThreeSession.createNewSession();
        assertThat(emptySession.getMoves(), is(emptyIterable()));
        assertThat(emptySession.getLastTurnActor(), is(nullValue()));
        assertThat(emptySession.getId(), not(isEmptyOrNullString()));
    }

    @Test
    public void testGetGameSession() {
        Session emptySession = gameOfThreeSession.createNewSession();

        emptySession.setLastTurnActor("p1");
        List<Integer> moves = emptySession.getMoves();
        moves.addAll(Arrays.asList(28, 9, 3));

        Session session = gameOfThreeSession.getSession(emptySession.getId());
        assertThat(session.getMoves(), is(Arrays.asList(28, 9, 3)));
        assertThat(session.getLastTurnActor(), is("p1"));
        assertThat(session.getId(), is(emptySession.getId()));
    }

    @Test
    public void testGetNotExistingGameSession() {
        Session session = gameOfThreeSession.getSession(UUID.randomUUID().toString());
        assertThat(session.getMoves(), is(emptyIterable()));
        assertThat(session.getLastTurnActor(), is(nullValue()));
        assertThat(session.getId(), not(isEmptyOrNullString()));
    }
}
