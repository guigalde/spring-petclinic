package org.springframework.samples.petclinic.statistics;

import javax.persistence.Entity;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Achievement extends NamedEntity{ 
    String description;
    String badgeImage; 
    Double threshold;
}
