package com.takeaway.gameofthree;

import com.takeaway.gameofthree.model.Move;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GameOfThree.class)
public class IntegrationTest {
    private static final String TEST_REMOTE_PLAYER = "p1";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.listen.to.queue}")
    private String gameNodeListenDest;

    @Value("${jms.send.to.queue}")
    private String gameNodeSendDest;

    @Value("${game.player}")
    private String playerName;

    @Test
    public void testStartGame() throws JMSException {
        String uuid = UUID.randomUUID().toString();
        jmsTemplate.convertAndSend(gameNodeListenDest, new Move(uuid, 20, TEST_REMOTE_PLAYER));

        Move move = (Move) jmsTemplate.receiveAndConvert(gameNodeSendDest);
        assertThat(move.getNumber(), equalTo(7));
        assertThat(move.getGameId(), equalTo(uuid));
        assertThat(move.getPlayer(), equalTo(playerName));
    }

    @Test
    public void wholeGame() {
        String uuid = UUID.randomUUID().toString();

        jmsTemplate.convertAndSend(gameNodeListenDest, new Move(uuid, 56, TEST_REMOTE_PLAYER));
        Move move = (Move) jmsTemplate.receiveAndConvert(gameNodeSendDest);
        assertThat(move.getNumber(), equalTo(19));

        jmsTemplate.convertAndSend(gameNodeListenDest, new Move(uuid, 6, TEST_REMOTE_PLAYER));
        Move move1 = (Move) jmsTemplate.receiveAndConvert(gameNodeSendDest);
        assertThat(move1.getNumber(), equalTo(2));

    }

}
