package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.ContactRequest;
import BackEnd.SkillBridge.entity.ContactRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {

    // Cari semua request masuk (sebagai target) berdasarkan status
    List<ContactRequest> findByTargetIdAndStatus(Long targetId, ContactRequestStatus status);

    // Cari semua request keluar (sebagai requester)
    List<ContactRequest> findByRequesterId(Long requesterId);

    // Cek apakah sudah ada request PENDING dari requester ke target
    boolean existsByRequesterIdAndTargetIdAndStatus(Long requesterId, Long targetId, ContactRequestStatus status);

    // Cari request spesifik antara dua user
    Optional<ContactRequest> findByRequesterIdAndTargetId(Long requesterId, Long targetId);

    // Cek apakah requester sudah mendapat persetujuan dari target
    boolean existsByRequesterIdAndTargetIdAndStatus(Long requesterId, Long targetId, ContactRequestStatus approved);
}
