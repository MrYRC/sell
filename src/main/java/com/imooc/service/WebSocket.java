package com.imooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private  static CopyOnWriteArraySet<WebSocket> WebSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        WebSocketSet.add(this);
        log.info("【websocket消息】有新的连接, 总数:{}", WebSocketSet.size());
    }

    @OnClose
    public void onClose(){
        WebSocketSet.remove(this);
        log.info("【websocket消息】连接断开，总数：{}", WebSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("【websocket消息】收到客户端发来的消息：{}", message);
    }

    public void sendMessage(String message){
        for (WebSocket webSocket:WebSocketSet){
            log.info("【websocket消息】广播信息，message={}", message);

            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
