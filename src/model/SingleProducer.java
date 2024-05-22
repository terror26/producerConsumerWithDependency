package model;

import service.MessageQueue;
import service.Producer;

public class SingleProducer implements Producer {
    private final MessageQueue queue;

    public SingleProducer(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public void produce(Message message) {
        queue.addMsg(message);
    }
}
