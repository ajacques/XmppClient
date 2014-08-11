package net.technowizardry.xmpp;

public class XmppPackets {
	private static final String STREAM_INIT_START =
			"<stream:stream xmlns:stream='http://etherx.jabber.org/streams' version='1.0' from='technowizardry.net' id='594fbf13-6a48-4caf-8ac3-5f9ceecc155e' xml:lang='en' xmlns='jabber:client'>";
	public static final String STREAM_INIT_STARTTLS_REQUIRED =
			"<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";
	public static final String STREAM_INIT_NOFEATURE = 
			  STREAM_INIT_START
			+   "<features />"
			+ "</stream:stream>";
	public static String buildStreamInit(String... features) {
		StringBuilder builder = new StringBuilder();
		builder.append(STREAM_INIT_START);
		builder.append("<features>");
		for (String feature : features) {
			builder.append(feature);
		}
		builder.append("</features>");
		builder.append("</stream:stream>");
		return builder.toString();
	}
}
