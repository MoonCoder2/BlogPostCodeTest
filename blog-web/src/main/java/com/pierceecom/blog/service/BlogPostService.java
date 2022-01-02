package com.pierceecom.blog.service;

import com.pierceecom.blog.model.BlogPost;
import com.pierceecom.blog.repository.BlogRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class BlogPostService {
    private final static Logger LOGGER = Logger.getLogger(BlogPostService.class.getName());
    private BlogPostEntityConverter blogPostEntityConverter;
    private BlogRepositoryFactory blogRepositoryConfig;
    private BlogRepository blogRepository;

    public BlogPostService() {
    }
    @Inject
    public BlogPostService(BlogPostEntityConverter blogPostEntityConverter, BlogRepositoryFactory blogRepositoryConfig) {
        this.blogPostEntityConverter = blogPostEntityConverter;
        this.blogRepositoryConfig = blogRepositoryConfig;
        this.blogRepository = blogRepositoryConfig.getBlogRepository();
    }

    public List<BlogPost> getAllBlogPosts() {
        return blogRepository.findAll()
                .stream()
                .map(blogPostEntityConverter::convertToBlogPost)
                .collect(Collectors.toList());
    }

    public Optional<BlogPost> getBlogPosts(@NotBlank String id) {
        Objects.requireNonNull(id, "Id must not be null");
        return blogRepository.findById(id)
                .map(blogPostEntityConverter::convertToBlogPost);
    }

    public Optional<BlogPost> deleteBlogPost(@NotNull String id) {
        Objects.requireNonNull(id, "Id must not be null");
        var blogPost = blogRepository.findById(id).map(blogPostEntityConverter::convertToBlogPost);
        if (blogPost.isPresent()) {
            blogRepository.deleteById(id);
        }
        return blogPost;

    }

    public CreateNewBlogPostResult createNewBlogPost(@NotNull BlogPost blogPost) {
        Objects.requireNonNull(blogPost, "blogPost must not be null");
        if (blogRepository.existsById(blogPost.getId())) {
            return CreateNewBlogPostResult.BLOG_POST_ALREADY_EXIST;
        } else {
            blogRepository.save(blogPostEntityConverter.convertToBlogPostEntity(blogPost));
            return CreateNewBlogPostResult.CREATED;
        }
    }

    public SaveBlogPostResult saveBlogPost(@NotNull BlogPost blogPost) {
        Objects.requireNonNull(blogPost, "blogPost must not be null");
        SaveBlogPostResult saveBlogPostResult = null;

        if (blogRepository.existsById(blogPost.getId())) {
            saveBlogPostResult = SaveBlogPostResult.UPDATED;
        } else {
            saveBlogPostResult = SaveBlogPostResult.CREATED;
        }
        blogRepository.save(blogPostEntityConverter.convertToBlogPostEntity(blogPost)); //UPSERT
        return saveBlogPostResult;
    }

    public enum SaveBlogPostResult {CREATED, UPDATED}

    public enum CreateNewBlogPostResult {CREATED, BLOG_POST_ALREADY_EXIST}
}
