package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByNameContainingIgnoreCase(String name);

    List<Skill> findByCategoryIgnoreCase(String category);

    boolean existsByNameIgnoreCase(String name);
}
