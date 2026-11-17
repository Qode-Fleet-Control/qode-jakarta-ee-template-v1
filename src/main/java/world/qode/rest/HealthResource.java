package world.qode.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// The fleet's health check (fleet.conf HEALTH_PATH), served by the app itself rather than by
// Liberty's mpHealth feature: mpHealth's /health is a separate web module, so the app's
// RequestLogFilter would never see it, and health checks must be logged like any request.
@Path("/health")
@ApplicationScoped
public class HealthResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject health() {
        return Json.createObjectBuilder().add("status", "UP").build();
    }
}
