package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.Application;
import BackEnd.SkillBridge.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Semua lamaran ke satu proyek (untuk ketua)
    List<Application> findByProjectIdOrderByCreatedAtDesc(Long projectId);

    // Semua lamaran ke satu proyek dengan status tertentu
    List<Application> findByProjectIdAndStatus(Long projectId, ApplicationStatus status);

    // Semua lamaran yang dikirim satu user
    List<Application> findByApplicantIdOrderByCreatedAtDesc(Long applicantId);

    // Cek apakah user sudah melamar ke proyek ini
    boolean existsByProjectIdAndApplicantId(Long projectId, Long applicantId);

    // Ambil lamaran spesifik dari satu user ke satu proyek
    Optional<Application> findByProjectIdAndApplicantId(Long projectId, Long applicantId);

    // ── Admin: count by status ────────────────────────────────────────────
    long countByStatus(ApplicationStatus status);
}
