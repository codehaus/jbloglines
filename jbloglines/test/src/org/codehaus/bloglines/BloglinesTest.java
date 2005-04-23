package org.codehaus.bloglines;

import com.sun.syndication.feed.synd.SyndFeed;
import org.codehaus.bloglines.exceptions.BloglinesException;
import org.codehaus.bloglines.http.BloglinesRestCaller;
import org.codehaus.bloglines.unmarshal.ItemsUnmarshaller;
import org.codehaus.bloglines.unmarshal.OutlineUnmarshaller;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.util.Date;

public class BloglinesTest extends MockObjectTestCase {
    private Mock restCallerMock;
    private Mock outlineMarshallMock;
    private Mock itemUnmarshallMock;
    private Bloglines bloglines;

    protected void setUp() throws Exception {
        super.setUp();
        restCallerMock = mock(BloglinesRestCaller.class);
        outlineMarshallMock = mock(OutlineUnmarshaller.class);
        itemUnmarshallMock = mock(ItemsUnmarshaller.class);

        bloglines = new Bloglines((OutlineUnmarshaller) outlineMarshallMock.proxy(),
                                  (ItemsUnmarshaller) itemUnmarshallMock.proxy(),
                                  (BloglinesRestCaller) restCallerMock.proxy());
    }

    public void testSettingCredentialsCallsThroughToTheRestApi() {
        String username = "sakfhaksf";
        String password = "dsakufhasfh";

        restCallerMock.expects(once()).method("setCredentials").with(same(username), same(password));

        bloglines.setCredentials(username, password);

    }

    public void testListSubscriptionsCallsTheRestApiAndUnmarshallsTheResults() throws BloglinesException {
        Outline expectedSubscriptionOutline = new Outline();
        String bloglinesRestResponse = "This is the response from Bloglines";
        restCallerMock.expects(once()).method("call").with(eq("listsubs"), NULL).will(returnValue(bloglinesRestResponse));
        outlineMarshallMock.expects(once()).method("unmarshal").with(same(bloglinesRestResponse)).will(returnValue(expectedSubscriptionOutline));

        Outline subscriptionOutline = bloglines.listSubscriptions();

        assertSame(expectedSubscriptionOutline, subscriptionOutline);
    }

    public void testGetItemsCallsTheRestApiAndUnmarshallsTheResults() throws BloglinesException {
        SyndFeed expectedSyndFeed = (SyndFeed) mock(SyndFeed.class).proxy();
        String bloglinesRestResponse = "This is the response from Bloglines";
        String subscriptionId = "343485";
        String[] parameters = new String[]{"s", subscriptionId, "n", "0"};
        restCallerMock.expects(once()).method("call").with(eq("getitems"), eq(parameters)).will(returnValue(bloglinesRestResponse));
        itemUnmarshallMock.expects(once()).method("unmarshal").with(same(bloglinesRestResponse)).will(returnValue(expectedSyndFeed));

        Date startDate = new Date();
        Outline outline = new Outline();
        outline.setSubscriptionId(subscriptionId);

        SyndFeed syndFeed = bloglines.getItems(outline, false, startDate);

        assertSame(expectedSyndFeed, syndFeed);
    }

    public void testGetItemsCallsTheRestApiPassingThroughTheMarkAsReadFlag() throws BloglinesException {
        SyndFeed expectedSyndFeed = (SyndFeed) mock(SyndFeed.class).proxy();
        String bloglinesRestResponse = "This is the response from Bloglines";
        String subscriptionId = "343485";
        String[] parameters = new String[]{"s", subscriptionId, "n", "1"};
        restCallerMock.expects(once()).method("call").with(eq("getitems"), eq(parameters)).will(returnValue(bloglinesRestResponse));
        itemUnmarshallMock.expects(once()).method("unmarshal").with(same(bloglinesRestResponse)).will(returnValue(expectedSyndFeed));

        Date startDate = new Date();
        Outline outline = new Outline();
        outline.setSubscriptionId(subscriptionId);

        SyndFeed syndFeed = bloglines.getItems(outline, true, startDate);

        assertSame(expectedSyndFeed, syndFeed);
    }

}
