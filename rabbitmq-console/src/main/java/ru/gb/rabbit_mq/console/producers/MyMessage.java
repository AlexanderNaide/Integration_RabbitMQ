package ru.gb.rabbit_mq.console.producers;

import java.io.Serial;
import java.io.Serializable;

public class MyMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -1650136059587331366L;

    private final String msg;

    public String getMsg() {
        return msg;
    }

    public MyMessage(String msg) {
        this.msg = msg;
    }
}
