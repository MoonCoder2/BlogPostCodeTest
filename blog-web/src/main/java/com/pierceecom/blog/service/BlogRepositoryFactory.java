package com.pierceecom.blog.service;

import com.pierceecom.blog.repository.BlogRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class BlogRepositoryFactory {
    @PersistenceContext
    private EntityManager entityManager;
    private BlogRepository blogRepository;

    public BlogRepositoryFactory() {
    }

    @PostConstruct
    private void init() {
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        this.blogRepository = factory.getRepository(BlogRepository.class);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BlogRepository getBlogRepository() {
        return blogRepository;
    }

    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }
}
