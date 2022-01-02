package com.pierceecom.blog;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(
        title = "Simple blog post API",
        version = "1.0.0",
        description = "This is the definition of the API for code test as Pierce AB"
),
        servers = {
                @Server(url = "http://localhost:8080/blog-web", description = "localhost")
        }
)
@ApplicationPath("/")
public class JAXRSConfiguration extends Application {

}
