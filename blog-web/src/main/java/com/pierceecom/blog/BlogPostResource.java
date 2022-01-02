package com.pierceecom.blog;

import com.pierceecom.blog.model.BlogPost;
import com.pierceecom.blog.service.BlogPostService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Tag(name = "post", description = "Example of REST Endpoints")
@Path("posts")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class BlogPostResource {

    private final BlogPostService blogService;
    private final Validator validator;

    @Inject
    public BlogPostResource(BlogPostService blogService, Validator validator) {
        this.blogService = blogService;
        this.validator = validator;
    }

    @GET
    @Operation(description = "Returns all posts")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(implementation = BlogPost[].class)))
    })
    public List<BlogPost> getAllBlogPosts() {
        return blogService.getAllBlogPosts();
    }


    @GET
    @Path("{id}")
    @Operation(description = "Returns a single post")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Post available", content = @Content(schema = @Schema(implementation = BlogPost.class))),
            @APIResponse(responseCode = "204", description = "No post exist")
    })
    public Response getBlogPost(@PathParam("id") @NotBlank String Id) {
        Optional<BlogPost> blogPost = blogService.getBlogPosts(Id);
        if (blogPost.isPresent()) {
            return Response.ok(blogPost.get()).build();
        } else {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Deletes a post")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "successfully deleted"),
            @APIResponse(responseCode = "404", description = "Post not found")
    })
    public Response deleteBlogPost(@PathParam("id") @NotBlank String Id) {
        Optional<BlogPost> blogPost = blogService.deleteBlogPost(Id);
        if (blogPost.isPresent()) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Operation(description = "Add a new post")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "OK of post"),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "409", description = "Id already exist")
    })
    public Response postBlogPost(@Context UriInfo uriInfo, @Valid BlogPost blogPost) {

        BlogPostService.CreateNewBlogPostResult createNewBlogPostResult = blogService.createNewBlogPost(blogPost);

        if (createNewBlogPostResult == BlogPostService.CreateNewBlogPostResult.CREATED) {
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(blogPost.getId());
            return Response.created(uriBuilder.build()).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @PUT
    @Operation(description = "Updates a post")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Post created"),
            @APIResponse(responseCode = "204", description = "Post has been updated")
    })
    public Response putBlogPost(@Context UriInfo uriInfo, @Valid BlogPost blogPost) {

        var result = blogService.saveBlogPost(blogPost);
        if (result == BlogPostService.SaveBlogPostResult.CREATED) {
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(blogPost.getId());
            return Response.created(uriBuilder.build()).build();
        } else {
            return Response.noContent().build();
        }
    }


}
