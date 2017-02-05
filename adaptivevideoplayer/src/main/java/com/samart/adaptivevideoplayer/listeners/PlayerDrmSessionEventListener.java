package com.samart.adaptivevideoplayer.listeners;


import android.os.Handler;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;

public class PlayerDrmSessionEventListener
	extends BaseHandlerEventListener
	implements DefaultDrmSessionManager.EventListener {
	
	public PlayerDrmSessionEventListener(final Handler messageHandler) {
		super(messageHandler);
	}
	
	@Override
	public void onDrmKeysLoaded() {
		
	}
	
	@Override
	public void onDrmSessionManagerError(final Exception e) {
		
	}
	
	@Override
	public void onDrmKeysRestored() {
		
	}
	
	@Override
	public void onDrmKeysRemoved() {
		
	}
}
