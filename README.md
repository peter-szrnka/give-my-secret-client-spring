# give-my-secret-client-spring
Official <u>Spring Boot</u> client library for [**Give My Secret**](https://github.com/peter-szrnka/give-my-secret) credential storage application. 

# Getting started
## Prerequisites

- The minimum supported version of Spring Boot is **3.0.1**,
- Minimum Java version: **17**

## Maven dependency

To use the official Spring client you have to add the library artifact to your Maven pom.xml:

```
    <dependency>
        <groupId>io.github.peter-szrnka</groupId>
        <artifactId>give-my-secret-client-spring</artifactId>
        <version>1.0.1</version>
    </dependency>
```

## Configuration properties

| Property Key                          | Type    | Default value | dESCRIPTION                                              |
| ------------------------------------- | ------- | ------------- | -------------------------------------------------------- |
| giveMySeret.baseUrl *                 | String  | null          | Base URL of the Give My Secret instance                  |
| giveMySecret.apiKey *                 | String  | null          | API key that will be sent to GMS                         |
| giveMySecret.secretId *               | String  | null          | Unique secret identifier                                 |
| giveMySecret.decrypt                  | Boolean | true          | Decryption enabled or not with the given keystore file   |
| giveMySecret.keystore.file            | String  | null          | Path to the keystore file                                |
| giveMySecret.keystore.type            | String  | PKCS12        | Type of the keystore file. Possible values: PKCS12 / JKS |
| giveMySecret.keystore.credential      | String  | null          | Keystore credential                                      |
| giveMySecret.keystore.alias           | String  | null          | Keystore alias that contains the encryption key          |
| giveMySecret.keystore.aliasCredential | String  | null          | Keystore alias credential                                |
| giveMySecret.maxRetry                 | int     | 3             | Maximum number of retries                                |
| giveMySecret.retryDelay               | int     | 1000          | Delay between the retries                                |
| giveMySecret.defaultConnectionTimeout | int     | 30000         | Connection timeout                                       |
| giveMySecret.defaultReadTimeout       | int     | 30000         | Read timeout                                             |
| giveMySecret.disableSslVerification   | Boolean | false         | SSL verification disabled or enabled                     |

*-Mandatory property

## Code sample

To define a client configuration you need to create a new properties file:

```
giveMySecret.baseUrl=http://localhost:8080
giveMySecret.apiKey=...
giveMySecret.secretId=secret1
giveMySecret.decrypt=false
```

or (if you need decryption with a keystore)

```
giveMySecret.baseUrl=http://localhost:8080
giveMySecret.apiKey=...
giveMySecret.secretId=secret2
giveMySecret.keystore.file=src/main/resources/test.p12
giveMySecret.keystore.type=PKCS12
giveMySecret.keystore.credential=...
giveMySecret.keystore.alias=test
giveMySecret.keystore.aliasCredential=...
```

Next you have to add the placeholder:

```
config.test=giveMySecret(gms-config1.properties:value)
config.username=giveMySecret(src/main/resources/gms-config2.properties:username)
config.password=giveMySecret(gms-config2.properties:password)
config.other1=giveMySecret(src/main/resources/gms-config2.properties:other1)
config.other2=giveMySecret(src/main/resources/gms-config2.properties:other2)
```

Then you can use these properties in your code:

```java
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${config.test}")
    private String test;

    @Value("${config.username}")
    private String test1;
    @Value("${config.password}")
    private String test2;

    @Value("${config.other1}")
    private String test3;

    @Value("${config.other2}")
    private String test4;

    @GetMapping
    public String test() {
        return "Test value: " + test + "; Username: " + test1 + "; Password: " + test2 + ";" + test3 + ";" + test4;
    }
}
```



# Code quality & health
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=peter-szrnka_give-my-secret-client-spring&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=peter-szrnka_give-my-secret-client-spring)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=peter-szrnka_give-my-secret-client-spring&metric=coverage)](https://sonarcloud.io/summary/new_code?id=peter-szrnka_give-my-secret-client-spring)
