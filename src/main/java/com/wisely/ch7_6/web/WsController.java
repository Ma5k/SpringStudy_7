package com.wisely.ch7_6.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.wisely.ch7_6.domin.WiselyMessage;
import com.wisely.ch7_6.domin.WiselyResponse;

/**
 * 1、当浏览器向服务端发送请求时，通过@MessageMapping映射/welcome这个地址，类似于@RequestMapping。
 * 2、当服务端有消息时，会对订阅了@SendTo中的路径的浏览器发送消息。
 * @author Mask
 *
 */
@Controller
public class WsController {
	@MessageMapping("/welcome")	//1
	@SendTo("/topic/getResponse")	//2
	public WiselyResponse say(WiselyMessage message) throws Exception{
		Thread.sleep(3000);
		return new WiselyResponse("Welcome, " + message.getName() + "!");
	}
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;	//通过SimpMessagingTemplate向浏览器发送消息。
	
	@MessageMapping("/chat")
	public void handleChat(Principal principal, String msg) {	//在SpringMVC中，可以直接在参数中获得pricipal，principal中包含当前用户的信息。
		if (principal.getName().equals("mask")) {	//这里是一段硬编码，如果发送人是mask，则发送给wxw，如果发送人是wxw则发送给mask
			messagingTemplate.convertAndSendToUser("wxw", "/queue/notifications", principal.getName() + "-send:" + msg);	
		} else {
			//通过messagingTemplate.convertAndSendToUser向用户发送消息，第一个参数是接收消息的用户，第二个是浏览器订阅的地址，第三个是消息本身。
			messagingTemplate.convertAndSendToUser("mask", "/queue/notifications", principal.getName() + "-send:" + msg);	//
		}
	}
}
