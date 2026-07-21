package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.ContactRequest;
import BackEnd.SkillBridge.entity.ContactRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {

    // Semua request masuk (sebagai target), filter by status
    List<ContactRequest> findByTargetIdAndStatus(Long targetId, ContactRequestStatus status);

    // Semua request keluar (sebagai requester)
    List<ContactRequest> findByRequesterId(Long requesterId);

    // Cek apakah sudah ada request dari requester ke target dengan status tertentu
    boolean existsByRequesterIdAndTargetIdAndStatus(Long requesterId, Long targetId,
                                                     ContactRequestStatus status);

    // Cari request spesifik antara dua user
    Optional<ContactRequest> findByRequesterIdAndTargetId(Long requesterId, Long targetId);
}
