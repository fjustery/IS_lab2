package com.p3319.lab2.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.p3319.lab2.dto.event.EventType;
import com.p3319.lab2.dto.event.WsEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public <T> void sendProductEvent(EventType type, Object entityId, T payload) {
        messagingTemplate.convertAndSend("/topic/products", new WsEvent<>(type, entityId, payload));
    }

    public <T> void sendOrganizationEvent(EventType type, Object entityId, T payload) {
        messagingTemplate.convertAndSend("/topic/organizations", new WsEvent<>(type, entityId, payload));
    }

    public <T> void sendPersonEvent(EventType type, Object entityId, T payload) {
        messagingTemplate.convertAndSend("/topic/persons", new WsEvent<>(type, entityId, payload));
    }
}
