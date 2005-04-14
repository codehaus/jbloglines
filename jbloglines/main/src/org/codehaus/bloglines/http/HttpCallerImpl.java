/*
 * Created on 10-Dec-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
