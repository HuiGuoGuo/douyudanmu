package com.learn.douyudanmu.action;

import java.io.IOException;
import java.net.Socket;

import com.learn.douyudanmu.service.SendMessage;

/**
 * 
 * @author Stone
 * @date 2016年1月15日
 * @forUse 发送心跳包,防止server端中断连接
 */
public class KeepLiveThread implements Runnable {
	private Socket socket;

	public KeepLiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		while (socket != null && socket.isConnected()) {
			try {
				SendMessage sendMessage = new SendMessage(socket.getOutputStream());
				sendMessage.sendKeepLive();
				Thread.sleep(60000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
