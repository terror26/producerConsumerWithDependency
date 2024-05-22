package service;

import model.Message;

public interface Consumer {

    void consume(Message message) throws InterruptedException;
    String getId();

    Consumer[] getDependentConsumers();

    void setDependentConsumers(Consumer[] dependentConsumers);

    int getRetries();

    void setRetries(int retries);
}
