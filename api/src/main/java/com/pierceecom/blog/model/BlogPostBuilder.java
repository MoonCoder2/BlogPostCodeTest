package com.pierceecom.blog.model;

public final class BlogPostBuilder {
    private String id;
    private String title;
    private String content;

    private BlogPostBuilder() {
    }

    public static BlogPostBuilder aBlogPost() {
        return new BlogPostBuilder();
    }

    public BlogPostBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public BlogPostBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BlogPostBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public BlogPost build() {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(id);
        blogPost.setTitle(title);
        blogPost.setContent(content);
        return blogPost;
    }
}
