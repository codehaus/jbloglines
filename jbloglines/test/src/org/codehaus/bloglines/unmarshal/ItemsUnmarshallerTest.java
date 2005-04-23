/*
 * Created on Dec 9, 2004
 *
 * 
 * Copyright (c) Zohar Melamed All rights reserved.
 * 
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. Due credit should be given to The Codehaus and Contributors
 * http://timtam.codehaus.org/
 * 
 * THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 *  
 */
package org.codehaus.bloglines.unmarshal;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import org.codehaus.bloglines.exceptions.BloglinesException;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.Constraint;

import java.io.StringReader;

public class ItemsUnmarshallerTest extends MockObjectTestCase {
    public void testUnmarshallUsesSyndInputFeedToBuildSyndFeed() throws Exception {
        String items = "Items to unmarshall";
        Mock syndFeedInputMock = mock(SyndFeedInput.class);
        SyndFeed expectedSyndFeed = (SyndFeed) mock(SyndFeed.class).proxy();
        syndFeedInputMock.expects(once()).method("build").with(eqStringReader(items)).will(returnValue(expectedSyndFeed));

        ItemsUnmarshaller itemUnmarshaller = new ItemsUnmarshallermpl((SyndFeedInput) syndFeedInputMock.proxy());

        SyndFeed syndFeed = itemUnmarshaller.unmarshal(items);

        assertSame(expectedSyndFeed, syndFeed);
    }

    public void testUnmarshallWrapsExceptionAsBloglinesException() throws Exception {
        String items = "Items to unmarshall";
        Mock syndFeedInputMock = mock(SyndFeedInput.class);
        Throwable thrownException = new IllegalArgumentException();
        syndFeedInputMock.expects(once()).method("build").with(eqStringReader(items)).will(throwException(thrownException));

        ItemsUnmarshaller itemUnmarshaller = new ItemsUnmarshallermpl((SyndFeedInput) syndFeedInputMock.proxy());

        try {
            itemUnmarshaller.unmarshal(items);
            fail("Sould have thrown exception");
        } catch (BloglinesException e) {
            assertEquals("umarshalling items failed", e.getMessage());
            assertSame(thrownException, e.getCause());
        }

    }

    private class StringReaderContentsConstraint implements Constraint {
        private String expectedStringContents;

        public StringReaderContentsConstraint(String expectedStringContents) {
            this.expectedStringContents = expectedStringContents;
        }

        public boolean eval(Object o) {
            try {
                StringReader stringReader = (StringReader) o;
                char[] chars = new char[expectedStringContents.length() + 5];
                int length = stringReader.read(chars);
                String content = new String(chars, 0, length);
                return content.equals(expectedStringContents);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public StringBuffer describeTo(StringBuffer stringBuffer) {
            return stringBuffer.append("<StringReader:{" + expectedStringContents + "}>");
        }
    }

    private Constraint eqStringReader(String items) {
        Constraint stringReaderContents = new StringReaderContentsConstraint(items);
        return stringReaderContents;
    }

}
