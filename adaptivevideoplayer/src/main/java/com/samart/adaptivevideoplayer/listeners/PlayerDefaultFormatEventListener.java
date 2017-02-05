package com.samart.adaptivevideoplayer.listeners;


import android.os.Handler;

import com.google.android.exoplayer2.source.ExtractorMediaSource;

import java.io.IOException;

public class PlayerDefaultFormatEventListener
	extends BaseHandlerEventListener
	implements ExtractorMediaSource.EventListener {
	
	public PlayerDefaultFormatEventListener(final Handler messageHandler) {
		super(messageHandler);
	}
	
	@Override
	public void onLoadError(final IOException error) {
		
	}
}
