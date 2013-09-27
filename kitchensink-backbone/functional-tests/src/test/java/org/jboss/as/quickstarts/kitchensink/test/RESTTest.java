package org.jboss.as.quickstarts.kitchensink.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Test for REST API of the application
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class RESTTest {

    private static final String NEW_MEMBER_NAME = "John Doe";
    private static final String NEW_MEMBER_EMAIL = "john.doe@redhat.com";
    private static final String NEW_MEMBER_PHONE = "1234567890";
    private static final String DEFAULT_MEMBER_NAME = "John Smith";

    private static final String API_PATH = "rest/members";

    private final DefaultHttpClient httpClient = new DefaultHttpClient();

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    @Deployment(testable = false)
    public static WebArchive deployment() {
        return Deployments.kitchensink();
    }

    @Test
    @InSequence(1)
    public void testGetMember() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH + "/0"));

        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject member = new JSONObject(responseBody);

        assertEquals(0, member.getInt("id"));
        assertEquals(DEFAULT_MEMBER_NAME, member.getString("name"));
    }

    @Test
    @InSequence(2)
    public void testAddMember() throws Exception {
        HttpPost post = new HttpPost(contextPath.toString() + API_PATH);
        post.setHeader("Content-Type", "application/json");
        String newMemberJSON = new JSONStringer().object()
                .key("name").value(NEW_MEMBER_NAME)
                .key("email").value(NEW_MEMBER_EMAIL)
                .key("phoneNumber").value(NEW_MEMBER_PHONE)
                .endObject().toString();
        post.setEntity(new StringEntity(newMemberJSON));

        HttpResponse response =  httpClient.execute(post);

        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    @InSequence(3)
    public void testGetAllMembers() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH));
        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONArray members = new JSONArray(responseBody);

        assertEquals(2, members.length());

        assertEquals(1, members.getJSONObject(0).getInt("id"));
        assertEquals(NEW_MEMBER_NAME, members.getJSONObject(0).getString("name"));
        assertEquals(NEW_MEMBER_EMAIL, members.getJSONObject(0).getString("email"));
        assertEquals(NEW_MEMBER_PHONE, members.getJSONObject(0).getString("phoneNumber"));

        assertEquals(0, members.getJSONObject(1).getInt("id"));
        assertEquals(DEFAULT_MEMBER_NAME, members.getJSONObject(1).getString("name"));
    }
}
