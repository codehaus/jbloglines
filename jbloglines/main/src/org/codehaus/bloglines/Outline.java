package org.codehaus.bloglines;
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
 * @author Sarah
 *
 */
public class Outline {

	private Outline[] children;
	private String title;
	private String type;
	private String htmlUrl;
	private String xmlUrl;
	private int unread;
	private String subId;
	private boolean ignore;


	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return
	 */
	public boolean isFeed() {
		return "rss".equals(type);
	}

	/**
	 * @return
	 */
	public Outline[] getChildren() {
		return children;
	}

	/**
	 * @param children The children to set.
	 */
	public void setChidlren(Outline[] children) {
		this.children = children;
	}

	/**
	 * @param attribute
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public String getHtmlUrl() {
		return htmlUrl;
	}
	
	/**
	 * @param htmlUrl The htmlUrl to set.
	 */
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	/**
	 * @return
	 */
	public String getXmlUrl() {
		return xmlUrl;
	}
	
	/**
	 * @param xmlUrl The xmlUrl to set.
	 */
	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}

	/**
	 * @return
	 */
	public int getUnread() {
		return unread;
	}
	
	/**
	 * @param unread The unread to set.
	 */
	public void setUnread(int unread) {
		this.unread = unread;
	}

	/**
	 * @return
	 */
	public String getSubId() {
		return subId;
	}
	
	/**
	 * @param subId The subId to set.
	 */
	public void setSubId(String subId) {
		this.subId = subId;
	}

	/**
	 * 
	 */
	public boolean getIgnore() {
		return ignore;
	}
	/**
	 * @param ignore
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
}
