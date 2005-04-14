/*
 * Created on 10-Dec-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.codehaus.bloglines;

import junit.framework.TestCase;

import org.codehaus.bloglines.exceptions.BloglinesException;
import org.codehaus.bloglines.http.BloglinesRestCaller;
import org.codehaus.bloglines.unmarshall.ItemsUnmarshall;
import org.codehaus.bloglines.unmarshall.OutlineUnmarshal;

import com.sun.syndication.feed.synd.SyndFeed;

/**
 * @author MELAMEDZ
 *
 * 
 */
public class BloglinesTestCase extends TestCase {
	class MockOutlineUnmarshall extends OutlineUnmarshal{
		public boolean unmarshallCalled ;
		/* (non-Javadoc)
		 * @see org.codehaus.bloglines.unmarshall.OutlineUnmarshal#unmarshal(java.lang.String)
		 */
		public Outline unmarshal(String outlines) {
			unmarshallCalled = true;
			return super.unmarshal(outlines);
		}
	};
	
	class MockItemUnmarshall extends ItemsUnmarshall{
		public  boolean umarshallCalled;

		/* (non-Javadoc)
		 * @see org.codehaus.bloglines.unmarshall.ItemsUnmarshall#unmarshal(java.lang.String)
		 */
		public SyndFeed unmarshal(String items) throws BloglinesException {
			umarshallCalled = true;
			return super.unmarshal(items);
		}
	};
	
	class MockRestCaller extends BloglinesRestCaller{
		private String ret;
		public String method;
		public String[]args;
		
		/**
		 * @param ret
		 */
		public MockRestCaller(String ret) {
			this.ret = ret;
		}
		/* (non-Javadoc)
		 * @see org.codehaus.bloglines.http.BloglinesRestCaller#call(java.lang.String, java.lang.String[])
		 */
		public String call(String method, String[] args)
				throws BloglinesException {
			// TODO Auto-generated method stub
			return super.call(method, args);
		}
	};
	public void testListSubs() {
		
		new Bloglines(new MockOutlineUnmarshall(),
					  new MockItemUnmarshall(),
					  new MockRestCaller("ff"));
		
	}
}
