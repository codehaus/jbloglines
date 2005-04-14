package org.codehaus.bloglines;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.bloglines.exceptions.BloglinesException;
import org.codehaus.bloglines.http.BloglinesRestCaller;
import org.codehaus.bloglines.unmarshall.ItemsUnmarshall;
import org.codehaus.bloglines.unmarshall.OutlineUnmarshal;

import com.sun.syndication.feed.synd.SyndFeed;

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
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 *  
 */

/**
 * @author Zohar
 *
 */
public class Bloglines {
	private static final String GETITEMS = "getitems";
	private static final String LISTSUBS = "listsubs";
	private OutlineUnmarshal outlineUnmarshal;
	private ItemsUnmarshall itemsUnmarshall;
	private BloglinesRestCaller restCaller;


	
	/**
	 * @param outlineUnmarshal
	 * @param itemsUnmarshall
	 * @param restCaller
	 */
	public Bloglines(OutlineUnmarshal outlineUnmarshal,
			ItemsUnmarshall itemsUnmarshall, BloglinesRestCaller restCaller) {
		this.outlineUnmarshal = outlineUnmarshal;
		this.itemsUnmarshall = itemsUnmarshall;
		this.restCaller = restCaller;
	}
	/**
	 * 
	 */
	public Bloglines() {
		outlineUnmarshal = new OutlineUnmarshal();
		itemsUnmarshall = new ItemsUnmarshall();
		restCaller = new BloglinesRestCaller();
	}

	/**
	 * @param name bloglines account name
	 * @param password bloglines account password
	 */
	public void setCredentials(String name, String password) {
		restCaller.setCredentials(name,password);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	public Outline listSubscriptions() throws BloglinesException{
		String outline = restCaller.call(LISTSUBS, null);
		return outlineUnmarshal.unmarshal(outline);
	}
	
	public SyndFeed getItems(Outline containingOutline,boolean markasRead,Date startDate) throws BloglinesException{
		String items = restCaller.call(GETITEMS, new String[]{"s",containingOutline.getSubId(),"n",markasRead?"1":"0"});
		return itemsUnmarshall.unmarshal(items);
		
	}
}
