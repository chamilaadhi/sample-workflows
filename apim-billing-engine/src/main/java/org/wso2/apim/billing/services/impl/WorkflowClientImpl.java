/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* WSO2 Inc. licenses this file to you under the Apache License,
* Version 2.0 (the "License"); you may not use this file except
* in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.wso2.apim.billing.services.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.wso2.apim.billing.bean.RedirectBean;
import org.wso2.apim.billing.domain.UserEntity;
import org.wso2.apim.billing.services.WorkflowClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WorkflowClientImpl implements WorkflowClient {
    Logger log = Logger.getLogger(WorkflowClientImpl.class);
    private String jksPath;
    private String jksPassword;
    private String clientId;
    private String clientSecret;
    private String tokenAPI;
    private String tokenScope;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTokenAPI() {
        return tokenAPI;
    }

    public void setTokenAPI(String tokenAPI) {
        this.tokenAPI = tokenAPI;
    }

    public String getJksPath() {
        return jksPath;
    }

    public void setJksPath(String jksPath) {
        this.jksPath = jksPath;
    }

    public String getTokenScope() {
        return tokenScope;
    }

    public void setTokenScope(String tokenScope) {
        this.tokenScope = tokenScope;
    }

    public String getJksPassword() {
        return jksPassword;
    }

    public void setJksPassword(String jksPassword) {
        this.jksPassword = jksPassword;
    }

    public boolean activateSubscription(RedirectBean bean, UserEntity userEntity) throws Exception {

        if (bean == null || bean.getReDirectUrl() == null || "account".equals(bean.getReDirectUrl())) {
            log.warn("Skipping activateSubscription ");
            return false;
        }
        System.setProperty("javax.net.ssl.trustStore", jksPath);
        System.setProperty("javax.net.ssl.trustStorePassword", jksPassword);

        return invokeCallbackservice(getAccessToken(), bean.getCallbackUrl() + "/" + bean.getWorkflowRefId());
    }

    private String getAccessToken() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("executing getAccessToken()..");
        }
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        String encodedCredentials = encodeCredentials(clientId, clientSecret.toCharArray());
        headers.add(new BasicNameValuePair("Authorization", "Basic " + encodedCredentials));
        headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));

        String body = "grant_type=client_credentials&scope=" + tokenScope;

        HttpResponse response = sendPOSTMessage(tokenAPI, headers, body);
        if (log.isDebugEnabled()) {
            log.debug("response:  " + response.getStatusLine());
        }
        String msg = getResponseBody(response);
        JSONObject jsonObj = new JSONObject(msg);
        return (String) jsonObj.get("access_token");
    }

    private boolean invokeCallbackservice(String token, String url) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("executing invokeCallbackservice()..");
        }
        boolean success = false;
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Authorization", "Bearer " + token));
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        
        String body = "{\"status\" : \"APPROVED\",\"attributes\" : {}}";
        HttpResponse response = sendPUTMessage(url, headers, body);
        if (log.isDebugEnabled()) {
            log.debug("response:  " + response.getStatusLine());
        }
        if(response.getStatusLine().getStatusCode() == 200){
            success = true;
        } 
        return success;
    }
    private HttpResponse sendPUTMessage(String url, List<NameValuePair> headers, String body) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();
        HttpPut put = new HttpPut(url);
        if (headers != null) {
            for (NameValuePair nameValuePair : headers) {
                put.addHeader(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        put.setEntity(new StringEntity(body));
        return httpClient.execute(put);
    }
    private HttpResponse sendPOSTMessage(String url, List<NameValuePair> headers, String body) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();
        HttpPost post = new HttpPost(url);
        if (headers != null) {
            for (NameValuePair nameValuePair : headers) {
                post.addHeader(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        post.setEntity(new StringEntity(body));
        return httpClient.execute(post);
    }

    private String getResponseBody(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String line;
        StringBuffer sb = new StringBuffer();

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        return sb.toString();
    }

    private String encodeCredentials(String user, char[] pass) {
        StringBuilder builder = new StringBuilder(user).append(':').append(pass);
        String cred = builder.toString();
        byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
        return new String(encodedBytes);
    }

}
