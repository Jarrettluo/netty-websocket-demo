
## 说明书

### 前言
本模块用于测试`netty-websocket-spring-boot-starter`
这是一个轻量级、高性能的WebSocket框架。该框架
使用Netty来开发WebSocket服务器
像spring-websocket的注解开发一样简单

### 案例
- 添加依赖

```
<dependency>
    <groupId>org.yeauty</groupId>
    <artifactId>netty-websocket-spring-boot-starter</artifactId>
    <version>0.12.0</version>
</dependency>
```

- 参考`websocket`
> 更多内容请参考：https://github.com/YeautyYE/netty-websocket-spring-boot-starter/blob/master/README_zh.md


### 测试方案
- websocket 在线调试工具可以使用
    - WebSocket在线测试工具 http://www.easyswoole.com/wstool.html
    - WebSocket 在线测试 v13 http://www.websocket-test.com/
    
- POSTMAN手动推送消息
   POST http://localhost:8080/doWebSocket/pushMsg