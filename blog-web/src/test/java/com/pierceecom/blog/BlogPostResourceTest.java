package com.pierceecom.blog;

import com.pierceecom.blog.model.BlogPost;
import com.pierceecom.blog.service.BlogPostService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.Mockito;

import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class BlogPostResourceTest {

    private final EasyRandom easyRandom = new EasyRandom();
    private final BlogPost exampleOfBlogPost = easyRandom.nextObject(BlogPost.class);

    private UriInfo uriInfo;
    private UriBuilder uriBuilder;
    private BlogPostService blogPostService;
    private BlogPostResource blogPostResource;

    @BeforeEach
    public void setUp() {
        uriInfo = Mockito.mock(UriInfo.class);
        uriBuilder = Mockito.mock(UriBuilder.class);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);

        blogPostService = Mockito.mock(BlogPostService.class);
        Validator validator = Mockito.mock(Validator.class);
        blogPostResource = new BlogPostResource(blogPostService, validator);
    }

    @Test
    void getAllBlogPosts() {
        when(blogPostService.getAllBlogPosts()).thenReturn(new ArrayList<>());
        var result = blogPostResource.getAllBlogPosts();

        assertEquals(0, result.size());
        Mockito.verify(blogPostService, times(1)).getAllBlogPosts();
        Mockito.verifyNoMoreInteractions(blogPostService);
    }

    @Test
    void getBlogPost() {
        when(blogPostService.getBlogPosts(exampleOfBlogPost.getId())).thenReturn(Optional.of(exampleOfBlogPost));
        var result = blogPostResource.getBlogPost(exampleOfBlogPost.getId());

        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertEquals(exampleOfBlogPost, result.getEntity());
        Mockito.verify(blogPostService, times(1)).getBlogPosts(exampleOfBlogPost.getId());
        Mockito.verifyNoMoreInteractions(blogPostService);
    }

    @Test
    void getBlogPostNotExist() {
        when(blogPostService.getBlogPosts(anyString())).thenReturn(Optional.empty());
        var result = blogPostResource.getBlogPost(exampleOfBlogPost.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), result.getStatus());
        Mockito.verify(blogPostService, times(1)).getBlogPosts(exampleOfBlogPost.getId());
        Mockito.verifyNoMoreInteractions(blogPostService);
    }

    @Test
    void deleteBlogPost() {
        when(blogPostService.deleteBlogPost(anyString())).thenReturn(Optional.of(exampleOfBlogPost));
        var result = blogPostResource.deleteBlogPost(exampleOfBlogPost.getId());

        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        Mockito.verify(blogPostService, times(1)).deleteBlogPost(exampleOfBlogPost.getId());
        Mockito.verifyNoMoreInteractions(blogPostService);
    }

    @Test
    void deleteBlogPostNotFound() {
        when(blogPostService.deleteBlogPost(anyString())).thenReturn(Optional.empty());
        var result = blogPostResource.deleteBlogPost(exampleOfBlogPost.getId());

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), result.getStatus());
        Mockito.verify(blogPostService, times(1)).deleteBlogPost(exampleOfBlogPost.getId());
        Mockito.verifyNoMoreInteractions(blogPostService);
    }


    @Test
    void postBlogPost() {
        when(blogPostService.createNewBlogPost(exampleOfBlogPost))
                .thenReturn(BlogPostService.CreateNewBlogPostResult.CREATED);
        var result = blogPostResource.postBlogPost(uriInfo, exampleOfBlogPost);

        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());

        Mockito.verify(blogPostService, times(1)).createNewBlogPost(exampleOfBlogPost);
        Mockito.verifyNoMoreInteractions(blogPostService);
    }

    @Test
    void postBlogPostAlreadyExist() {
        when(blogPostService.createNewBlogPost(exampleOfBlogPost))
                .thenReturn(BlogPostService.CreateNewBlogPostResult.BLOG_POST_ALREADY_EXIST);
        var result = blogPostResource.postBlogPost(uriInfo, exampleOfBlogPost);

        assertEquals(Response.Status.CONFLICT.getStatusCode(), result.getStatus());

        Mockito.verify(blogPostService, times(1)).createNewBlogPost(exampleOfBlogPost);
        Mockito.verifyNoMoreInteractions(blogPostService);
    }

    @Test
    void putBlogPost() {
        when(blogPostService.saveBlogPost(exampleOfBlogPost))
                .thenReturn(BlogPostService.SaveBlogPostResult.UPDATED);
        var result = blogPostResource.putBlogPost(uriInfo, exampleOfBlogPost);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), result.getStatus());

        Mockito.verify(blogPostService, times(1)).saveBlogPost(exampleOfBlogPost);
        Mockito.verifyNoMoreInteractions(blogPostService);
    }

    @Test
    void putBlogPostCreate() {
        when(blogPostService.saveBlogPost(exampleOfBlogPost))
                .thenReturn(BlogPostService.SaveBlogPostResult.CREATED);
        var result = blogPostResource.putBlogPost(uriInfo, exampleOfBlogPost);

        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());

        Mockito.verify(blogPostService, times(1)).saveBlogPost(exampleOfBlogPost);
        Mockito.verifyNoMoreInteractions(blogPostService);
    }


}