/*
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
 * http://jbloglines.codehaus.org/
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
package org.codehaus.bloglines.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.bloglines.exceptions.BloglinesException;

/**
 * @author Zohar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HttpCallerImpl implements HttpCaller {
	private static final String BLOGLINES_RPC_URI = "rpc.bloglines.com";
	private static final String BLOGLINES_REALM = "Bloglines RPC";
	private HttpClient client;

	public void setCredentials(String name,String password){
		client = new HttpClient();
		client.getState().setCredentials(BLOGLINES_REALM,BLOGLINES_RPC_URI,
		new UsernamePasswordCredentials(name, password));
	}
	/* (non-Javadoc)
	 * @see org.codehaus.bloglines.HttpCaller#doGet(java.lang.StringBuffer)
	 */
	public String doGet(StringBuffer uri) throws BloglinesException {
        GetMethod get = null;
		int status;
		try {
			get = new GetMethod(uri.toString());
			get.setDoAuthentication( true );
			status = client.executeMethod( get );
			if(status != HttpStatus.SC_OK){
				throw new BloglinesException("failed in call :"+uri+" http error: "
						+HttpStatus.getStatusText(status) );
			}
			return get.getResponseBodyAsString();
		} catch (HttpException e) {
			throw new BloglinesException("failed in call :"+uri,e);
		} catch (IOException e) {
			throw new BloglinesException("failed in call :"+uri,e);
		}finally {
            get.releaseConnection();
        }}

}
