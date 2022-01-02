package com.pierceecom.blog.service;

import com.pierceecom.blog.model.BlogPost;
import com.pierceecom.blog.model.entities.BlogPostEntity;
import com.pierceecom.blog.repository.BlogRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class BlogPostServiceTest {

    private final BlogPostEntityConverter blogPostEntityConverter = new BlogPostEntityConverter();
    private final EasyRandom easyRandom = new EasyRandom();
    private final BlogPostEntity blogPostEntity = easyRandom.nextObject(BlogPostEntity.class);
    private final BlogPost blogPost =blogPostEntityConverter.convertToBlogPost(blogPostEntity);
    private final String BLOG_POST_ID = blogPostEntity.getId();
    private final String BLOG_POST_ID_THAT_DO_NOT_EXIST = BLOG_POST_ID + "_BLOG_POST_ID_THAT_DO_NOT_EXIST";

    BlogPostService blogPostService;
    BlogRepository blogRepository;

    @BeforeEach
    void setUp() {
        blogRepository = Mockito.mock(BlogRepository.class);
        BlogRepositoryFactory blogRepositoryFactory = Mockito.mock(BlogRepositoryFactory.class);
        Mockito.when(blogRepositoryFactory.getBlogRepository()).thenReturn(blogRepository);
        blogPostService = new BlogPostService(blogPostEntityConverter, blogRepositoryFactory);
    }

    @Test
    void getAllBlogPosts() {
        Mockito.when(blogRepository.findAll()).thenReturn(List.of(blogPostEntity));

        List<BlogPost> blogPosts = blogPostService.getAllBlogPosts();

        assertEquals(blogPosts.size(), 1);
        assertEquals(blogPosts.get(0).getId(), blogPostEntity.getId());
        Mockito.verify(blogRepository, times(1)).findAll();
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void getBlogPost() {
        Mockito.when(blogRepository.findById(BLOG_POST_ID)).thenReturn(Optional.of(blogPostEntity));

        Optional<BlogPost> blogPosts = blogPostService.getBlogPosts(BLOG_POST_ID);

        assertTrue(blogPosts.isPresent());
        assertEquals(blogPosts.get().getId(), blogPostEntity.getId());
        Mockito.verify(blogRepository, times(1)).findById(BLOG_POST_ID);
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void getBlogPostNullTest() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            blogPostService.getBlogPosts(null);
        });

        Assertions.assertEquals("Id must not be null", exception.getMessage());
    }

    @Test
    void getBlogPostsThatDoNotExist() {
        Mockito.when(blogRepository.findById(BLOG_POST_ID_THAT_DO_NOT_EXIST)).thenReturn(Optional.empty());

        Optional<BlogPost> blogPosts = blogPostService.getBlogPosts(BLOG_POST_ID_THAT_DO_NOT_EXIST);

        assertTrue(blogPosts.isEmpty());
        Mockito.verify(blogRepository, times(1)).findById(BLOG_POST_ID_THAT_DO_NOT_EXIST);
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void deleteBlogPostThatDoesNotExist() {
        Mockito.when(blogRepository.findById(BLOG_POST_ID_THAT_DO_NOT_EXIST)).thenReturn(Optional.empty());

        Optional<BlogPost> blogPosts = blogPostService.deleteBlogPost(BLOG_POST_ID_THAT_DO_NOT_EXIST);

        assertTrue(blogPosts.isEmpty());
        Mockito.verify(blogRepository, times(1)).findById(BLOG_POST_ID_THAT_DO_NOT_EXIST);
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void deleteBlogPost() {
        Mockito.when(blogRepository.findById(BLOG_POST_ID)).thenReturn(Optional.of(blogPostEntity));

        Optional<BlogPost> blogPosts = blogPostService.deleteBlogPost(BLOG_POST_ID);

        assertTrue(blogPosts.isPresent());
        Mockito.verify(blogRepository, times(1)).findById(BLOG_POST_ID);
        Mockito.verify(blogRepository, times(1)).deleteById(BLOG_POST_ID);
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void deleteBlogPostNull() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            blogPostService.deleteBlogPost(null);
        });

        Assertions.assertEquals("Id must not be null", exception.getMessage());
    }

    @Test
    void createNewBlogPost() {
        BlogPostService.CreateNewBlogPostResult createNewBlogPostResult = blogPostService.createNewBlogPost(blogPost);

        assertEquals(BlogPostService.CreateNewBlogPostResult.CREATED,createNewBlogPostResult);
        Mockito.verify(blogRepository, times(1)).existsById(BLOG_POST_ID);
        Mockito.verify(blogRepository, times(1)).save(any(BlogPostEntity.class));
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void createNewBlogPostConflict() {
        Mockito.when(blogRepository.existsById(BLOG_POST_ID)).thenReturn(true);

        BlogPostService.CreateNewBlogPostResult createNewBlogPostResult = blogPostService.createNewBlogPost(blogPost);

        assertEquals(BlogPostService.CreateNewBlogPostResult.BLOG_POST_ALREADY_EXIST,createNewBlogPostResult);
        Mockito.verify(blogRepository, times(1)).existsById(BLOG_POST_ID);
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void createNewBlogPostNullTest() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            blogPostService.createNewBlogPost(null);
        });

        Assertions.assertEquals("blogPost must not be null", exception.getMessage());
    }

    @Test
    void saveBlogPost() {
        Mockito.when(blogRepository.existsById(BLOG_POST_ID)).thenReturn(false);

        BlogPostService.SaveBlogPostResult saveBlogPostResult = blogPostService.saveBlogPost(blogPost);

        assertEquals(BlogPostService.SaveBlogPostResult.CREATED,saveBlogPostResult);
        Mockito.verify(blogRepository, times(1)).existsById(BLOG_POST_ID);
        Mockito.verify(blogRepository, times(1)).save(any(BlogPostEntity.class));
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void saveBlogPostAlreadyExist() {
        Mockito.when(blogRepository.existsById(BLOG_POST_ID)).thenReturn(true);

        BlogPostService.SaveBlogPostResult saveBlogPostResult = blogPostService.saveBlogPost(blogPost);

        assertEquals(BlogPostService.SaveBlogPostResult.UPDATED,saveBlogPostResult);
        Mockito.verify(blogRepository, times(1)).existsById(BLOG_POST_ID);
        Mockito.verify(blogRepository, times(1)).save(any(BlogPostEntity.class));
        Mockito.verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void saveBlogPostNullTest() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            blogPostService.saveBlogPost(null);
        });

        Assertions.assertEquals("blogPost must not be null", exception.getMessage());
    }
}