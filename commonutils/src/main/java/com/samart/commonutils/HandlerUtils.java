package com.samart.commonutils;


import android.os.Handler;
import android.os.Message;

public final class HandlerUtils {
	
	public static void sendMsg(final Handler messageHandler, final int what) {
		sendMsg(messageHandler, what, null);
	}
	
	public static void sendMsg(final Handler messageHandler, final int what, final int arg1) {
		sendMsg(messageHandler, what, arg1, 0);
	}
	
	public static void sendMsg(final Handler messageHandler, final int what, final int arg1, final int arg2) {
		sendMsg(messageHandler, what, arg1, arg2, null);
	}
	
	public static void sendMsg(final Handler messageHandler, final int what, final Object obj) {
		sendMsg(messageHandler, what, 0, 0, obj);
	}
	
	public static void sendMsg(final Handler messageHandler, final int what, final int arg1, final int arg2, final Object obj) {
		if (messageHandler != null) {
			messageHandler.sendMessage(Message.obtain(messageHandler, what, arg1, arg2, obj));
		}
	}
}
