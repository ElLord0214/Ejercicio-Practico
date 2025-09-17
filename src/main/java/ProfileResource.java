
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@Path("/profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileResource {

    @GET
    public List<ProfileResponse> list() {
        return Profile.listAll()
                      .stream()
                      .map(p -> ProfileResponse.from((Profile) p))
                      .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") java.util.UUID id) {
        Profile p = Profile.findById(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ProfileResponse.from(p)).build();
    }

    @POST
    @Transactional
    public Response create(@Valid ProfileRequest req, @Context UriInfo uriInfo) {
        // Pre-check duplicado por email:
        if (Profile.find("email", req.email.toLowerCase()).firstResultOptional().isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Email already exists")).build();
        }

        Profile p = new Profile();
        p.firstName = req.firstName;
        p.lastName = req.lastName;
        p.email = req.email.toLowerCase();
        p.cellphone = req.cellphone;
        p.persist();

        URI uri = uriInfo.getAbsolutePathBuilder().path(p.id.toString()).build();
        return Response.created(uri).entity(ProfileResponse.from(p)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") java.util.UUID id, @Valid ProfileRequest req) {
        Profile p = Profile.findById(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Validar duplicado: si existe otro registro con ese email
        Optional<Profile> byEmail = Profile.find("email", req.email.toLowerCase()).firstResultOptional();
        if (byEmail.isPresent() && !byEmail.get().id.equals(id)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Email already exists")).build();
        }

        p.firstName = req.firstName;
        p.lastName = req.lastName;
        p.email = req.email.toLowerCase();
        p.cellphone = req.cellphone;
        // updatedAt se actualiza por @PreUpdate al commit de la transacción
        return Response.ok(ProfileResponse.from(p)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") java.util.UUID id) {
        Profile p = Profile.findById(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        p.delete();
        return Response.noContent().build();
    }
}

