package org.codehaus.bloglines.http;

import org.codehaus.bloglines.exceptions.BloglinesException;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.UnsupportedEncodingException;

public class BloglinesRestCallerImplTest extends MockObjectTestCase {
    public void testSetCredentialsCallsTheHttpCaller() {
        String name = "sldfkashdf";
        String password = "dsfgkasdf";

        Mock httpCallerMock = mock(HttpCaller.class);
        httpCallerMock.expects(once()).method("setCredentials").with(same(name), same(password));

        BloglinesRestCallerImpl caller = new BloglinesRestCallerImpl((HttpCaller) httpCallerMock.proxy(), "UTF-8");

        caller.setCredentials(name, password);
    }

    public void testCallWillInvokeHttpCallerWithCorrectUriWhenNoArguments() throws BloglinesException {
        Mock httpCallerMock = mock(HttpCaller.class);
        String expectedUri = "http://rpc.bloglines.com/someMethod";
        httpCallerMock.expects(once()).method("doGet").with(eq(expectedUri));

        BloglinesRestCallerImpl caller = new BloglinesRestCallerImpl((HttpCaller) httpCallerMock.proxy(), "UTF-8");

        caller.call("someMethod", new String[0]);
    }

    public void testCallWillInvokeHttpCallerWithCorrectUriWhenOneArgument() throws BloglinesException {
        Mock httpCallerMock = mock(HttpCaller.class);
        String expectedUri = "http://rpc.bloglines.com/someMethod?name=value";
        httpCallerMock.expects(once()).method("doGet").with(eq(expectedUri));

        BloglinesRestCallerImpl caller = new BloglinesRestCallerImpl((HttpCaller) httpCallerMock.proxy(), "UTF-8");

        caller.call("someMethod", new String[]{"name", "value"});
    }

    public void testCallWillInvokeHttpCallerWithCorrectUriWhenManyArguments() throws BloglinesException {
        Mock httpCallerMock = mock(HttpCaller.class);
        String expectedUri = "http://rpc.bloglines.com/someMethod?name1=value1&name2=value2&name3=value3";
        httpCallerMock.expects(once()).method("doGet").with(eq(expectedUri));

        BloglinesRestCallerImpl caller = new BloglinesRestCallerImpl((HttpCaller) httpCallerMock.proxy(), "UTF-8");

        caller.call("someMethod", new String[]{"name1", "value1", "name2", "value2", "name3", "value3"});
    }

    public void testWhenUrlEncodingExceptionIsThrownItIsWrappedByABloglinesException() throws BloglinesException {
        Mock httpCallerMock = mock(HttpCaller.class);
        BloglinesRestCallerImpl caller = new BloglinesRestCallerImpl((HttpCaller) httpCallerMock.proxy(), "Unsupported-16");
        try {
            caller.call("someMethod", new String[]{"name1", "value1"});
            fail("Should have thrown exception");
        } catch (BloglinesException e) {
            assertEquals("bad encoding: Unsupported-16", e.getMessage());
            assertEquals(UnsupportedEncodingException.class, e.getCause().getClass());
        }
    }

}

