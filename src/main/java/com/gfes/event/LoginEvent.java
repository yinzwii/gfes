package com.gfes.event;

import com.gfes.entity.User;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.core.ResolvableType;

public class LoginEvent extends PayloadApplicationEvent<User> {
    public LoginEvent(Object source, User payload) {
        super(source, payload);
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forRawClass(LoginEvent.class);
    }
}
