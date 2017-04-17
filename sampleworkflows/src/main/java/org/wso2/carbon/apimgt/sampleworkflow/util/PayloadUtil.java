package org.wso2.carbon.apimgt.sampleworkflow.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Utility class to build payload
 */
public class PayloadUtil {

    public static String buildBPMNProcessStartPalyload(String processDefinitionKey, String workflowReferenceId,
            String clientId, String clientSecret, String scope, String tokenAPI, String description,
            String callbackUrl) {

        JSONArray variables = new JSONArray();

        JSONObject clientIdObj = new JSONObject();
        clientIdObj.put("name", "clientId");
        clientIdObj.put("value", clientId);
        variables.add(clientIdObj);

        JSONObject clientSecretObj = new JSONObject();
        clientSecretObj.put("name", "clientSecret");
        clientSecretObj.put("value", clientSecret);
        variables.add(clientSecretObj);

        JSONObject scopeObj = new JSONObject();
        scopeObj.put("name", "scope");
        scopeObj.put("value", scope);
        variables.add(scopeObj);

        JSONObject tokenAPIObj = new JSONObject();
        tokenAPIObj.put("name", "tokenAPI");
        tokenAPIObj.put("value", tokenAPI);
        variables.add(tokenAPIObj);

        JSONObject descriptionObj = new JSONObject();
        descriptionObj.put("name", "description");
        descriptionObj.put("value", description);
        variables.add(descriptionObj);

        JSONObject callbackUrlObj = new JSONObject();
        callbackUrlObj.put("name", "callbackUrl");
        callbackUrlObj.put("value", callbackUrl);
        variables.add(callbackUrlObj);

        JSONObject wfReferenceObj = new JSONObject();
        wfReferenceObj.put("name", "wfReference");
        wfReferenceObj.put("value", workflowReferenceId);
        variables.add(wfReferenceObj);

        JSONObject payload = new JSONObject();
        payload.put("processDefinitionKey", processDefinitionKey);

        // set workflowreferencid to business key so we can later query the process instance using this value
        // if we want to delete the instance
        payload.put("businessKey", workflowReferenceId);
        payload.put("variables", variables);

        return payload.toJSONString();

    }

    public static String getProcessId(String content) throws ParseException {
        String processId = null;
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(content);
        JSONArray data = (JSONArray) obj.get("data");
        if (data != null) {
            JSONObject instanceDetails = (JSONObject) data.get(0);
            // extract the id related to that process. this id is used to delete the process
            processId = (String) instanceDetails.get("id");
        }
        return processId;
    }

}
