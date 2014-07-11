package net.technowizardry.xmppclient.networking;

public class HostnameEndPoint {
	private final String hostname;
	private final int port;

	public HostnameEndPoint(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}
}
