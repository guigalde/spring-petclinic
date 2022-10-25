package org.springframework.samples.petclinic.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/statistics/achievement")
public class AchievementController {
    private AchievementService achievementService;

    private final String  ACHIEVEMENTS_LISTING_VIEW="/achievements/AchievementsListing";

    @Autowired
    public void achievementController(AchievementService service){
        this.achievementService=service;
    }

    @GetMapping("/")
    public ModelAndView showAchievements(){

        ModelAndView result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);
        result.addObject("achievements", achievementService.getAchievement());
        return result;

    }
    
}
