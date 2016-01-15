package com.learn.douyudanmu.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.learn.douyudanmu.service.SendMessage;

/**
 * 
 * @author Stone
 * @date 2016年1月14日
 * @forUse
 */
public class SayHello implements Runnable {
	private String DANMU_IP;
	private int DANMU_PORT;
	private String ROOM_ID;
	private final String GROUT_ID = "4";

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private boolean conn;
	private Logger Log = Logger.getLogger(SayHello.class);

	private ResourceBundle rs = ResourceBundle.getBundle("conf");

	// 构造方法，初始化
	public SayHello() throws UnknownHostException, IOException {
		super();
		// TODO Auto-generated constructor stub
		DANMU_IP = rs.getString("server_address");
		DANMU_PORT = Integer.parseInt(rs.getString("server_port"));
		ROOM_ID = rs.getString("room_id");
		socket = new Socket(DANMU_IP, DANMU_PORT);
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
		conn = true;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SendMessage sendMessage = new SendMessage(outputStream);
		while (conn) {
			try {
				sendMessage.send(ROOM_ID, GROUT_ID);
				new Thread(new KeepLiveThread(socket)).start();
				receive(inputStream);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				Log.error("", e);
			} finally {
				closeConnection();
			}

		}
	}

	public void receive(InputStream inputStream) throws IOException {
		int i;
		byte[] bytes = new byte[1024];
		while (socket != null && socket.isConnected() && (i = inputStream.read(bytes)) != -1) {
			parseResponse(new String(bytes, 0, i));
		}

	}

	/**
	 * 只解析弹幕，没有解析礼物.
	 */
	public void parseResponse(String response) {
		String REGEX = "type@=chatmessage/.*/content@=(.*)/snick@=(.*?)/";
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(response);
		if (matcher.find()) {
			Log.info(matcher.group(2) + ": " + matcher.group(1));
		}
	}

	public void closeConnection() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
