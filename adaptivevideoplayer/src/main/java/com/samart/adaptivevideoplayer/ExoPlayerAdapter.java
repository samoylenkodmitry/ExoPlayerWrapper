package com.samart.adaptivevideoplayer;


import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ExoPlayerAdapter {
	
	private static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 1;
	private static final int DEFAULT_LIVE_PRESENTATION_DELAY_MS = 1000;
	private static final String DEFAULT_PLAYER_CACHE_SUBDIR_NAME = "adaptiveplayercache";
	private static final long DEFAULT_CACHE_SIZE_MAX_BYTES = 200 * 1024 * 1024;
	private static final String DEFAULT_USER_AGENT = "Android-Adaptiveplayer";
	private static DefaultBandwidthMeter sBandwidthMeter = null;
	private static SimpleCache sFileCache = null;
	private final Context mContext;
	private final HandlerThread mHandlerThread = new HandlerThread("exoplayeradapter");
	private String mUserAgent = DEFAULT_USER_AGENT;
	private Handler mMessageHandler;
	
	//settings
	private int mMinLoadableRetryCount = DEFAULT_MIN_LOADABLE_RETRY_COUNT;
	private long mLivePresentationDelayMs = DEFAULT_LIVE_PRESENTATION_DELAY_MS;
	private AdaptiveMediaSourceEventListener mAdaptiveFormatListener;
	private ExtractorMediaSource.EventListener mDefaultFormatListener;
	
	public ExoPlayerAdapter(final Context context) {
		mContext = context;
		
		mHandlerThread.start();
		mMessageHandler = new Handler(mHandlerThread.getLooper());
		
		if (sFileCache == null) {
			initFileCache(DEFAULT_CACHE_SIZE_MAX_BYTES, DEFAULT_PLAYER_CACHE_SUBDIR_NAME, context);
		}
		
		if (sBandwidthMeter == null) {
			sBandwidthMeter = new DefaultBandwidthMeter();
		}
	}
	
	public void setFileCacheSize(final int cacheSize) {
		initFileCache(cacheSize, DEFAULT_PLAYER_CACHE_SUBDIR_NAME, mContext);
	}
	
	private void createListeners() {
		mDefaultFormatListener = new ExtractorMediaSource.EventListener() {
			
			@Override
			public void onLoadError(final IOException error) {
				
			}
		};
		mAdaptiveFormatListener = new AdaptiveMediaSourceEventListener() {
			
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
		};
	}
	
	public void init() {
		try {
			mDrmSessionManager = buildDrmSessionManager(getVideoUrl().contentFormat, getAppVersion(), getSession(), getContentId(), getMediaFile());
		} catch (final UnsupportedDrmException e) {
		}
		
	}
	
	private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(
		final Format contentFormat, final int appVersion, final String session, final int contentId,
		final MediaFile mediaFile
	) throws UnsupportedDrmException {
		if (contentFormat==Format.DASH) {
			return DefaultDrmSessionManager.<FrameworkMediaCrypto>newWidevineInstance(
				new DashWidevineDrmCallback(appVersion, session, contentId, mediaFile),
				null,
				mMessageHandler, mListener);
			
		}
		
		return null;
	}
	
	private MediaSource buildMediaSource(final Format contentFormat, final Uri uri, final DataSource.Factory mediaDataSourceFactory, final String contentCacheKey) {
		
		createListeners();
		
		switch (contentFormat) {
			case DASH: {
				return new DashMediaSource(
					uri, new DefaultHttpDataSourceFactory(mUserAgent),
					//custom id for cache keys
					new DashManifestParser(contentCacheKey),
					new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
					mMinLoadableRetryCount, mLivePresentationDelayMs,
					mMessageHandler, mAdaptiveFormatListener);
			}
			case HLS: {
				return new HlsMediaSource(uri, mediaDataSourceFactory, mMessageHandler, mAdaptiveFormatListener);
			}
			case MP4: {
				return new ExtractorMediaSource(uri, mediaDataSourceFactory, Mp4Extractor.FACTORY, mMessageHandler, mDefaultFormatListener);
			}
			case UNDEFINED:
			default: {
				return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(), mMessageHandler, mDefaultFormatListener);
			}
		}
	}
	
	private static void initFileCache(final long defaultCacheSizeMaxBytes, final String defaultPlayerCacheSubdirName, final Context context) {
		sFileCache = new SimpleCache(new File(context.getCacheDir(), defaultPlayerCacheSubdirName), new LeastRecentlyUsedCacheEvictor(defaultCacheSizeMaxBytes));
	}
	
	private static class DashWidevineDrmCallback implements MediaDrmCallback {
		
		
		private static final byte[] ZERO_BYTES_ARRAY = new byte[0];
		private final DrmRequestProvider mDrmRequestProvider;
		
		private DashWidevineDrmCallback(final DrmRequestProvider drmRequestProvider) {
			mDrmRequestProvider = drmRequestProvider;
		}
		
		@Override
		public byte[] executeProvisionRequest(final UUID uuid, final ExoMediaDrm.ProvisionRequest request) throws Exception {
			return ZERO_BYTES_ARRAY;
		}
	interface DrmRequestProvider {
		String provideDrmServerUrl();
		void applyRequestHeaders(Uri.Builder builder);
	}
		@Override
		public byte[] executeKeyRequest(final UUID uuid, final ExoMediaDrm.KeyRequest request) throws Exception {
			final String url = request.getDefaultUrl();
			
			final Uri baseUri = Uri
				.parse(TextUtils.isEmpty(url) ? mDrmRequestProvider.provideDrmServerUrl() : url);
			
			final Uri.Builder uriBuilder = new Uri.Builder();
			
			uriBuilder
				.scheme(baseUri.getScheme())
				.authority(baseUri.getAuthority())
				.path(baseUri.getPath())
				.query(baseUri.getQuery());
			
			mDrmRequestProvider.applyRequestHeaders(uriBuilder);
			
			final int[] responseCodes = { 0 };
			final Exception[] exceptions = { null };
			
			final byte[] keyData = NetworkUtils.handleConnection(
				uriBuilder.build().toString(),
				NetworkUtils.REQUEST_METHOD_POST, NetworkUtils.CONTENT_TYPE_OCTET_STREAM,
				new KeyRequestOutputHandler(request),
				new ByteArrInputHandler(),
				new KeyRequestResponseHandler(responseCodes, exceptions)
			);
			
			if (exceptions[0] != null) {
				throw new UnsupportedDrmException(
					UnsupportedDrmException.REASON_INSTANTIATION_ERROR, exceptions[0]);
			}
			
			if (ArrayUtils.isEmpty(keyData)) {
				throw new UnsupportedDrmException(
					UnsupportedDrmException.REASON_INSTANTIATION_ERROR);
			}
			
			return keyData;
		}
		private static class KeyRequestOutputHandler implements NetworkUtils.OutputHandler {
			
			private final ExoMediaDrm.KeyRequest mRequest;
			
			private KeyRequestOutputHandler(final ExoMediaDrm.KeyRequest request) {
				mRequest = request;
			}
			
			@Override
			public byte[] getOutputBytes(final OutputStream output) throws IOException {
				return mRequest.getData();
			}
		}
		
		private static class ByteArrInputHandler implements NetworkUtils.InputHandler<byte[]> {
			
			@Override
			public byte[] handleInput(final InputStream input) throws IOException {
				return IoUtils.readBytes(input, false);
			}
		}
		
		private static class KeyRequestResponseHandler implements NetworkUtils.ResponseHandler {
			
			private final int[] mResponseCodes;
			private final Exception[] mExceptions;
			
			private KeyRequestResponseHandler(final int[] responseCodes, final Exception[] exceptions) {
				mResponseCodes = responseCodes;
				mExceptions = exceptions;
			}
			
			@Override
			public void handleResponseCode(final int responseCode) {
				mResponseCodes[0] = responseCode;
			}
			
			@Override
			public void handleException(final Exception exception) {
				mExceptions[0] = exception;
			}
		}
	}
}
