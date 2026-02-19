package com.bolingcavalry.helloworld.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    
    @GetMapping("/delay/{ms}")
    public String delay(@PathVariable int ms) throws InterruptedException {
        // 这里打印的信息可以帮我们确认当前是不是虚拟线程模式
        log.info("当前线程名: {}", Thread.currentThread().getName());
        Thread.sleep(ms);
        return String.format("Delayed for %dms at %s", ms, new Date());
    }
}
