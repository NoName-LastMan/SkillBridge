package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.Role;
import BackEnd.SkillBridge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // ── Admin: count methods ───────────────────────────────────────────────
    long countByRole(Role role);

    long countByIsVerified(boolean isVerified);

    long countByRoleAndIsVerified(Role role, boolean isVerified);

    // ── Admin: filter by role ──────────────────────────────────────────────
    List<User> findByRoleOrderByCreatedAtDesc(Role role);

    // ── Admin: filter by role and verification status ──────────────────────
    List<User> findByRoleAndIsVerifiedOrderByCreatedAtDesc(Role role, boolean isVerified);

    // ── Admin: search users by email, namaLengkap, or NIM ─────────────────
    @Query("SELECT u FROM User u LEFT JOIN Profile p ON p.user = u " +
           "WHERE u.role = :role " +
           "AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(p.namaLengkap) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(p.nim) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY u.createdAt DESC")
    List<User> searchByKeywordAndRole(@Param("keyword") String keyword,
                                      @Param("role") Role role);
}
