package com.kqp.terminus.data.event;

import com.kqp.terminus.data.message.MessageTemplater;
import com.kqp.terminus.util.Broadcaster;
import net.minecraft.util.Formatting;

public class MessageEvent extends Event {
    private final String message;
    private final Formatting color;
    private final boolean bold;
    private final boolean italic;

    public MessageEvent(String message, Formatting color, boolean bold, boolean italic) {
        this.message = message;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
    }

    @Override
    public void invoke() {
        Broadcaster broadcaster = new Broadcaster();
        broadcaster.broadcastMessage(MessageTemplater.templateString(message), color, bold, italic);
    }
}