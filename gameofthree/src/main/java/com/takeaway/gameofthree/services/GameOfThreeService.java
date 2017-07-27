package com.takeaway.gameofthree.services;

import com.takeaway.gameofthree.model.Move;

public interface GameOfThreeService {

    void processMove(Move move);

    void startGame();
}
