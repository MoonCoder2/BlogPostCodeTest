package com.pierceecom.blog;

import com.pierceecom.blog.model.BlogPost;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

public class BlogPostBaseTestClass {
    protected static final String SERVER_TARGET = "http://localhost:8080";
    protected static final String POSTS_ENDPOINT = "/blog-web/posts/";
    protected final URI POSTS_ENDPOINT_URI;
    protected final EasyRandom easyRandom;

    protected final Invocation.Builder postXMLInvocationBuilder;
    protected final Invocation.Builder postJsonInvocationBuilder;
    protected final Invocation.Builder postUnsupportedTypeInvocationBuilder;
    protected final WebTarget blogPostsTarget;

    public BlogPostBaseTestClass() throws URISyntaxException {
        easyRandom = new EasyRandom(
                new EasyRandomParameters()
                        .seed(new Random().nextLong())
                        .stringLengthRange(60, 60)
        );

        POSTS_ENDPOINT_URI = new URI(SERVER_TARGET).resolve(POSTS_ENDPOINT);
        Client client = ClientBuilder.newClient();
        blogPostsTarget = client.target(POSTS_ENDPOINT_URI);

        postXMLInvocationBuilder = blogPostsTarget.request(MediaType.APPLICATION_XML);
        postJsonInvocationBuilder = blogPostsTarget.request(MediaType.APPLICATION_JSON);
        postUnsupportedTypeInvocationBuilder = blogPostsTarget.request(MediaType.TEXT_HTML);
    }


    protected Response post(BlogPost blogPost) {
        return postJsonInvocationBuilder.post(Entity.entity(blogPost, MediaType.APPLICATION_JSON));
    }

    protected Response put(BlogPost blogPost) {
        return postJsonInvocationBuilder.put(Entity.entity(blogPost, MediaType.APPLICATION_JSON));
    }

    protected Response get(String blogPostId) {
        return blogPostsTarget
                .path(blogPostId)
                .request()
                .get();
    }

    protected Response delete(String blogPostId) {
        return blogPostsTarget
                .path(blogPostId)
                .request()
                .delete();
    }

    protected Response getAll() {
        return blogPostsTarget
                .request()
                .get();
    }

    protected String getBlogPostLocation(String blogPostId) {
        return POSTS_ENDPOINT_URI.resolve(blogPostId).toString();
    }


}
