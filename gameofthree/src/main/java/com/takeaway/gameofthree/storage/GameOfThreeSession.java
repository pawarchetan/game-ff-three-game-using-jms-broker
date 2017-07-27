package com.takeaway.gameofthree.storage;

import com.takeaway.gameofthree.model.Session;

public interface GameOfThreeSession {

    Session getSession(String id);

    Session createNewSession();
}
