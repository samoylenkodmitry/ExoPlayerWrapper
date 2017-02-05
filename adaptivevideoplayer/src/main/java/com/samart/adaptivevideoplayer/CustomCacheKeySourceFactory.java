package com.samart.adaptivevideoplayer;


import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSinkFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.samart.adaptivevideoplayer.model.Format;

public class CustomCacheKeySourceFactory implements DataSource.Factory {
	
	private final SimpleCache mCache;
	private final DataSource.Factory mDataSourceFactory;
	private final Format mContentFormat;
	private final String mKeyPrefix;
	
	CustomCacheKeySourceFactory(final SimpleCache cache, final DataSource.Factory dataSourceFactory, final Format contentFormat, final String keyPrefix) {
		mCache = cache;
		mDataSourceFactory = dataSourceFactory;
		mContentFormat = contentFormat;
		mKeyPrefix = keyPrefix;
	}
	
	@Override
	public DataSource createDataSource() {
		// todo create custom key with uri parsing
		return new CacheDataSource(mCache, mDataSourceFactory.createDataSource(),
			new FileDataSourceFactory().createDataSource(),
			new CacheDataSinkFactory(mCache, ExoPlayerAdapter.CACHE_FILE_SIZE_MAX_BYTES).createDataSink(),
			CacheDataSource.FLAG_BLOCK_ON_CACHE
				& CacheDataSource.FLAG_IGNORE_CACHE_FOR_UNSET_LENGTH_REQUESTS
				& CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
	}
}
