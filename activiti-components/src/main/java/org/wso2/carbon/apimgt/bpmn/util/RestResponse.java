package org.wso2.carbon.apimgt.bpmn.util;

import org.apache.http.Header;

import java.util.Arrays;

public class RestResponse {
    private String contentType;
    private String content;
    private Header[] headers;
    private int httpStatus;

    public RestResponse(String contentType, String content, Header[] headers, int httpStatus) {
        this.contentType = contentType;
        this.content = content;
        this.headers = headers;
        this.httpStatus = httpStatus;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "RestResponse [contentType=" + contentType + ", content=" + content + ", headers="
                + Arrays.toString(headers) + ", httpStatus=" + httpStatus + "]";
    }
    
    

}
