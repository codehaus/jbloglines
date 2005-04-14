/*
 * Created on Nov 28, 2004
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

import org.codehaus.bloglines.unmarshall.OutlineUnmarshal;

import junit.framework.TestCase;

/**
 * @author Sarah
 *
 */
public class OutlineUnmarshalTestCase extends TestCase {
	private static final String OUTLINE_OPML = "<?xml version='1.0' encoding='utf-8'?>"+
	"<opml version='1.0'>"+
	"<head>"+
	    "<title>Bloglines Subscriptions</title>"+
	    "<dateCreated>Sun, 28 Nov 2004 10:45:05 GMT</dateCreated>"+
	    "<ownerName>zohar@codehaus.org</ownerName>"+
	"</head>"+
	"<body>"+
	  	"<outline title='Subscriptions'>"+
		    "<outline title='Bloglines | News' htmlUrl='http://www.bloglines.com' type='rss' xmlUrl='http://www.bloglines.com/rss/about/news'  BloglinesSubId='5259174'  BloglinesUnread='8' BloglinesIgnore='0' />"+
		   " <outline title='Stuff' BloglinesSubId='5259181' BloglinesIgnore='0'>"+
		  "      <outline title='Boing Boing' htmlUrl='http://www.boingboing.net/' type='rss' xmlUrl='http://boingboing.net/rss.xml'  BloglinesSubId='5259182'  BloglinesUnread='36' BloglinesIgnore='0' />"+
		 "   </outline>"+
		"</outline>"+
"</body>"+
"</opml>";

	public void testOutlineUnmarshall() {
		OutlineUnmarshal ou = new OutlineUnmarshal();
		Outline topLevel = ou.unmarshal(OUTLINE_OPML);
		assertEquals("Subscriptions",topLevel.getTitle());
		assertEquals(false,topLevel.isFeed());
		
		Outline[] outlines = topLevel.getChildren();
		assertEquals(2,outlines.length);
		Outline stuff = outlines[1];
		assertEquals("Stuff",stuff.getTitle());
		assertEquals(1,stuff.getChildren().length);
		Outline boingboing = stuff.getChildren()[0];
		assertEquals("Boing Boing",boingboing.getTitle());
		assertTrue(boingboing.isFeed());
		assertEquals("http://www.boingboing.net/",boingboing.getHtmlUrl());
		assertEquals("http://boingboing.net/rss.xml",boingboing.getXmlUrl());
		assertTrue(boingboing.getUnread()>=26);
		assertEquals(boingboing.getSubId(),"5259182");
		assertFalse(boingboing.getIgnore());
	}		
}
