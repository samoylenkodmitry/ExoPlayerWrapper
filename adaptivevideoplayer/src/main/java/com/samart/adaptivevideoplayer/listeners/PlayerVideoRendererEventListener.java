package com.samart.adaptivevideoplayer.listeners;


import android.os.Handler;
import android.view.Surface;

import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

public class PlayerVideoRendererEventListener
	extends BaseHandlerEventListener
	implements VideoRendererEventListener {
	
	public PlayerVideoRendererEventListener(final Handler messageHandler) {
		super(messageHandler);
	}
	
	@Override
	public void onVideoEnabled(final DecoderCounters counters) {
		
	}
	
	@Override
	public void onVideoDecoderInitialized(final String decoderName, final long initializedTimestampMs, final long initializationDurationMs) {
		
	}
	
	@Override
	public void onVideoInputFormatChanged(final com.google.android.exoplayer2.Format format) {
		
	}
	
	@Override
	public void onDroppedFrames(final int count, final long elapsedMs) {
		
	}
	
	@Override
	public void onVideoSizeChanged(final int width, final int height, final int unappliedRotationDegrees, final float pixelWidthHeightRatio) {
		
	}
	
	@Override
	public void onRenderedFirstFrame(final Surface surface) {
		
	}
	
	@Override
	public void onVideoDisabled(final DecoderCounters counters) {
		
	}
}
