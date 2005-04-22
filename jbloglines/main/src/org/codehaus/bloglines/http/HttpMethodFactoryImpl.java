package org.codehaus.bloglines.http;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpMethodFactoryImpl implements HttpMethodFactory {
    public HttpMethod createGetMethod(String uri) {
        return new GetMethod(uri);
    }
}
