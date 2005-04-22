package org.codehaus.bloglines.http;

import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;

public class HttpMethodFactoryImplTest extends TestCase {
    public void testCreateGetMethodWillReturnAGetMethodWithTheCorrectUri() throws URIException {
        HttpMethodFactoryImpl httpMethodFactory = new HttpMethodFactoryImpl();

        String uri = "http://dasdfldsdkufh.com/";

        HttpMethod getMethod = httpMethodFactory.createGetMethod(uri);

        assertEquals(uri, getMethod.getURI().toString());
    }
}
