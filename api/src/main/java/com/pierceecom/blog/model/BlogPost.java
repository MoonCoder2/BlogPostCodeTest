package com.pierceecom.blog.model;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "Post")
public class BlogPost {
    @NotBlank
    private String id;
    private String title;
    private String content;

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogPost blogPost = (BlogPost) o;
        return getId().equals(blogPost.getId()) && Objects.equals(getTitle(), blogPost.getTitle()) && Objects.equals(getContent(), blogPost.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getContent());
    }
}
