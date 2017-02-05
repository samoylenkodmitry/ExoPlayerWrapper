package com.samart.adaptivevideoplayer.listeners;


import android.os.Handler;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

public class PlayerEventListener
	extends BaseHandlerEventListener
	implements ExoPlayer.EventListener {
	
	public PlayerEventListener(final Handler messageHandler) {
		super(messageHandler);
	}
	
	@Override
	public void onTimelineChanged(final Timeline timeline, final Object manifest) {
		
	}
	
	@Override
	public void onTracksChanged(final TrackGroupArray trackGroups, final TrackSelectionArray trackSelections) {
		
	}
	
	@Override
	public void onLoadingChanged(final boolean isLoading) {
		
	}
	
	@Override
	public void onPlayerStateChanged(final boolean playWhenReady, final int playbackState) {
		
	}
	
	@Override
	public void onPlayerError(final ExoPlaybackException error) {
		
	}
	
	@Override
	public void onPositionDiscontinuity() {
		
	}
}
