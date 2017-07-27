package com.takeaway.gameofthree.util;

import com.takeaway.gameofthree.model.Move;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class MoveConverter implements MessageConverter{

    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
        Move gameTurn = (Move) o;
        MapMessage message = session.createMapMessage();
        message.setString("gameId", gameTurn.getGameId());
        message.setInt("number", gameTurn.getNumber());
        message.setString("player", gameTurn.getPlayer());
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        MapMessage mapMessage = (MapMessage) message;
        return new Move(mapMessage.getString("gameId"), mapMessage.getInt("number"),
                mapMessage.getString("player"));
    }
}
