package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.request.AddUserSkillRequest;
import BackEnd.SkillBridge.dto.request.CreateSkillRequest;
import BackEnd.SkillBridge.dto.response.SkillResponse;
import BackEnd.SkillBridge.dto.response.UserSkillResponse;
import BackEnd.SkillBridge.entity.Skill;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.entity.UserSkill;
import BackEnd.SkillBridge.entity.UserSkillId;
import BackEnd.SkillBridge.repository.SkillRepository;
import BackEnd.SkillBridge.repository.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    // ── Master Skill ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(SkillResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> searchSkills(String keyword) {
        return skillRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(SkillResponse::from)
                .toList();
    }

    @Transactional
    public SkillResponse createSkill(CreateSkillRequest request) {
        if (skillRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Skill '" + request.getName() + "' sudah ada");
        }

        Skill skill = Skill.builder()
                .name(request.getName())
                .category(request.getCategory())
                .build();

        return SkillResponse.from(skillRepository.save(skill));
    }

    // ── User Skill ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<UserSkillResponse> getMySkills(Long userId) {
        return userSkillRepository.findByUserId(userId)
                .stream()
                .map(UserSkillResponse::from)
                .toList();
    }

    @Transactional
    public UserSkillResponse addSkillToUser(User user, AddUserSkillRequest request) {
        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Skill tidak ditemukan"));

        if (userSkillRepository.existsByIdUserIdAndIdSkillId(user.getId(), skill.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Skill '" + skill.getName() + "' sudah ada di profil Anda");
        }

        UserSkill userSkill = UserSkill.builder()
                .id(new UserSkillId(user.getId(), skill.getId()))
                .user(user)
                .skill(skill)
                .level(request.getLevel())
                .build();

        return UserSkillResponse.from(userSkillRepository.save(userSkill));
    }

    @Transactional
    public void removeSkillFromUser(Long userId, Long skillId) {
        UserSkillId id = new UserSkillId(userId, skillId);
        if (!userSkillRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Skill tidak ditemukan di profil Anda");
        }
        userSkillRepository.deleteById(id);
    }
}
