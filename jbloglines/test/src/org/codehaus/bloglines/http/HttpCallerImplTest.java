package org.codehaus.bloglines.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.codehaus.bloglines.exceptions.BloglinesException;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.Constraint;

import java.io.IOException;

public class HttpCallerImplTest extends MockObjectTestCase {
    private Mock httpClientMock;
    private Mock httpMethodFactoryMock;
    private Mock getMethodMock;
    private HttpMethod getMethod;
    private HttpCallerImpl httpCaller;
    private String uri;

    protected void setUp() throws Exception {
        super.setUp();
        httpClientMock = mock(HttpClient.class);
        httpMethodFactoryMock = mock(HttpMethodFactory.class);
        getMethodMock = mock(HttpMethod.class);
        getMethod = (HttpMethod) getMethodMock.proxy();

        httpCaller = new HttpCallerImpl((HttpClient) httpClientMock.proxy(), (HttpMethodFactory) httpMethodFactoryMock.proxy());

        uri = "http://whatever.com";
    }

    public void testSetCredentialsWillCallSetCredentialsOnTheStateOnHttpClient() throws Exception {
        String name = "iksdfhkja";
        String password = "guighsd";

        Mock httpStateMock = mock(HttpState.class);
        HttpState httpState = (HttpState) httpStateMock.proxy();
        httpClientMock.expects(once()).method("getState").withNoArguments().will(returnValue(httpState));
        UsernamePasswordCredentials expectedCredentials = new UsernamePasswordCredentials(name, password);
        httpStateMock.expects(once())
                .method("setCredentials")
                .with(eq("Bloglines RPC"), eq("rpc.bloglines.com"), eqCredentials(expectedCredentials));

        httpCaller.setCredentials(name, password);
    }

    public void testDoGetCallsExecuteOnHttpClientAndClosedConnection() throws Exception {
        String expectedResponse = "skdsdfjhksadfh";

        int httpReturnCode = 200;

        httpMethodFactoryMock.expects(once()).method("createGetMethod").with(eq(uri)).will(returnValue(getMethod));
        getMethodMock.expects(once()).method("setDoAuthentication").with(eq(true));
        httpClientMock.expects(once()).method("executeMethod").with(same(getMethod)).will(returnValue(httpReturnCode));
        getMethodMock.expects(once()).method("getResponseBodyAsString").will(returnValue(expectedResponse));
        getMethodMock.expects(once()).method("releaseConnection");

        String response = httpCaller.doGet(uri);

        assertEquals(expectedResponse, response);
    }

    public void testDoGetThrowsBloglinesExceptionWhenReturnCodeIsNotOKButStillReleasesConnection() throws Exception {
        int httpReturnCode = 404;

        httpMethodFactoryMock.expects(once()).method("createGetMethod").with(eq(uri)).will(returnValue(getMethod));
        getMethodMock.expects(once()).method("setDoAuthentication").with(eq(true));
        httpClientMock.expects(once()).method("executeMethod").with(same(getMethod)).will(returnValue(httpReturnCode));
        getMethodMock.expects(once()).method("releaseConnection");

        try {
            httpCaller.doGet(uri);
            fail("Should have thrown exception");
        } catch (BloglinesException e) {
            assertEquals("Error 404 (Not Found) in HTTP call: http://whatever.com", e.getMessage());
        }
    }

    public void testWrapsIOExceptionInBloglinesExceptionAndReleasesConnectionWhenErrorOccursInDoGet() throws Exception {
        httpMethodFactoryMock.expects(once()).method("createGetMethod").with(eq(uri)).will(returnValue(getMethod));
        getMethodMock.expects(once()).method("setDoAuthentication").with(eq(true));
        IOException expectedIOException = new IOException();
        httpClientMock.expects(once()).method("executeMethod").with(same(getMethod)).will(throwException(expectedIOException));
        getMethodMock.expects(once()).method("releaseConnection");

        try {
            httpCaller.doGet(uri);
            fail("Should have thrown exception");
        } catch (BloglinesException e) {
            assertEquals("Http call failed: http://whatever.com", e.getMessage());
            assertEquals(expectedIOException, e.getCause());
        }
    }

    private Constraint eqCredentials(UsernamePasswordCredentials expectedCredentials) {
        return new HttpCallerImplTest.UsernamePasswordCredentialsEqualConstraint(expectedCredentials);
    }

    public class UsernamePasswordCredentialsEqualConstraint implements Constraint {
        private final UsernamePasswordCredentials expectedCredentials;

        public UsernamePasswordCredentialsEqualConstraint(UsernamePasswordCredentials expectedCredentials) {
            this.expectedCredentials = expectedCredentials;
        }

        public StringBuffer describeTo(StringBuffer stringBuffer) {
            return stringBuffer.append("<UsernamePasswordCredentials: {" + expectedCredentials + "}");
        }

        public boolean eval(Object o) {
            try {
                UsernamePasswordCredentials credentials = (UsernamePasswordCredentials) o;
                return expectedCredentials.getUserName().equals(credentials.getUserName()) &&
                       expectedCredentials.getPassword().equals(credentials.getPassword());
            } catch (ClassCastException e) {
                return false;
            }
        }
    }
}
