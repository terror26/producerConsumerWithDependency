import model.Message;
import model.MultiConsumer;
import model.SingleProducer;
import service.Consumer;
import service.MessageQueue;
import service.Producer;

public class Main {
    public static void main(String[] args) {
        MessageQueue q1 = new MessageQueue();
        Consumer c1 = new MultiConsumer("c1");
        Consumer c2 = new MultiConsumer("c2",c1);
        Consumer c3 = new MultiConsumer("c3",c1,c2);

        q1.subscribe(c1); // consumer, then dependent consumersList
        q1.subscribe(c2); // consumer, then dependent consumersList
        q1.subscribe(c3); // consumer, then dependent consumersList
        Producer singleProducer = new SingleProducer(q1);

        singleProducer.produce(new Message("p1", "{\"value\" : \"hi first\"}"));
        singleProducer.produce(new Message("p2", "{\"value\" : \"hi second\"}"));
        singleProducer.produce(new Message("p3", "{\"value\" : \"hi third\"}"));
        singleProducer.produce(new Message("p4", "{\"value\" : \"hi fourth\"}"));

        q1.shutDown();
    }
}