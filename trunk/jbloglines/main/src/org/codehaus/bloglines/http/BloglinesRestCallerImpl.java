/*
 * Created on Nov 28, 2004
 *
 * 
 * Copyright (c) Zohar Melamed & William Jones - All rights reserved.
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

import org.codehaus.bloglines.exceptions.BloglinesException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BloglinesRestCallerImpl implements BloglinesRestCaller {
    private final HttpCaller caller;
    private final String encoding;

    public BloglinesRestCallerImpl(HttpCaller caller, String encoding) {
        this.caller = caller;
        this.encoding = encoding;
    }

    public void setCredentials(String name, String password) {
        caller.setCredentials(name, password);
    }

    public String call(String method, String[] args) throws BloglinesException {
        StringBuffer uri = new StringBuffer("http://rpc.bloglines.com/");
        uri.append(method);
        if (args != null && args.length > 0) {
            uri.append("?");
            for (int i = 0; i < args.length; i += 2) {
                try {
                    if (i != 0) {
                        uri.append("&");
                    }
                    uri.append(args[i]).append("=").append(URLEncoder.encode(args[i + 1], encoding));
                } catch (UnsupportedEncodingException e) {
                    throw new BloglinesException("bad encoding: " + encoding, e);
                }
            }
        }
        return caller.doGet(uri.toString());
    }
}