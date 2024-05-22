package service;

import model.Message;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MessageQueue {
    List<Message> messageList = new ArrayList<>();

    private static final Logger log = Logger.getLogger(MessageQueue.class.getName());
    List<Consumer> consumers = new ArrayList<>();

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    private int allowedRetries = 3;

    public boolean subscribe(Consumer currConsumer) {
        if (consumers == null || consumers.size() == 0) {
            this.consumers.add(currConsumer);
        } else {
            if (containsNoCircularDependency(currConsumer)) {
                this.consumers.add(currConsumer);
            } else {
                log.info("Failed to add as circular dependency exists");
                return false;
            }
        }
        return true;
    }

    public void addMsg(Message message) {
        messageList.add(message);
        notifyConsumers(); // ask consumers to read
    }

    void notifyConsumers() {
        List<Consumer> currentConsumers = createConsumersOrder();
        executorService.submit(() -> {
            Message message;
            synchronized (messageList) {
                message = messageList.remove(0);
            }
            log.info("Starting consuming msg = " + message);
            for (Consumer currentCW : currentConsumers) {
                try {
                    currentCW.setRetries(currentCW.getRetries() + 1);
                    currentCW.consume(message);
                } catch (InterruptedException e) {
                    if (currentCW.getRetries() > allowedRetries) {
                        log.info("Exhausted the retries for consumer = " + currentCW.getId());
                    } else {
                        log.info("retrying for consumer = " + currentCW.getId());
                    }
                }
            }
        });
    }

    public void shutDown() {
        executorService.shutdown();
    }

    private List<Consumer> createConsumersOrder() {
        Stack<Consumer> st = new Stack();
        Map<String, Boolean> isvisited = new HashMap<>();
        for (Consumer consumer : consumers) {
            if (isvisited.get(consumer.getId()) == null || isvisited.get(consumer.getId()) == false) {
                topologicalSort(consumer, st, isvisited);
            }
        }
        List<Consumer> consumerList = new ArrayList<>();

        while (!st.isEmpty()) {
            consumerList.add(0, st.pop());
        }
//        log.info("Ordering of consumers = " + Arrays.toString(consumerList.toArray()));
        return consumerList;
    }

    private void topologicalSort(Consumer consumer, Stack<Consumer> st, Map<String, Boolean> isvisited) {
        isvisited.put(consumer.getId(), true);

        for (Consumer dependentConsumers : consumer.getDependentConsumers()) {
            if (isvisited.get(dependentConsumers.getId()) == null || isvisited.get(dependentConsumers.getId()) == false) {
                topologicalSort(dependentConsumers, st, isvisited);
            }
        }
        st.push(consumer);
    }

    private boolean containsNoCircularDependency(Consumer currConsumer) {
        return true; // TODO
    }
}
