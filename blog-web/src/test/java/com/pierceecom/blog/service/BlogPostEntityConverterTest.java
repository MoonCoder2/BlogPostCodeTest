package com.pierceecom.blog.service;

import com.pierceecom.blog.model.BlogPost;
import com.pierceecom.blog.model.entities.BlogPostEntity;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlogPostEntityConverterTest {
    EasyRandom easyRandom = new EasyRandom();
    BlogPostEntityConverter blogPostEntityConverter = new BlogPostEntityConverter();

    @Test
    public void convertToBlogPost() {
        BlogPostEntity BlogPostEntity = easyRandom.nextObject(BlogPostEntity.class);
        BlogPost blogPost = blogPostEntityConverter.convertToBlogPost(BlogPostEntity);

        assertEquals(BlogPostEntity.getId(), blogPost.getId());
        assertEquals(BlogPostEntity.getTitle(), blogPost.getTitle());
        assertEquals(BlogPostEntity.getContent(), blogPost.getContent());
    }

    @Test
    public void convertToBlogPostEntity() {
        BlogPost blogPost = easyRandom.nextObject(BlogPost.class);
        BlogPostEntity blogPostEntity = blogPostEntityConverter.convertToBlogPostEntity(blogPost);

        assertEquals(blogPost.getId(), blogPostEntity.getId());
        assertEquals(blogPost.getTitle(), blogPostEntity.getTitle());
        assertEquals(blogPost.getContent(), blogPostEntity.getContent());
    }

    @Test
    public void convertToBlogPostNullTest() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            blogPostEntityConverter.convertToBlogPost(null);
        });

        Assertions.assertEquals("blogEntryEntity must not be null", exception.getMessage());
    }

    @Test
    public void convertToBlogPostEntityNullTest() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            blogPostEntityConverter.convertToBlogPostEntity(null);
        });

        Assertions.assertEquals("blogPost must not be null", exception.getMessage());
    }

}