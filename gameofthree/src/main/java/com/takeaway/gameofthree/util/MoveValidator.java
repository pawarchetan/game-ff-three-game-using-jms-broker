package com.takeaway.gameofthree.util;

import com.takeaway.gameofthree.model.Move;
import com.takeaway.gameofthree.model.Session;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class MoveValidator {
    public static boolean validateMove(Session session, Move move) {
        if (move.getPlayer() != null && move.getPlayer().equals(session.getLastTurnActor())) {
            log.error("ERROR: duplicate Move by player: " + move.getPlayer());
            return false;
        }

        if (move.getNumber() < 1) {
            log.info("The received " + move.getNumber() + " number in invalid");
        }
        List<Integer> moves = session.getMoves();
        if (!moves.isEmpty()) {
            Integer lastNumber = moves.get(session.getMoves().size() - 1);
            int expectedNumber = MoveUtil.calculateNextMove(lastNumber);
            if (expectedNumber != move.getNumber()) {
                log.info("The received " + move.getNumber() + " number in invalid. Expected number is " + expectedNumber);
                return false;
            }
        }
        return true;
    }
}
