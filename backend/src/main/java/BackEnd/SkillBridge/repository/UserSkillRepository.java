package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.UserSkill;
import BackEnd.SkillBridge.entity.UserSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, UserSkillId> {

    List<UserSkill> findByUserId(Long userId);

    boolean existsByIdUserIdAndIdSkillId(Long userId, Long skillId);
}
