package com.takeaway.gameofthree.storage;

import com.takeaway.gameofthree.model.Session;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameOfThreeSessionImpl implements GameOfThreeSession {
    private Map<String, Session> sessionStore = new ConcurrentHashMap<>();

    @Override
    public Session getSession(String id) {
        sessionStore.putIfAbsent(id, new Session(id));
        return sessionStore.get(id);
    }

    @Override
    public Session createNewSession() {
        Session session = new Session(UUID.randomUUID().toString());
        sessionStore.put(session.getId(), session);
        return session;
    }
}
