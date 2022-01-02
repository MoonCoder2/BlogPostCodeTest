package com.pierceecom.blog.repository;

import com.pierceecom.blog.model.entities.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<BlogPostEntity, String> {

}
