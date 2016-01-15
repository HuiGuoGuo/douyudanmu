package com.learn.douyudanmu.action;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.jsp.JspWriter;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		URLConnection connection = null;
		try {
			URL url = new URL("https://youxinyi.cn");
			connection = url.openConnection();
			InputStream input = connection.getInputStream();
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096 * 4096];
			for (int n; (n = input.read(b)) != -1;) {
				buffer.append(new String(b, 0, n));
			}
			System.out.println(buffer.toString());
			OutputStream os = new FileOutputStream("d:/test.txt");
			PrintWriter out = new PrintWriter(os);
			out.flush();
			out.write(buffer.toString());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("出错啦" + e.getMessage());
		}

	}
}
