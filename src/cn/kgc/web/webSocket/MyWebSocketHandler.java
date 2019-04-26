package cn.kgc.web.webSocket;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.kgc.pojo.Message;

@Component
public class MyWebSocketHandler implements WebSocketHandler {
	
	//当MyWebSocketHandler类被加载时就会创建该Map，随类而生
    public static final Map<String, WebSocketSession> userSocketSessionMap;
    
    static {
        userSocketSessionMap = new HashMap<String, WebSocketSession>();
    }

	//握手实现连接后
	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) 
			throws Exception {
		String uid = (String) webSocketSession.getHandshakeAttributes().get("uid");
        if (userSocketSessionMap.get(uid) == null) {
            userSocketSessionMap.put(uid, webSocketSession);
        }
	}

	//发送信息前的处理
	@Override
	public void handleMessage(WebSocketSession webSocketSession, 
			WebSocketMessage<?> webSocketMessage) throws Exception {
		
		 //if(webSocketMessage.getPayload().getPayloadLength()==0)return;

	        //得到Socket通道中的数据并转化为Message对象
	        Message msg=JSON.parseObject(webSocketMessage.getPayload().toString(),Message.class);

	        Timestamp now = new Timestamp(System.currentTimeMillis());
	        msg.setMessageDate(now);
	        //将信息保存至数据库
	        //youandmeService.addMessage(msg.getFromId(),msg.getFromName(),msg.getToId(),msg.getMessageText(),msg.getMessageDate());

	        //发送Socket信息
	        sendMessageToUser(msg.getToId(), new TextMessage(JSON.toJSONString(msg)));
		    
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

}
