package com.pierceecom.blog.service;

import com.pierceecom.blog.model.BlogPost;
import com.pierceecom.blog.model.BlogPostBuilder;
import com.pierceecom.blog.model.entities.BlogPostEntity;
import com.pierceecom.blog.model.entities.BlogPostEntityBuilder;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Stateless
@LocalBean
public class BlogPostEntityConverter {

    public BlogPost convertToBlogPost(@NotNull BlogPostEntity blogEntryEntity) {
        Objects.requireNonNull(blogEntryEntity, "blogEntryEntity must not be null");
        return BlogPostBuilder.aBlogPost()
                .withId(blogEntryEntity.getId())
                .withTitle(blogEntryEntity.getTitle())
                .withContent(blogEntryEntity.getContent())
                .build();
    }

    public BlogPostEntity convertToBlogPostEntity(@NotNull BlogPost blogPost) {
        Objects.requireNonNull(blogPost, "blogPost must not be null");
        return BlogPostEntityBuilder.aBlogPostEntity()
                .withId(blogPost.getId())
                .withTitle(blogPost.getTitle())
                .withContent(blogPost.getContent())
                .build();
    }
}
