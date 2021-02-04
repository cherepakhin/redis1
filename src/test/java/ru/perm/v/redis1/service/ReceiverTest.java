package ru.perm.v.redis1.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverTest {

    @Test
    void receiveMessage() {
        Receiver receiver = new Receiver();
        receiver.receiveMessage("-");
        assertEquals(1,receiver.getCount());
    }
}