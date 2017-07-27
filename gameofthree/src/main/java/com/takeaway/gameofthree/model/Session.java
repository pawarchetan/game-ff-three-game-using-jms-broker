package com.takeaway.gameofthree.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@ToString
public class Session {
    private String id;
    private List<Integer> moves;
    private String lastTurnActor;

    public Session(String id) {
        this.id = id;
        moves = Collections.synchronizedList(new ArrayList<>());
    }
}
