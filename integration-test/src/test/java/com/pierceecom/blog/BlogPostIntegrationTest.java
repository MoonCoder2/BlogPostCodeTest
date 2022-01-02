package com.pierceecom.blog;

//import com.pierceecom.blog.model.Bloggy;

import com.pierceecom.blog.model.BlogPost;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.context.annotation.Profile;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "run.integration.tests", matches = "true")
public class BlogPostIntegrationTest extends BlogPostBaseTestClass {


    public BlogPostIntegrationTest() throws URISyntaxException {
    }

    @Test
    public void getBlogPostsXML() {
        Response response = blogPostsTarget.request(MediaType.APPLICATION_XML).get();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getBlogPostsJSON() {
        Response response = blogPostsTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getBlogPostsNotSupportedMediaType() {
        Response response = blogPostsTarget.request(MediaType.TEXT_PLAIN).get();
        assertEquals(406, response.getStatus());
    }


    @Test
    public void addPost() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);
        Response response = post(randomBlogPost);
        assertEquals(201, response.getStatus());
        assertEquals(getBlogPostLocation(randomBlogPost.getId()), response.getHeaders().getFirst("Location"));
    }

    @Test
    public void postDuplicateNoChange() {
        BlogPost randomBlogPost1 = easyRandom.nextObject(BlogPost.class);
        BlogPost randomBlogPost2 = easyRandom.nextObject(BlogPost.class);
        randomBlogPost2.setId(randomBlogPost1.getId());

        Response response1 = post(randomBlogPost1);
        Response response2 = post(randomBlogPost2);

        assertEquals(201, response1.getStatus());
        assertEquals(409, response2.getStatus());

        Response getPostOnServerResponse = get(randomBlogPost1.getId());
        assertEquals(200, getPostOnServerResponse.getStatus());
        BlogPost blogPostOnServer = getPostOnServerResponse.readEntity(BlogPost.class);
        assertEquals(randomBlogPost1, blogPostOnServer);
    }

    @Test
    public void postValidationIdCannotBeBlank() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);
        randomBlogPost.setId("");
        Response response = post(randomBlogPost);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void postValidationIdCannotBeNull() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);
        randomBlogPost.setId(null);
        Response response = post(randomBlogPost);
        assertEquals(400, response.getStatus());
    }


    @Test
    public void postAndGetBlogPost() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);
        Response postResponse = post(randomBlogPost);
        assertEquals(201, postResponse.getStatus());

        BlogPost blogPostInServer = get(randomBlogPost.getId()).readEntity(BlogPost.class);

        assertEquals(randomBlogPost, blogPostInServer);
    }

    @Test
    public void getUnknownId() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);

        Response response = get(randomBlogPost.getId());

        assertEquals(204, response.getStatus());
    }

    @Test
    public void deleteUnknownId() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);

        Response response = delete(randomBlogPost.getId());
        assertEquals(404, response.getStatus());
    }

    @Test
    public void putUnknownId() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);

        Response response = put(randomBlogPost);

        assertEquals(201, response.getStatus());
        assertEquals(getBlogPostLocation(randomBlogPost.getId()), response.getHeaders().getFirst("Location"));
    }

    @Test
    public void putValidationIdCannotBeBlank() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);
        randomBlogPost.setId("");
        Response response = put(randomBlogPost);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void pulValidationIdCannotBeNull() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);
        randomBlogPost.setId(null);
        Response response = put(randomBlogPost);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void modifyBlogPost() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);
        BlogPost randomBlogPostModified = easyRandom.nextObject(BlogPost.class);
        randomBlogPostModified.setId(randomBlogPost.getId());

        Response responsePost = post(randomBlogPost);
        assertEquals(201, responsePost.getStatus());

        Response responsePut = put(randomBlogPostModified);
        assertEquals(204, responsePut.getStatus());

        Response responseGet = get(randomBlogPost.getId());
        assertEquals(200, responseGet.getStatus());

        assertEquals(randomBlogPostModified, responseGet.readEntity(BlogPost.class));
    }

    @Test
    public void deleteBlogPost() {
        BlogPost randomBlogPost = easyRandom.nextObject(BlogPost.class);

        var postResponse = post(randomBlogPost);
        assertEquals(201, postResponse.getStatus());

        var getResponse = get(randomBlogPost.getId());
        assertEquals(200, getResponse.getStatus());

        Response deleteResponse = delete(randomBlogPost.getId());
        assertEquals(200, deleteResponse.getStatus());

        var getResponseAfterDelete = get(randomBlogPost.getId());
        assertEquals(204, getResponseAfterDelete.getStatus());
    }


    @Test
    public void testGetALL() {
        BlogPost randomBlogPost1 = easyRandom.nextObject(BlogPost.class);
        BlogPost randomBlogPost2 = easyRandom.nextObject(BlogPost.class);
        BlogPost randomBlogPost3 = easyRandom.nextObject(BlogPost.class);

        post(randomBlogPost1);
        post(randomBlogPost2);

        Set<BlogPost> blogPosts = Arrays.stream(getAll().readEntity(BlogPost[].class)).collect(Collectors.toSet());
        assertTrue(blogPosts.contains(randomBlogPost1));
        assertTrue(blogPosts.contains(randomBlogPost2));
        assertFalse(blogPosts.contains(randomBlogPost3));
    }

}
