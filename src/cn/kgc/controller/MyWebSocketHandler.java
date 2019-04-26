package cn.kgc.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.kgc.pojo.Message;
import javafx.scene.control.Alert;

@Component
public class MyWebSocketHandler implements WebSocketHandler {
	
	//当MyWebSocketHandler类被加载时就会创建该Map，随类而生
    public static final Map<String, WebSocketSession> userSocketSessionMap;
    
    public static final List<String> users;
    
    static {
        userSocketSessionMap = new HashMap<String, WebSocketSession>();
        users = new ArrayList<>();
    }

	//握手实现连接后
	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) 
			throws Exception {
		String uid = (String) webSocketSession.getHandshakeAttributes().get("uid");
		Map<String, Object> mm = webSocketSession.getHandshakeAttributes();
        if (userSocketSessionMap.get(uid) == null) {
            userSocketSessionMap.put(uid, webSocketSession);
            users.add(uid);
            System.out.println(users.size() + "-------------------------");
            sendAll();
        }
	}

	//发送信息前的处理
	@Override
	public void handleMessage(WebSocketSession webSocketSession, 
			WebSocketMessage<?> webSocketMessage) throws Exception {
		
		 //if(webSocketMessage.getPayload().getPayloadLength()==0)return;

	        //得到Socket通道中的数据并转化为Message对象
	        Message msg=new Gson().fromJson(webSocketMessage.getPayload().toString(),Message.class);

	        Timestamp now = new Timestamp(System.currentTimeMillis());
	        msg.setMessageDate(now);
	        //将信息保存至数据库
	        //youandmeService.addMessage(msg.getFromId(),msg.getFromName(),msg.getToId(),msg.getMessageText(),msg.getMessageDate());

	        //发送Socket信息
	        sendMessageToUser(msg.getToId(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
		    
	}

	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus arg1)
			throws Exception {
		System.out.println("WebSocket:"+webSocketSession.getHandshakeAttributes().get("uid")+"close connection");
        Iterator<Map.Entry<String,WebSocketSession>> iterator = userSocketSessionMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,WebSocketSession> entry = iterator.next();
            if(entry.getValue().getHandshakeAttributes().get("uid")==webSocketSession.getHandshakeAttributes().get("uid")){
                userSocketSessionMap.remove(webSocketSession.getHandshakeAttributes().get("uid"));
                System.out.println("WebSocket in staticMap:" + webSocketSession.getHandshakeAttributes().get("uid") + "removed");
            }
        }
	}
	
	@Override
	public void handleTransportError(WebSocketSession arg0, Throwable arg1) throws Exception {
		
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	//发送信息的实现
    public void sendMessageToUser(String uid, TextMessage message)
            throws IOException {
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }
    
    public void sendAll() throws IOException {
    	for (String userName : users) {
    		WebSocketSession session = userSocketSessionMap.get(userName);
    		if (session != null && session.isOpen()) {
    			String content = "";
    			for (String u : users) {
    				content += u + "--";
				}
    			Message msg = new Message();
    			Timestamp now = new Timestamp(System.currentTimeMillis());
    	        msg.setMessageDate(now);
    	        msg.setMessageText("在线人数:" + users.size() + " 分别:" + content);
    	        msg.setToId(userName);
    			TextMessage tm = new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg));
                session.sendMessage(tm);
            }
		}
    }

}
