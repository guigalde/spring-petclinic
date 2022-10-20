package org.springframework.samples.petclinic.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "achievement")
public class Achievent extends NamedEntity{
    @Column(name = "description")  
    String description;
    @Column(name = "badge_image")  
    String badgeImage;
    @Column(name = "threshold")  
    Double threshold;
}
