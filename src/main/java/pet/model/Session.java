package pet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Sessions")
@Getter
@Setter
@NoArgsConstructor
public class Session {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
