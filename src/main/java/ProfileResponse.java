import java.time.OffsetDateTime;
import java.util.UUID;

public class ProfileResponse {
    public UUID id;
    public String firstName;
    public String lastName;
    public String email;
    public String cellphone;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;

    public static ProfileResponse from(Profile p) {
        ProfileResponse r = new ProfileResponse();
        r.id = p.id;
        r.firstName = p.firstName;
        r.lastName = p.lastName;
        r.email = p.email;
        r.cellphone = p.cellphone;
        r.createdAt = p.createdAt;
        r.updatedAt = p.updatedAt;
        return r;
    }
}
