package org.codehaus.bloglines;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpClient;
import org.codehaus.bloglines.exceptions.BloglinesException;
import org.codehaus.bloglines.http.BloglinesRestCallerImpl;
import org.codehaus.bloglines.http.HttpCallerImpl;
import org.codehaus.bloglines.http.HttpMethodFactoryImpl;
import org.codehaus.bloglines.unmarshall.ItemsUnmarshallImpl;
import org.codehaus.bloglines.unmarshall.OutlineUnmarshalImpl;

/*
 * Created on Nov 24, 2004
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

/**
 * @author Zohar
 */
// we expect :
//<outline title="Subscriptions">
//<outline title="Bloglines | News" htmlUrl="http://www.bloglines.com" type="rss" xmlUrl="http://www.bloglines.com/rss/about/news"  BloglinesSubId="5259174"  BloglinesUnread="8" BloglinesIgnore="0" />
//<outline title="Stuff" BloglinesSubId="5259181" BloglinesIgnore="0">
//    <outline title="Boing Boing" htmlUrl="http://www.boingboing.net/" type="rss" xmlUrl="http://boingboing.net/rss.xml"  BloglinesSubId="5259182"  BloglinesUnread="26" BloglinesIgnore="0" />
//</outline>
//</outline>

public class BloglinesAcceptanceTests extends TestCase {
    private static final String PASSWORD = "poopoo";
    private static final String OWNER_NAME = "zohar@codehaus.org";
    private Bloglines bloglines;

    protected void setUp() throws Exception {
        super.setUp();
        bloglines = new Bloglines(new OutlineUnmarshalImpl(), new ItemsUnmarshallImpl(new SyndFeedInput()), new BloglinesRestCallerImpl(new HttpCallerImpl(new HttpClient(), new HttpMethodFactoryImpl()), "UTF-8"));
    }

    public void testWhatever() {

    }

    public void _testListSubs() throws BloglinesException {
        bloglines.setCredentials(OWNER_NAME, PASSWORD);
        Outline topLevel = bloglines.listSubscriptions();

        assertEquals("Subscriptions", topLevel.getTitle());
        assertEquals(false, topLevel.isFeed());

        Outline[] outlines = topLevel.getChildren();
        assertEquals(2, outlines.length);
        Outline stuff = outlines[1];
        assertEquals("Stuff", stuff.getTitle());
        assertEquals(1, stuff.getChildren().length);
        Outline boingboing = stuff.getChildren()[0];
        assertEquals("Boing Boing", boingboing.getTitle());
        assertTrue(boingboing.isFeed());
        assertEquals("http://www.boingboing.net/", boingboing.getHtmlUrl());
        assertEquals("http://boingboing.net/rss.xml", boingboing.getXmlUrl());
        assertEquals(boingboing.getSubscriptionId(), "5259182");
        assertFalse(boingboing.getIgnore());
    }

    public void _testGetItems() throws BloglinesException {
        bloglines.setCredentials(OWNER_NAME, PASSWORD);
        Outline topLevel = bloglines.listSubscriptions();
        Outline[] outlines = topLevel.getChildren();
        Outline stuff = outlines[1];
        Outline boingboing = stuff.getChildren()[0];
        SyndFeed items = bloglines.getItems(boingboing, false, null);
        assertEquals("Boing Boing", items.getTitle());
        assertEquals("http://www.boingboing.net/", items.getLink());
    }
}
