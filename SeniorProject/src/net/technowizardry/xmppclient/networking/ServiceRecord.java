package net.technowizardry.xmppclient.networking;

public class ServiceRecord {
	private final String address;
	private final int port;
	private final int weight;
	private final int priority;

	public ServiceRecord(final String address, final int port, final int weight, final int priority) {
		this.address = address;
		this.port = port;
		this.weight = weight;
		this.priority = priority;
	}

	@Override
	public String toString() {
		return String.format("%s:%s [W: %s, PRI: %s]", address, port, weight, priority);
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public int getWeight() {
		return weight;
	}

	public int getPriority() {
		return priority;
	}
}
