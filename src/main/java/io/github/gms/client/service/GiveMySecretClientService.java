package io.github.gms.client.service;

import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.enums.KeystoreType;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Properties;

import static io.github.gms.client.model.ClientProperty.*;
import static io.github.gms.client.util.FileUtil.loadPropertiesFile;
import static io.github.gms.client.util.PropertyLoader.*;
import static io.github.gms.client.validator.InputValidator.validateKeystoreParameters;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class GiveMySecretClientService {

    public Map<String, String> getSecret(Properties properties)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException,
            CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException,
            BadPaddingException, InvalidKeyException, KeyManagementException {

        GiveMySecretClientConfig config = createConfig(properties);
        io.github.gms.client.GiveMySecretClient client = GiveMySecretClientBuilder.create(config);
        GetSecretRequest.Builder builder = initApiKeyAndSecretId(properties);

        if (getBooleanProperty(properties, DECRYPT)) {
            initKeystoreSettings(builder, properties);
        }

        return client.getSecret(builder.build());
    }

    private static GiveMySecretClientConfig createConfig(Properties properties) {
        GiveMySecretClientConfig.Builder builder = GiveMySecretClientConfig.builder()
                .url(getStringProperty(properties, BASE_URL));

        builder.maxRetry(getIntProperty(properties, MAX_RETRY));
        builder.retryDelay(getIntProperty(properties, RETRY_DELAY));
        builder.defaultConnectionTimeout(getIntProperty(properties, CONNECTION_TIMEOUT));
        builder.defaultReadTimeout(getIntProperty(properties, READ_TIMEOUT));
        builder.disableSslVerification(getBooleanProperty(properties, DISABLE_SSL_VERIFICATION));

        return builder.build();
    }

    private static GetSecretRequest.Builder initApiKeyAndSecretId(Properties properties) {
        return GetSecretRequest.builder()
                .apiKey(getStringProperty(properties, API_KEY))
                .secretId(getStringProperty(properties, SECRET_ID));
    }

    private static void initKeystoreSettings(GetSecretRequest.Builder builder, Properties properties) {
        validateKeystoreParameters(properties);
        builder.keystore(loadPropertiesFile(getStringProperty(properties, KEYSTORE_FILE)))
                .keystoreType(KeystoreType.valueOf(getStringProperty(properties, KEYSTORE_TYPE)))
                .keystoreCredential(getStringProperty(properties, KEYSTORE_CREDENTIAL))
                .keystoreAlias(getStringProperty(properties, KEYSTORE_ALIAS))
                .keystoreAliasCredential(getStringProperty(properties, KEYSTORE_ALIAS_CREDENTIAL));
    }
}
