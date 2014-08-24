package net.technowizardry.xmpp.auth;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Test;

public class ScramAuthStrategyTests {
	// Test vectors from RFC6120
	private static final String TEST_USERNAME = "juliet";
	private static final String TEST_PASSWORD = "r0m30myr0m30";
	private static final String TEST_NONCE = "oMsTAAwAAAAMAAAANP0TAAAAAABPU0AAe124695b-69a9-4de6-9c30-b51b3808c59e";
	private static final String TEST_SALT = "68da3408-4f4f-467f-912e-49f53f43d033";
	private static final int TEST_ITERATIONS = 4096;
	private static final String TEST_CLIENT_FIRST = "n,,n=juliet,r=oMsTAAwAAAAMAAAANP0TAAAAAABPU0AA";
	private static final String TEST_SERVER_FIRST = "r=oMsTAAwAAAAMAAAANP0TAAAAAABPU0AAe124695b-69a9-4de6-9c30-b51b3808c59e,s=NjhkYTM0MDgtNGY0Zi00NjdmLTkxMmUtNDlmNTNmNDNkMDMz,i=4096";

	@Test
	public void testGenerateNonce() {
		ScramAuthStrategy strategy = new ScramAuthStrategy(TEST_USERNAME, TEST_PASSWORD);
		String nonce = strategy.GenerateNonce();
		assertNotNull(nonce);
		assertFalse(nonce.isEmpty());
		assertTrue(nonce.length() == 24);
		assertTrue(String.format("%s did not match expected Regex", nonce), Pattern.matches("^[a-zA-Z0-9]+$", nonce));
	}

	@Test
	public void testGenerateResponse() throws IOException {
		ScramAuthStrategy strategy = new ScramAuthStrategy(TEST_USERNAME, TEST_PASSWORD);
		String result = strategy.GenerateResponse(TEST_NONCE, TEST_SALT, TEST_ITERATIONS, TEST_CLIENT_FIRST, TEST_SERVER_FIRST);
		assertEquals("c=biws,r=oMsTAAwAAAAMAAAANP0TAAAAAABPU0AAe124695b-69a9-4de6-9c30-b51b3808c59e,p=UA57tM/SvpATBkH2FXs0WDXvJYw=", result);
	}
}
