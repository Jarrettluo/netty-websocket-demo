package com.example.demo.controller;

import com.example.demo.websocket.MyWebSocket;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 模拟向多用户推送消息的方法
 * @Author Jarrett Luo
 * @Date 2022/4/22 13:06
 * @Version 1.0
 */

@RestController
@RequestMapping("/doWebSocket")
public class WebSocketController {

    @PostMapping(value = "/pushMsg")
    public String pushMessage(@RequestBody String msg) throws IOException {
        MyWebSocket.sendInfoToClient(msg);
       return "push msg: "  + msg + "success!";
    }
}
