package com.takeaway.gameofthree.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Move {

    private String gameId;

    private int number;

    private String player;

}
