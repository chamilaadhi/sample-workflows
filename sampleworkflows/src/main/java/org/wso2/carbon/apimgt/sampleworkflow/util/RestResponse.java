package org.wso2.carbon.apimgt.sampleworkflow.util;

/**
 * Data model for rest util response
 */
public class RestResponse {

    private String content;
    private int httpStatus;

    public RestResponse(String content, int httpStatus) {      
        this.content = content;       
        this.httpStatus = httpStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }


    @Override
    public String toString() {
        return "RestResponse [content=" + content + ", httpStatus=" + httpStatus + "]";
    }    
}
