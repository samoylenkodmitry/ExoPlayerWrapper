package com.samart.adaptivevideoplayer.listeners;


import android.os.Handler;

import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSpec;

import java.io.IOException;

public class PlayerAdaptiveMediaSourceEventListener
	extends BaseHandlerEventListener
	implements AdaptiveMediaSourceEventListener {
	
	public PlayerAdaptiveMediaSourceEventListener(final Handler messageHandler) {
		super(messageHandler);
	}
	
	@Override
	public void onLoadStarted(final DataSpec dataSpec, final int dataType, final int trackType, final com.google.android.exoplayer2.Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeMs, final long mediaEndTimeMs, final long elapsedRealtimeMs) {
		
	}
	
	@Override
	public void onLoadCompleted(final DataSpec dataSpec, final int dataType, final int trackType, final com.google.android.exoplayer2.Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeMs, final long mediaEndTimeMs, final long elapsedRealtimeMs, final long loadDurationMs, final long bytesLoaded) {
		
	}
	
	@Override
	public void onLoadCanceled(final DataSpec dataSpec, final int dataType, final int trackType, final com.google.android.exoplayer2.Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeMs, final long mediaEndTimeMs, final long elapsedRealtimeMs, final long loadDurationMs, final long bytesLoaded) {
		
	}
	
	@Override
	public void onLoadError(final DataSpec dataSpec, final int dataType, final int trackType, final com.google.android.exoplayer2.Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeMs, final long mediaEndTimeMs, final long elapsedRealtimeMs, final long loadDurationMs, final long bytesLoaded, final IOException error, final boolean wasCanceled) {
		
	}
	
	@Override
	public void onUpstreamDiscarded(final int trackType, final long mediaStartTimeMs, final long mediaEndTimeMs) {
		
	}
	
	@Override
	public void onDownstreamFormatChanged(final int trackType, final com.google.android.exoplayer2.Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaTimeMs) {
		
	}
}
