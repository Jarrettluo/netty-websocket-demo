package com.example.demo.websocket;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author Jarrett Luo
 * @Date 2022/4/22 11:00
 * @Version 1.0
 */
@ServerEndpoint(path = "/ws/{uid}")
public class MyWebSocket {

    /**
     * 静态变量，用来记录当前在线连接数
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放么个客户端对应的MyWebSocket对象
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话， 需要通过它来给客户端发送数据
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private Session session;

    /**
     * 标示连接用户的唯一信息
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private String uid;

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req,
                          @RequestParam MultiValueMap reqMap, @PathVariable String uid, @PathVariable Map pathMap){
        System.out.println(session.toString());
        session.setSubprotocols("stomp");
        if (!"ok".equals(req)){
            System.out.println("Authentication failed!");
            session.close();
        }
        // TODO 未实现避免同一个客户端连接用户的方法，前端避免重复连接
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req,
                       @RequestParam MultiValueMap reqMap, @PathVariable String uid, @PathVariable Map pathMap){
        System.out.println("new connection");
        System.out.println(req);
        // 添加对象到set中
        webSocketSet.add(this);
        this.session = session;
        // 指定的用户信息赋给uid信息
        this.uid =  uid;
        // 在线用户增加
        addOnlineCount();
        System.out.println("当前在线用户数量： " + getOnlineCount() + " | " + webSocketSet.size());
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // 从对象集合中删除该连接对象
        webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        session.sendText("Hello Netty! 当前在线用户数量： " + getOnlineCount() + " | " + webSocketSet.size());
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.sendText(message);
    }

    /**
     * 群发消息到客户端， 此处是发送给连接的所有用户。
     * 可以实现根据UID发送给指定的用户
     * @param message
     * @throws IOException
     */
    public static void sendInfoToClient(String message) throws IOException {
        for (MyWebSocket myWebSocket : webSocketSet) {
            myWebSocket.sendMessage(message);
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount ++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount --;
    }

}