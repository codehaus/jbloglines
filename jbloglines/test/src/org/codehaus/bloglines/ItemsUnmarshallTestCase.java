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

package org.codehaus.bloglines;

import java.util.List;

import org.codehaus.bloglines.exceptions.BloglinesException;
import org.codehaus.bloglines.unmarshall.ItemsUnmarshall;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

import junit.framework.TestCase;

/**
 * @author Sarah
 *
 */
public class ItemsUnmarshallTestCase extends TestCase {
	private final String ITEMS = 
		"<?xml version='1.0'?>"+
			"<rss version='2.0' "+
			 "xmlns:dc='http://purl.org/dc/elements/1.1/' "+
			 "xmlns:bloglines='http://www.bloglines.com/services/module' "+
			 "xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>"+
			  "<channel>"+
			    "<title>Feed Title</title>"+
			    "<link>http://www.feed.example/</link>"+
			    "<description>The description of the feed</description>"+
			    "<language>en-us</language>"+
			    "<webMaster>support@bloglines.com</webMaster>"+
			    "<bloglines:siteid>66</bloglines:siteid>"+
			    "<item>"+
				"<title>An entry title</title>"+
					"<dc:creator>John</dc:creator>"+
			        "<guid isPermaLink='true'>http://feed.example/2004/09/27/1.html</guid>"+
			        "<link>http://feed.example/2004/09/27/1.html</link>"+
			        "<description><![CDATA[Here is the entry text.]]></description>"+
			        "<pubDate>Mon, 27 Sep 2004 8:04:17 GMT</pubDate>"+
			        "<bloglines:itemid>12</bloglines:itemid>"+
			    "</item>"+
			    "</channel>"+
			"</rss>";
	
	public void testUnmarshallFeedItems() throws BloglinesException {
		ItemsUnmarshall itemsUnmarshall = new ItemsUnmarshall();
		SyndFeed feed = itemsUnmarshall.unmarshal(ITEMS);
		assertEquals("Feed Title",feed.getTitle());
		assertEquals("The description of the feed",feed.getDescription());
		List entries = feed.getEntries();
		assertEquals(1,entries.size());
		SyndEntry entry = (SyndEntry) entries.get(0);
		assertEquals("An entry title",entry.getTitle());
		List contents = entry.getContents();
		contents.getClass();
	}
}
