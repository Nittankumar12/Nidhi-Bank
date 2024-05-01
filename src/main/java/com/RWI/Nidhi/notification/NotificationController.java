package com.RWI.Nidhi.notification;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class NotificationController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Notification greeting(NotificationSender message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Notification("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
