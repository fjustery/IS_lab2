package com.p3319.lab2.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsEvent<T> {
    private EventType type;
    private Object entityId;
    private T payload;
}
