package org.codehaus.bloglines.http;

import org.codehaus.bloglines.exceptions.BloglinesException;

public interface BloglinesRestCaller {
    void setCredentials(String name, String password);

    String call(String method, String[] args) throws BloglinesException;
}
