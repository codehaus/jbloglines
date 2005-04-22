package org.codehaus.bloglines.http;

import org.apache.commons.httpclient.HttpMethod;

public interface HttpMethodFactory {
    HttpMethod createGetMethod(String url);
}
