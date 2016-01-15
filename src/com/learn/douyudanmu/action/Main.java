package com.learn.douyudanmu.action;

import java.io.IOException;

import com.learn.douyudanmu.action.SayHello;

/**
 * 
 * @author Stone
 * @date 2016年1月14日
 * @forUse
 */
public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		new Thread(new SayHello()).start();
	}
}
