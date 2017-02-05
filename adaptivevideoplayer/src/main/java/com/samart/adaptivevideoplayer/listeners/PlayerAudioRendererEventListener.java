package com.samart.adaptivevideoplayer.listeners;


import android.os.Handler;

import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;

public class PlayerAudioRendererEventListener
	extends BaseHandlerEventListener
	implements AudioRendererEventListener {
	
	
	public PlayerAudioRendererEventListener(final Handler messageHandler) {
		super(messageHandler);
	}
	
	@Override
	public void onAudioEnabled(final DecoderCounters counters) {
		
	}
	
	@Override
	public void onAudioSessionId(final int audioSessionId) {
		
	}
	
	@Override
	public void onAudioDecoderInitialized(final String decoderName, final long initializedTimestampMs, final long initializationDurationMs) {
		
	}
	
	@Override
	public void onAudioInputFormatChanged(final com.google.android.exoplayer2.Format format) {
		
	}
	
	@Override
	public void onAudioTrackUnderrun(final int bufferSize, final long bufferSizeMs, final long elapsedSinceLastFeedMs) {
		
	}
	
	@Override
	public void onAudioDisabled(final DecoderCounters counters) {
		
	}
}
