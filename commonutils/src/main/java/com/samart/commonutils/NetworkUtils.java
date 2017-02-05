package com.samart.commonutils;


import android.text.TextUtils;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtils {
	
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String REQUEST_METHOD_POST = "POST";
	public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
	
	public interface Handler {
		
	}
	
	public interface ConnectionHandler<T> extends Handler {
		
		T handleConnection(final HttpURLConnection connection) throws IOException;
	}
	
	public interface InputHandler<T> extends Handler {
		
		T handleInput(final InputStream input) throws IOException;
	}
	
	public interface OutputHandler extends Handler {
		
		byte[] getOutputBytes(final OutputStream output) throws IOException;
	}
	
	public interface ResponseHandler extends Handler {
		
		void handleResponseCode(final int responseCode);
		
		void handleException(final Exception exception);
	}
	
	public static <T> T handleConnection(
		final String urlString, final String requestMethod, final String contentType,
		final OutputHandler outputHandler,
		final ConnectionHandler<T> connectionHandler,
		final ResponseHandler responseHandler) {
		return handleConnection(
			urlString, requestMethod, contentType,
			outputHandler, connectionHandler, null, responseHandler);
	}
	
	public static <T> T handleConnection(
		final String urlString, final String requestMethod, final String contentType,
		final OutputHandler outputHandler,
		final InputHandler<T> inputHandler,
		final ResponseHandler responseHandler) {
		return handleConnection(
			urlString, requestMethod, contentType,
			outputHandler, null, inputHandler, responseHandler);
	}
	
	public static <T> T handleConnection(
		final String urlString,
		final OutputHandler outputHandler,
		final ConnectionHandler<T> connectionHandler,
		final ResponseHandler responseHandler) {
		return handleConnection(
			urlString, REQUEST_METHOD_GET, null,
			outputHandler, connectionHandler, null, responseHandler);
	}
	
	public static <T> T handleConnection(
		final String urlString,
		final OutputHandler outputHandler,
		final InputHandler<T> inputHandler,
		final ResponseHandler responseHandler) {
		return handleConnection(
			urlString, REQUEST_METHOD_GET, null,
			outputHandler, null, inputHandler, responseHandler);
	}
	
	public static <T> T handleConnection(final String urlString, final OutputHandler outputHandler) {
		return handleConnection(urlString, REQUEST_METHOD_POST, null, outputHandler, null, null, null);
	}
	
	public static <T> T handleConnection(final String urlString, final ConnectionHandler<T> connectionHandler) {
		return handleConnection(urlString, REQUEST_METHOD_GET, null, null, connectionHandler, null, null);
	}
	
	public static <T> T handleConnection(final String urlString, final InputHandler<T> inputHandler) {
		return handleConnection(urlString, REQUEST_METHOD_GET, null, null, null, inputHandler, null);
	}
	
	public static void handleConnection(final String urlString, final ResponseHandler responseHandler) {
		handleConnection(urlString, REQUEST_METHOD_GET, null, null, null, null, responseHandler);
	}
	
	public static int getUrlResponseCode(final String urlString) {
		return resolveUrl(urlString, false).first;
	}
	
	public static Pair<Integer, String> resolveUrl(final String urlString, final boolean followRedirects) {
		final int responseCode;
		
		HttpURLConnection connection = null;
		
		try {
			connection = (HttpURLConnection) new URL(urlString).openConnection();
			
			connection.connect();
			
			responseCode = connection.getResponseCode();
			
			if (followRedirects
				&& (responseCode == HttpURLConnection.HTTP_MOVED_PERM
				|| responseCode == HttpURLConnection.HTTP_MOVED_TEMP)) {
				final String redirectedUrl = connection.getHeaderField("Location");
				
				connection.disconnect();
				
				connection = null;
				
				if (!TextUtils.equals(redirectedUrl, urlString)) {
					final Pair<Integer, String> resolvedUrl = resolveUrl(redirectedUrl, true);
					
					if (!TextUtils.isEmpty(resolvedUrl.second)) {
						return resolvedUrl;
					}
				}
			}
		} catch (final IOException e) {
			L.e(e);
			
			return new Pair<>(-1, urlString);
		} finally {
			
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return new Pair<>(responseCode, urlString);
	}
	
	public static boolean isUrlValid(final String urlString) {
		try {
			new URL(urlString);
		} catch (final MalformedURLException e) {
			return false;
		}
		
		return true;
	}
	
	public static class HttpErrorException extends IOException {
		
		public final int ResponseCode;
		
		public HttpErrorException(final int responseCode) {
			super();
			
			ResponseCode = responseCode;
		}
		
		public HttpErrorException(final int responseCode, final String detailMessage) {
			super(detailMessage);
			
			ResponseCode = responseCode;
		}
		
		public String toString() {
			return "HTTP response = " + ResponseCode + ", " + super.toString();
		}
	}
	
	private static <T> T handleConnection(
		final String urlString, final String requestMethod, final String contentType,
		final OutputHandler outputHandler,
		final ConnectionHandler<T> connectionHandler, final InputHandler<T> inputHandler,
		final ResponseHandler responseHandler) {
		if (TextUtils.isEmpty(urlString)
			|| (connectionHandler == null && outputHandler == null && inputHandler == null && responseHandler == null)) {
			return null;
		}
		
		final URL url;
		
		try {
			url = new URL(urlString);
		} catch (final MalformedURLException e) {
			L.e(e);
			
			return null;
		}
		
		try {
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(outputHandler != null);
			connection.setDoInput(inputHandler != null);
			
			if (!TextUtils.isEmpty(requestMethod)) {
				connection.setRequestMethod(requestMethod);
			}
			
			if (!TextUtils.isEmpty(contentType)) {
				connection.setRequestProperty("Content-type", contentType);
			}
			
			OutputStream output = null;
			InputStream input = null;
			
			try {
				byte[] outputBytes = null;
				
				if (outputHandler != null) {
					
					outputBytes = outputHandler.getOutputBytes(output);
					
					output = new BufferedOutputStream(connection.getOutputStream());
					
					IoUtils.writeBytes(outputBytes, output, false);
					
					output.flush();
				}
				
				final int responseCode = connection.getResponseCode();
				
				if (responseCode == HttpURLConnection.HTTP_OK
					) {
					if (connectionHandler != null) {
						return connectionHandler.handleConnection(connection);
					}
					
					if (inputHandler != null) {
						input = new BufferedInputStream(connection.getInputStream());
						
						return inputHandler.handleInput(input);
					}
				} else {
					if (responseHandler != null) {
						responseHandler.handleResponseCode(responseCode);
					}
				}
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (final IOException ignored) {
					}
				}
				
				if (input != null) {
					try {
						input.close();
					} catch (final IOException ignored) {
					}
				}
				
				connection.disconnect();
			}
		} catch (final IOException e) {
			L.e(e);
			
			if (responseHandler != null) {
				responseHandler.handleException(e);
			}
		}
		
		return null;
	}
	
}
