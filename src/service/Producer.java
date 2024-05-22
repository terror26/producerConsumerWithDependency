package service;

import model.Message;

public interface Producer {

    void produce(Message message);
}
