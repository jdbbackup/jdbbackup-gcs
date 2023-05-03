package com.fathzer.jdbbackup.destinations.gcs;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.function.Function;

import com.fathzer.jdbbackup.DestinationManager;
import com.fathzer.jdbbackup.ProxyCompliant;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/** A Destination manager that saves to Google Cloud storage.*/
public class GCSManager implements DestinationManager<BucketPath>, ProxyCompliant {
	private static final String PROXY_HOST_PROP = "https.proxyHost";
	private static final String PROXY_PORT_PROP = "https.proxyPort";
	
	private Storage storage = StorageOptions.getDefaultInstance().getService();
	
	@Override
	public void setProxy(Proxy proxy, PasswordAuthentication auth) {
		ProxyCompliant.super.setProxy(proxy, auth);
		if (Proxy.NO_PROXY.equals(proxy)) {
			System.clearProperty(PROXY_HOST_PROP);
			System.clearProperty(PROXY_PORT_PROP);
		} else {
			// Unfortunately, I have no idea of how to set the proxy without setting system wide property ... which is a very bad practice
			System.setProperty(PROXY_HOST_PROP, ((InetSocketAddress)proxy.address()).getHostString());
			System.setProperty(PROXY_PORT_PROP, Integer.toString(((InetSocketAddress)proxy.address()).getPort()));
			if (auth!=null) {
				Authenticator.setDefault(new Authenticator() {
					@Override
					public PasswordAuthentication getPasswordAuthentication() {
						return auth;
					}
				});
			}
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
