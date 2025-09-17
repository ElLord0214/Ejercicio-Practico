import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "profiles", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Profile extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    public UUID id;

    @Column(nullable = false)
    public String firstName;

    @Column(nullable = false)
    public String lastName;

    @Column(nullable = false, unique = true)
    public String email;

    public String cellphone;

    @Column(nullable = false)
    public OffsetDateTime createdAt;

    @Column(nullable = false)
    public OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
