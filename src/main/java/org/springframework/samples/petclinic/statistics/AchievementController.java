package org.springframework.samples.petclinic.statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/statistics/achievements")
public class AchievementController {

    private final String PERSONAL_LISTING_VIEW="/achievements/PersonalAchievementsListing";
    private final String ACHIEVEMENTS_LISTING_VIEW="/achievements/AchievementsListing";
    private final String ACHIEVEMENTS_FORM="/achievements/createOrUpdateAchievementForm";
    private final String OWNER_ACHIEVEMENTS_FORM="/achievements/createOrUpdateAchievementsOfOwnerForm";

    private AchievementService service;
    private OwnerService ownerService;

    @Autowired
    public AchievementController(AchievementService service, OwnerService ownerService){
        this.service=service;
        this.ownerService=ownerService;
    }

    @Transactional(readOnly = true)
    @GetMapping("/")
    public ModelAndView showAchievements(){
        ModelAndView result=new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);
        result.addObject("achievements", service.getAchievements());
        return result;
    }

    @Transactional()
    @GetMapping("/{id}/delete")
    public ModelAndView deleteAchievement(@PathVariable int id){
        service.deleteAchievementById(id);        
        return showAchievements();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}/edit")
    public ModelAndView editAchievement(@PathVariable int id){
        Achievement achievement=service.getById(id);        
        ModelAndView result=new ModelAndView(ACHIEVEMENTS_FORM);
        result.addObject("achievement", achievement);         
        result.addObject("metrics",Arrays.asList(Metric.values()));        
        return result;
    }
 
    @Transactional
    @PostMapping("/{id}/edit")
    public ModelAndView saveAchievement(@PathVariable int id,@Valid Achievement achievement, BindingResult br){
        ModelAndView result=null;
        if(br.hasErrors()){
            result=new ModelAndView(ACHIEVEMENTS_FORM,br.getModel());            
            result.addObject("metrics",Arrays.asList(Metric.values()));        
            return result;
        }
        Achievement achievementToBeUpdated=service.getById(id);
        BeanUtils.copyProperties(achievement,achievementToBeUpdated,"id");
        service.save(achievementToBeUpdated);
        result=showAchievements();
        result.addObject("message", "The achievement was updated successfully");
        return result;        
    }

    @Transactional(readOnly = true)
    @GetMapping("/new")
    public ModelAndView createAchievement(){
        Achievement achievement=new Achievement();
        ModelAndView result=new ModelAndView(ACHIEVEMENTS_FORM);        
        result.addObject("achievement",achievement);
        result.addObject("metrics",Arrays.asList(Metric.values()));        
        return result;
    }

    @Transactional
    @PostMapping("/new")
    public ModelAndView saveNewAchievement(@Valid Achievement achievement, BindingResult br){
        ModelAndView result=null;
        if(br.hasErrors()){
            result=new ModelAndView(ACHIEVEMENTS_FORM,br.getModel());            
            result.addObject("metrics",Arrays.asList(Metric.values()));        
            return result;
        }
        service.save(achievement);
        result=showAchievements();
        result.addObject("message", "The achievement was created successfully");
        return result;
    }

    @GetMapping("/byOwner/{id}")
    public ModelAndView showPersonalAchievementsListing(@PathVariable int id){
        ModelAndView result=new ModelAndView(PERSONAL_LISTING_VIEW);
        result.addObject("achievements",service.getAchievementsByOwner(id));
        return result;
    }

    @GetMapping("/byOwner/{id}/edit")
    public ModelAndView showOwnerAchievementsEditForm(@PathVariable("id")int ownerId){
        ModelAndView result=new ModelAndView(OWNER_ACHIEVEMENTS_FORM);
        Owner owner=ownerService.findOwnerById(ownerId);
        result.addObject("owner", owner);
        result.addObject("availableAchievements",service.getAchievements());
        return result;
    }

    @PostMapping("/byOwner/{id}/edit")
    public ModelAndView updateOwnerAchievements(Owner owner,BindingResult brresult,@PathVariable("id")int ownerId){        
        Owner ownerToBeUpdated=ownerService.findOwnerById(ownerId);
        ownerToBeUpdated.getAchievements().clear();
        ownerToBeUpdated.getAchievements().addAll(owner.getAchievements());
        ownerService.saveOwner(ownerToBeUpdated);
        return showPersonalAchievementsListing(ownerId);
    }

    @GetMapping("/me")
    public ModelAndView showCurrentUserAchievements(@AuthenticationPrincipal Object user){
        ModelAndView result=null;
        Owner owner=null;
        if(user!=null && (user instanceof UserDetails))
            owner=ownerService.findOwnerByUsername(((UserDetails)user).getUsername());
        if(owner!=null){
            result=showPersonalAchievementsListing(owner.getId());
        }else{
            result=new ModelAndView("welcome");
            result.addObject("message","You are not an Owner, thus you don't have achievements");
            result.addObject("messageType","warning");            
        }
        return result;
    }
    
}
