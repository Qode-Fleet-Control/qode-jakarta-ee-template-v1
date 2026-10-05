package world.qode.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@ApplicationScoped
public class RootResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject root() {
        return Json.createObjectBuilder()
                .add("app", "jakarta-ee-template")
                .add("status", "ok")
                .build();
    }
}
