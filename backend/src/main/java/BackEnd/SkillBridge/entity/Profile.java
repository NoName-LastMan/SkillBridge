package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Profile")
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    @JoinColumn(name = "User_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "nama_lengkap", nullable = false, length = 150)
    private String namaLengkap;

    @Column(unique = true, nullable = false, length = 20)
    private String nim;

    @Column(nullable = false, length = 100)
    private String prodi;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
