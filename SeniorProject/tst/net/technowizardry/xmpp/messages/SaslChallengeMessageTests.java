package net.technowizardry.xmpp.messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class SaslChallengeMessageTests {
	private static final String TEST_INPUT = "cj1vTXNUQUF3QUFBQU1BQUFBTlAwVEFBQUFBQUJQVTBBQWUxMjQ2OTViLTY5YTktNGRlNi05YzMwLWI1MWIzODA4YzU5ZSxzPU5qaGtZVE0wTURndE5HWTBaaTAwTmpkbUxUa3hNbVV0TkRsbU5UTm1ORE5rTURNeixpPTQwOTY=";
	private static final String EXPECTED_SALT = "NjhkYTM0MDgtNGY0Zi00NjdmLTkxMmUtNDlmNTNmNDNkMDMz";
	private static final String EXPECTED_ITERATIONS = "4096";
	private static final String EXPECTED_NONCE = "oMsTAAwAAAAMAAAANP0TAAAAAABPU0AAe124695b-69a9-4de6-9c30-b51b3808c59e";
	@Test
	public void testDecode() {
		SaslChallengeMessage message = new SaslChallengeMessage(TEST_INPUT);
		assertEquals(EXPECTED_SALT, message.GetProperty("s"));
		assertEquals(EXPECTED_ITERATIONS, message.GetProperty("i"));
		assertEquals(EXPECTED_NONCE, message.GetProperty("r"));
	}
}
