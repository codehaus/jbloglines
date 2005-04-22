package org.codehaus.bloglines.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.codehaus.bloglines.exceptions.BloglinesException;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class HttpCallerImplTest extends MockObjectTestCase {
    public void testDoGetCallsExecuteOnHttpClientAndClosedConnection() throws Exception {
        String expectedResponse = "skdsdfjhksadfh";
        String uri = "http://whatever.com";

        Mock httpClientMock = mock(HttpClient.class);
        Mock httpMethodFactoryMock = mock(HttpMethodFactory.class);
        Mock getMethodMock = mock(HttpMethod.class);
        HttpMethod getMethod = (HttpMethod) getMethodMock.proxy();

        int httpReturnCode = 200;

        httpMethodFactoryMock.expects(once()).method("createGetMethod").with(eq(uri)).will(returnValue(getMethod));
        getMethodMock.expects(once()).method("setDoAuthentication").with(eq(true));
        httpClientMock.expects(once()).method("executeMethod").with(same(getMethod)).will(returnValue(httpReturnCode));
        getMethodMock.expects(once()).method("getResponseBodyAsString").will(returnValue(expectedResponse));
        getMethodMock.expects(once()).method("releaseConnection");

        HttpCallerImpl httpCaller = new HttpCallerImpl((HttpClient) httpClientMock.proxy(), (HttpMethodFactory) httpMethodFactoryMock.proxy());

        String response = httpCaller.doGet(uri);

        assertEquals(expectedResponse, response);
    }

    public void testDoGetThrowsBloglinesExceptionWhenReturnCodeIsNotOKButStillReleasesConnection() throws Exception {
        String uri = "http://whatever.com";

        Mock httpClientMock = mock(HttpClient.class);
        Mock httpMethodFactoryMock = mock(HttpMethodFactory.class);
        Mock getMethodMock = mock(HttpMethod.class);
        HttpMethod getMethod = (HttpMethod) getMethodMock.proxy();

        int httpReturnCode = 404;

        httpMethodFactoryMock.expects(once()).method("createGetMethod").with(eq(uri)).will(returnValue(getMethod));
        getMethodMock.expects(once()).method("setDoAuthentication").with(eq(true));
        httpClientMock.expects(once()).method("executeMethod").with(same(getMethod)).will(returnValue(httpReturnCode));
        getMethodMock.expects(once()).method("releaseConnection");

        HttpCallerImpl httpCaller = new HttpCallerImpl((HttpClient) httpClientMock.proxy(), (HttpMethodFactory) httpMethodFactoryMock.proxy());

        try {
            httpCaller.doGet(uri);
            fail("Should have thrown exception");
        } catch (BloglinesException e) {
            assertEquals("Error 404 (Not Found) in HTTP call: http://whatever.com", e.getMessage());
        }
    }

}
