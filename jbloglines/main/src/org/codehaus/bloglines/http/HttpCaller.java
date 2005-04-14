/*
 * Created on 10-Dec-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.codehaus.bloglines.http;

import org.codehaus.bloglines.exceptions.BloglinesException;

/**
 * @author Zohar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface HttpCaller {
	/**
	 * @param uri
	 * @return
	 * @throws BloglinesException
	 */
	public String doGet(StringBuffer uri) throws BloglinesException;

	/**
	 * @param name
	 * @param password
	 */
	public void setCredentials(String name, String password);
}