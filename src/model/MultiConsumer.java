package model;

import service.Consumer;

public class MultiConsumer implements Consumer {

    private final String id;

    Consumer[] dependentConsumers;

    int retries;

    public MultiConsumer(String id, Consumer... consumers) {
        this.id = id;
        this.dependentConsumers = consumers;
    }


    @Override
    public void consume(Message message) throws InterruptedException {
        System.out.println("Consumer = " + getId() + " Consuming msg = " + message.getMsg());
        Thread.sleep(1000);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Consumer[] getDependentConsumers() {
        return dependentConsumers;
    }

    @Override
    public void setDependentConsumers(Consumer[] dependentConsumers) {
        this.dependentConsumers = dependentConsumers;
    }

    @Override
    public int getRetries() {
        return this.retries;
    }

    @Override
    public void setRetries(int retries) {
        this.retries = retries;
    }

    @Override
    public String toString() {
        return getId();
    }
}
