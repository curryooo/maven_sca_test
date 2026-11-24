package net.tiaotiaolong.maven_test.securityfixture;

/** Offline-generated, unused scanner fixtures. Never use these values as credentials. */
public final class SensitiveInformationFixture {
    // SI-AK-001: synthetic provider-format pair, never issued by a provider.
    private static final String ACCESS_KEY_ID = "LTAI5teucmcQWTFQlHT7az2f";
    private static final String ACCESS_KEY_SECRET = "H5qkj8Pb98cWrjwEoV18suCxIlbqwN";

    // SI-TOKEN-001: synthetic LLM token; never sent to any API.
    private static final String LLM_API_KEY = "sk-iKU30eNoCWaDqTCUUQB9kxSvbvpM8pYva38buBSDPWkIj0xT";

    private SensitiveInformationFixture() {
    }
}
