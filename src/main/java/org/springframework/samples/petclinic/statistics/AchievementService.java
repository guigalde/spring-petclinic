package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {
    private  AchievementRepository achievementRepository;

    @Autowired
    public AchievementService(AchievementRepository repo){
        this.achievementRepository=repo;
    }

    List<Achievement> getAchievement(){
        return achievementRepository.findAll();
    }
    
}
