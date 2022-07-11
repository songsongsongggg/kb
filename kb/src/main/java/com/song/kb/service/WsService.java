package com.song.kb.service;

import com.song.kb.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

    @Resource
    public WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message) {
//        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(message );
    }
}
