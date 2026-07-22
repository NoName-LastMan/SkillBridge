package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    // Semua anggota satu proyek
    List<TeamMember> findByProjectId(Long projectId);

    // Semua proyek yang diikuti satu user (sebagai anggota)
    List<TeamMember> findByUserId(Long userId);

    // Cek apakah user sudah jadi anggota proyek ini
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    long countByProjectId(Long projectId);
}
