package model;

public class Message {
    final String pattern;
    final String msg;
    public Message(String pattern, String msg) {
        this.pattern = pattern;
        this.msg = msg;
    }

    public String getPattern() {
        return pattern;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
