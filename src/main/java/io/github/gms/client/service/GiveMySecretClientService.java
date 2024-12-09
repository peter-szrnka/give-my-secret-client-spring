package io.github.gms.client.service;

import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.enums.KeystoreType;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;
import io.github.gms.client.util.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

import static io.github.gms.client.util.Constants.EMPTY;
import static io.github.gms.client.validator.InputValidator.validateKeystoreParameters;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class GiveMySecretClientService {

    public Map<String, String> getSecret(Properties properties) throws Exception {
        GiveMySecretClientConfig config = createConfig(properties);
        io.github.gms.client.GiveMySecretClient client = GiveMySecretClientBuilder.create(config);
        GetSecretRequest.Builder builder = initApiKeyAndSecretId(properties);

        if (Constants.TRUE.equals(properties.getProperty("giveMySecret.decrypt", Constants.TRUE))) {
            initKeystoreSettings(builder, properties);
        }

        return client.getSecret(builder.build());
    }

    private static GiveMySecretClientConfig createConfig(Properties properties) {
        return GiveMySecretClientConfig.builder()
                .url(properties.getProperty("giveMySecret.baseUrl", "http://localhost:8080/"))
                .build();
    }

    private static GetSecretRequest.Builder initApiKeyAndSecretId(Properties properties) {
        return GetSecretRequest.builder()
                .apiKey(properties.getProperty("giveMySecret.apiKey", EMPTY))
                .secretId(properties.getProperty(Constants.GIVE_MY_SECRET_SECRET_ID, EMPTY));
    }

    private static void initKeystoreSettings(GetSecretRequest.Builder builder, Properties properties) throws FileNotFoundException {
        validateKeystoreParameters(properties);
        builder.keystore(new FileInputStream(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_FILE)))
                .keystoreType(KeystoreType.valueOf(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_TYPE, "PKCS12")))
                .keystoreCredential(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_CREDENTIAL))
                .keystoreAlias(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_ALIAS))
                .keystoreAliasCredential(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_ALIAS_CREDENTIAL));
    }
}
