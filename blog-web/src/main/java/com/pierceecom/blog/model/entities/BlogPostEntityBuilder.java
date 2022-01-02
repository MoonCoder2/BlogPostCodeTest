package com.pierceecom.blog.model.entities;

public final class BlogPostEntityBuilder {
    private String id;
    private String title;
    private String content;

    private BlogPostEntityBuilder() {
    }

    public static BlogPostEntityBuilder aBlogPostEntity() {
        return new BlogPostEntityBuilder();
    }

    public BlogPostEntityBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public BlogPostEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BlogPostEntityBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public BlogPostEntity build() {
        BlogPostEntity blogPostEntity = new BlogPostEntity();
        blogPostEntity.setId(id);
        blogPostEntity.setTitle(title);
        blogPostEntity.setContent(content);
        return blogPostEntity;
    }
}
