package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.response.ContactRequestResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.ContactRequestRepository;
import BackEnd.SkillBridge.repository.ProfileRepository;
import BackEnd.SkillBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ContactRequestService {

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    // ── Kirim request untuk melihat kontak ────────────────────────────────
    @Transactional
    public ContactRequestResponse sendContactRequest(User requester, Long targetUserId) {
        // Validasi tidak bisa request ke diri sendiri
        if (requester.getId().equals(targetUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tidak bisa mengirim request ke diri sendiri");
        }

        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User tidak ditemukan"));

        // Cek apakah target memang memiliki kontak PRIVATE
        Profile targetProfile = profileRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profil user tidak ditemukan"));

        if (targetProfile.getContactPrivacy() == ContactPrivacy.PUBLIC) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Kontak user ini sudah PUBLIC, tidak perlu mengirim request");
        }

        // Cek sudah ada request APPROVED
        if (contactRequestRepository.existsByRequesterIdAndTargetIdAndStatus(
                requester.getId(), targetUserId, ContactRequestStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Anda sudah mendapatkan akses ke kontak user ini");
        }

        // Cek sudah ada request PENDING
        if (contactRequestRepository.existsByRequesterIdAndTargetIdAndStatus(
                requester.getId(), targetUserId, ContactRequestStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Anda sudah mengirim request yang sedang menunggu persetujuan");
        }

        ContactRequest newRequest = ContactRequest.builder()
                .requester(requester)
                .target(target)
                .status(ContactRequestStatus.PENDING)
                .build();

        ContactRequest saved = contactRequestRepository.save(newRequest);
        return enrichResponse(saved);
    }

    // ── Daftar request masuk (saya sebagai target) — PENDING ──────────────
    @Transactional(readOnly = true)
    public List<ContactRequestResponse> getIncomingRequests(Long targetId) {
        return contactRequestRepository
                .findByTargetIdAndStatus(targetId, ContactRequestStatus.PENDING)
                .stream()
                .map(this::enrichResponse)
                .toList();
    }

    // ── Daftar request yang saya kirim ────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ContactRequestResponse> getOutgoingRequests(Long requesterId) {
        return contactRequestRepository
                .findByRequesterId(requesterId)
                .stream()
                .map(this::enrichResponse)
                .toList();
    }

    // ── Setujui request ───────────────────────────────────────────────────
    @Transactional
    public ContactRequestResponse approveRequest(Long requestId, Long currentUserId) {
        ContactRequest request = findAndValidateOwnership(requestId, currentUserId);

        if (request.getStatus() != ContactRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request ini sudah diproses sebelumnya");
        }

        request.setStatus(ContactRequestStatus.APPROVED);
        return enrichResponse(contactRequestRepository.save(request));
    }

    // ── Tolak request ─────────────────────────────────────────────────────
    @Transactional
    public ContactRequestResponse rejectRequest(Long requestId, Long currentUserId) {
        ContactRequest request = findAndValidateOwnership(requestId, currentUserId);

        if (request.getStatus() != ContactRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request ini sudah diproses sebelumnya");
        }

        request.setStatus(ContactRequestStatus.REJECTED);
        return enrichResponse(contactRequestRepository.save(request));
    }

    // ── Helper: validasi ownership (hanya target yang bisa approve/reject) ─
    private ContactRequest findAndValidateOwnership(Long requestId, Long currentUserId) {
        ContactRequest request = contactRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request tidak ditemukan"));

        if (!request.getTarget().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Anda tidak berhak memproses request ini");
        }
        return request;
    }

    // ── Helper: enrich response dengan nama dari profile ──────────────────
    private ContactRequestResponse enrichResponse(ContactRequest req) {
        String requesterName = profileRepository
                .findByUserId(req.getRequester().getId())
                .map(Profile::getNamaLengkap)
                .orElse(null);

        String requesterNim = profileRepository
                .findByUserId(req.getRequester().getId())
                .map(Profile::getNim)
                .orElse(null);

        String targetName = profileRepository
                .findByUserId(req.getTarget().getId())
                .map(Profile::getNamaLengkap)
                .orElse(null);

        return ContactRequestResponse.builder()
                .id(req.getId())
                .requesterId(req.getRequester().getId())
                .requesterEmail(req.getRequester().getEmail())
                .requesterName(requesterName)
                .requesterNim(requesterNim)
                .targetId(req.getTarget().getId())
                .targetName(targetName)
                .status(req.getStatus())
                .createdAt(req.getCreatedAt())
                .updatedAt(req.getUpdatedAt())
                .build();
    }
}
