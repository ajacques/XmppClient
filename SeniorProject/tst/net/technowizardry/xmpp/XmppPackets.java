package net.technowizardry.xmpp;

public class XmppPackets {
	private static final String STREAM_INIT_START =
			"<stream:stream xmlns:stream='http://etherx.jabber.org/streams' version='1.0' from='technowizardry.net' id='594fbf13-6a48-4caf-8ac3-5f9ceecc155e' xml:lang='en' xmlns='jabber:client'>";
	public static final String STREAM_INIT_STARTTLS_REQUIRED =
			"<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'><required /></starttls>";
	public static final String STREAM_INIT_STARTTLS =
			"<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls' />";
	public static final String STREAM_INIT_NOFEATURE =
			  STREAM_INIT_START
			+   "<features />"
			+ "</stream:stream>";
	public static final String STREAM_INIT_COMPRESS =
			"<compression xmlns='http://jabber.org/features/compress'>"
			+ "<method>zlib</method><method>lzw</method>"
			+ "</compression>";
	public static final String STREAM_INIT_WEIRD_FEATURE =
			"<foobar xmlns='http://technowizardry.net/xmpp/madeup'>"
			+ "<boojum>Seven</boojum>"
			+ "</foobar>";
	public static final String SASL_SUCCESS = "<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl' />";
	public static String buildMechanismList(String... mechanisms) {
		StringBuilder builder = new StringBuilder();
		builder.append("<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
		for (String mech : mechanisms) {
			builder.append("<mechanism>");
			builder.append(mech);
			builder.append("</mechanism>");
		}
		builder.append("</mechanisms>");
		return builder.toString();
	}
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
	public static String buildIqMessage(String type, String content) {
		StringBuilder builder = new StringBuilder();
		builder.append("<iq xmlns='jabber:client' id='1' type='");
		builder.append(type);
		builder.append("'>");
		builder.append(content);
		builder.append("</iq>");
		return builder.toString();
	}
}
