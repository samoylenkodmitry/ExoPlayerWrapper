package com.samart.adaptivevideoplayer;


import android.net.Uri;
import android.text.TextUtils;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.samart.commonutils.ArrayUtils;
import com.samart.commonutils.IoUtils;
import com.samart.commonutils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class DashWidevineDrmCallback implements MediaDrmCallback {
	
	
	private static final byte[] ZERO_BYTES_ARRAY = new byte[0];
	interface DrmRequestProvider {
		
		String provideDrmServerUrl();
		
		void applyRequestHeaders(Uri.Builder builder);
	}
	private final DrmRequestProvider mDrmRequestProvider;
	
	DashWidevineDrmCallback(final DrmRequestProvider drmRequestProvider) {
		mDrmRequestProvider = drmRequestProvider;
	}
	
	@Override
	public byte[] executeProvisionRequest(final UUID uuid, final ExoMediaDrm.ProvisionRequest request) throws Exception {
		return ZERO_BYTES_ARRAY;
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
