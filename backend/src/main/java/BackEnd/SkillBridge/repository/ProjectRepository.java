package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.Project;
import BackEnd.SkillBridge.entity.ProjectCategory;
import BackEnd.SkillBridge.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Semua proyek milik satu user (sebagai ketua)
    List<Project> findByCreatedByIdOrderByCreatedAtDesc(Long userId);

    // Proyek berdasarkan status
    Page<Project> findByStatusAndDeletedAtIsNullOrderByCreatedAtDesc(ProjectStatus status, Pageable pageable);

    // Proyek berdasarkan kategori dan status
    List<Project> findByCategoryAndStatusOrderByCreatedAtDesc(ProjectCategory category, ProjectStatus status);

    // Search proyek by keyword di title atau description
    @Query("SELECT p FROM Project p WHERE p.deletedAt IS NULL AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND p.status = :status ORDER BY p.createdAt DESC")
    Page<Project> searchByKeyword(@Param("keyword") String keyword,
                                  @Param("status") ProjectStatus status,
                                  Pageable pageable);

    // Cek apakah user adalah pemilik proyek
    boolean existsByIdAndCreatedById(Long projectId, Long userId);

    // ── Admin: count by status ────────────────────────────────────────────
    long countByStatus(ProjectStatus status);

    // ── Admin: search all projects (no status filter) ─────────────────────
    @Query("SELECT p FROM Project p WHERE p.deletedAt IS NULL AND " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY p.createdAt DESC")
    List<Project> adminSearchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Project p WHERE p.deletedAt IS NULL AND " +
           "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Project> findActiveForModeration(@Param("keyword") String keyword, Pageable pageable);

    // ── Admin: semua proyek diurutkan by createdAt desc ──────────────────
    List<Project> findAllByOrderByCreatedAtDesc();
}
