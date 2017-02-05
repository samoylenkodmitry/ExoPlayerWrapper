package com.samart.adaptivevideoplayer.listeners;


import android.os.Handler;

import com.samart.commonutils.HandlerUtils;

public abstract class BaseHandlerEventListener {
	
	private volatile Handler mMessageHandler;
	
	public BaseHandlerEventListener(final Handler handler) {
		mMessageHandler = handler;
	}
	
	public void release() {
		mMessageHandler = null;
	}
	
	public void sendMessage(final int what) {
		sendMessage(what, 0);
	}
	
	public void sendMessage(final int what, final int arg1) {
		sendMessage(what, arg1, 0);
	}
	
	public void sendMessage(final int what, final int arg1, final int arg2) {
		sendMessage(what, arg1, arg2, null);
	}
	
	public void sendMessage(final int what, final Object obj) {
		sendMessage(what, 0, 0, obj);
	}
	
	public void sendMessage(final int what, final int arg1, final int arg2, final Object obj) {
		
		HandlerUtils.sendMsg(mMessageHandler, what, arg1, arg2, obj);
	}
	
}
