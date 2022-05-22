// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.demo.service.azure.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class reads values from local configuration file
 * resources/conf/appsettings.json.
 * Please change the configuration using your account information. For more
 * information, see
 * https://docs.microsoft.com/azure/media-services/latest/access-api-cli-how-to.
 * For security
 * reasons, do not check in the configuration file to source control.
 */
@Configuration
//@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigWrapper {
//	@Value("${azure.media.client.id}")
//    private String azureClientId;
//	@Value("${azure.media.client.secret}")
//    private String azureClientSecret;
	@Value("${azure.media.tenant.id}")
    private String aadTenantId;
	@Value("${azure.media.service.account.name}")
    private String azureMediaServicesAccountName;
	@Value("${azure.media.arm.token.id}")
    private  String azureArmTokenAudience;
	@Value("${azure.media.arm.endpoint}")
    private String azureArmEndpoint;
	@Value("${azure.media.region}")
    private String region;
	@Value("${azure.resource.group}")
    private String azureResourceGroup;
	@Value("${azure.subscription.id}")
    private String azureSubscriptionId;
//    private static String CONF_JSON = "conf/appsettings.json";
//    private final JSONObject jsonObject;
//    private final InputStreamReader isReader;

//    public ConfigWrapper() {
//        InputStream inStream = ConfigWrapper.class.getClassLoader().getResourceAsStream(CONF_JSON);
//        isReader = new InputStreamReader(inStream);
//
//        JSONParser parser = new JSONParser();
//        Object obj = null;
//        try {
//            obj = parser.parse(isReader);
//        } catch (Exception ioe) {
//            System.err.println(ioe);
//            System.exit(1);
//        }
//
//        jsonObject = (JSONObject) obj;
//    }
//
//    public void close() {
//        try {
//            if (isReader != null) {
//                isReader.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getAadClientId() {
//        return (String) jsonObject.get(AZURE_CLIENT_ID);
//    }
//
//    public String getAadSecret() {
//        return (String) jsonObject.get(AZURE_CLIENT_SECRET);
//    }
//
//    public String getAadTenantId() {
//        return (String) jsonObject.get(AAD_TENANT_ID);
//    }
//
//    public String getAccountName() {
//        return (String) jsonObject.get(AZURE_MEDIA_SERVICES_ACCOUNT_NAME);
//    }
//
//    public String getArmAadAudience() {
//        return (String) jsonObject.get(AZURE_ARM_TOKEN_AUDIENCE);
//    }
//
//    public String getArmEndpoint() {
//        return (String) jsonObject.get(AZURE_ARM_ENDPOINT);
//    }
//
//    public String getRegion() {
//        return (String) jsonObject.get(REGION);
//    }
//
//    public String getResourceGroup() {
//        return (String) jsonObject.get(AZURE_RESOURCE_GROUP);
//    }
//
//    public String getSubscriptionId() {
//        return (String) jsonObject.get(AZURE_SUBSCRIPTION_ID);
//    }
}
