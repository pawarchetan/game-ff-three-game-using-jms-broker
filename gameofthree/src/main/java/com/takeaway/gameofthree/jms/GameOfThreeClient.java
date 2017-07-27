package com.takeaway.gameofthree.jms;

import com.takeaway.gameofthree.model.Move;

public interface GameOfThreeClient {

    void sendNextNumber(Move move);
}
