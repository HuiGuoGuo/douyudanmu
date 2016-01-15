package com.learn.douyudanmu.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;

import com.learn.douyudanmu.dto.Message;

/**
 * 
 * @author Stone
 * @date 2016年1月15日
 * @forUse
 */
public class SendMessage {
	private OutputStream outputStream;
	private ByteArrayOutputStream byteArrayOutputStream;
	private Logger Log = Logger.getLogger(SendMessage.class);

	public SendMessage(OutputStream outputStream) {
		this.outputStream = outputStream;
		byteArrayOutputStream = new ByteArrayOutputStream(1000);
	}

	public void sendMessage(String context) throws IOException {
		Message message = new Message(context);
		putDataToStream(message.getLength());
		putDataToStream(message.getCode());
		putDataToStream(message.getMagic());
		byteArrayOutputStream.write(context.getBytes());
		putDataToStream(message.getEnd());

		outputStream.write(byteArrayOutputStream.toByteArray());
		byteArrayOutputStream.reset();
	}

	public void send(String roomId, String groupId) throws IOException, InterruptedException {
		Log.info("进入房间:" + roomId);
		Log.info("加入group:" + groupId);
		sendMessage("type@=loginreq/username@=auto_KRLJbE8mZM/password@=1234567890123456/roomid@=" + roomId + "/");
		sendMessage("type@=joingroup/rid@=" + roomId + "/gid@=" + groupId + "/");

		if (byteArrayOutputStream != null) {
			byteArrayOutputStream.close();
		}
	}

	public void sendKeepLive() throws IOException {
		sendMessage("type@=keeplive/tick@=70/");
	}

	/**
	 * 将数据放入缓冲流
	 */
	public void putDataToStream(int[] data) {
		if (data != null) {
			for (int i : data) {
				byteArrayOutputStream.write(i);
			}
		}
	}

}
