package com.gfes.event;

import com.gfes.entity.Log;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.core.ResolvableType;

public class LogEvent extends PayloadApplicationEvent<Log> {

    public LogEvent(Object source, Log payload) {
        super(source, payload);
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forRawClass(LogEvent.class);
    }
}
