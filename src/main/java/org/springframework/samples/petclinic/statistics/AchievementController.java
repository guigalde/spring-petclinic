package org.springframework.samples.petclinic.statistics;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/statistics/achievements")
public class AchievementController {

    private final String ACHIEVEMENTS_LISTING_VIEW="/achievements/AchievementsListing";
    private final String ACHIEVEMENTS_FORM="/achievements/createOrUpdateAchievementForm";

    private AchievementService service;

    @Autowired
    public AchievementController(AchievementService service){
        this.service=service;
    }


    @GetMapping("/")
    public ModelAndView showAchievements(){
        ModelAndView result=new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);
        result.addObject("achievements", service.getAchievements());
        return result;
    }


    @GetMapping("/{id}/delete")
    public ModelAndView deleteAchievement(@PathVariable int id){
        service.deleteAchievementById(id);        
        return showAchievements();
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editAchievement(@PathVariable int id){
        Achievement achievement=service.getById(id);        
        ModelAndView result=new ModelAndView(ACHIEVEMENTS_FORM);
        result.addObject("achievement", achievement);
        return result;
    }


    @PostMapping("/{id}/edit")
    public ModelAndView saveAchievement(@PathVariable int id,Achievement achievement){
        
        Achievement achievementToBeUpdated=service.getById(id);
        BeanUtils.copyProperties(achievement,achievementToBeUpdated,"id");
        service.save(achievementToBeUpdated);
        return showAchievements();
    }



}
