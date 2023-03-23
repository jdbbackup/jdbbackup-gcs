package com.fathzer.jdbbackup.managers.gcs;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.function.Function;

import com.fathzer.jdbbackup.DestinationManager;
import com.fathzer.jdbbackup.utils.ProxySettings;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/** A Destination manager that saves to Google Cloud storage.*/
public class GCSManager implements DestinationManager<BucketPath> {
	private static final String PROXY_HOST_PROP = "https.proxyHost";
	private static final String PROXY_PORT_PROP = "https.proxyPort";
	
	private Storage storage = StorageOptions.getDefaultInstance().getService();
	
	@Override
	public void setProxy(ProxySettings proxy) {
		// Unfortunately, I have no idea of how to set the proxy without setting system wide property ... which is a very bad practice
		if (proxy!=null) {
			System.setProperty(PROXY_HOST_PROP, proxy.getHost());
			System.setProperty(PROXY_PORT_PROP, Integer.toString(proxy.getPort()));
			if (proxy.getLogin()!=null) {
				Authenticator.setDefault(new Authenticator() {
					@Override
					public PasswordAuthentication getPasswordAuthentication() {
						return proxy.getLogin();
					}
				});
			}
		} else {
			System.clearProperty(PROXY_HOST_PROP);
			System.clearProperty(PROXY_PORT_PROP);
		}
	}

	@Override
	public String getScheme() {
		return "gcs";
	}

	@Override
	public BucketPath validate(String path, Function<String, CharSequence> extensionBuilder) {
		return new BucketPath(path, extensionBuilder);
	}

	@Override
	public void send(InputStream in, long size, BucketPath destination) throws IOException {
		storage.createFrom(BlobInfo.newBuilder(destination.getBucket(), destination.getPath()).build(), in);
	}
}
