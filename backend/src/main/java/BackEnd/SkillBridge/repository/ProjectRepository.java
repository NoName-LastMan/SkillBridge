package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.Project;
import BackEnd.SkillBridge.entity.ProjectCategory;
import BackEnd.SkillBridge.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Semua proyek milik satu user (sebagai ketua)
    List<Project> findByCreatedByIdOrderByCreatedAtDesc(Long userId);

    // Proyek berdasarkan status
    List<Project> findByStatusOrderByCreatedAtDesc(ProjectStatus status);

    // Proyek berdasarkan kategori dan status
    List<Project> findByCategoryAndStatusOrderByCreatedAtDesc(ProjectCategory category, ProjectStatus status);

    // Search proyek by keyword di title atau description
    @Query("SELECT p FROM Project p WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND p.status = :status ORDER BY p.createdAt DESC")
    List<Project> searchByKeyword(@Param("keyword") String keyword,
                                   @Param("status") ProjectStatus status);

    // Cek apakah user adalah pemilik proyek
    boolean existsByIdAndCreatedById(Long projectId, Long userId);
}
