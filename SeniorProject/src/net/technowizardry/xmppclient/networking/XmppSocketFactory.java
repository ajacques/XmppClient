package net.technowizardry.xmppclient.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class XmppSocketFactory {
	private final ServiceEndpointResolver resolver;

	public XmppSocketFactory(ServiceEndpointResolver resolver) {
		this.resolver = resolver;
	}

	public Socket newConnection(String domainName) throws UnknownHostException, IOException {
		List<HostnameEndPoint> endpoints = resolver.fetchEndpoints(domainName);
		HostnameEndPoint endpoint = endpoints.get(0);
		Socket socket = new Socket(endpoint.GetHostname(), endpoint.GetPort());
		return socket;
	}
}
