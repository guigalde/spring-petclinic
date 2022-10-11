package org.springframework.samples.petclinic.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchievementFormatterTest {
    @Autowired
    AchievementService service;
    @Test
    public void testPrint(){
        String name="Test";
        Achievement achievement=new Achievement();
        achievement.setName(name);
        AchievementFormatter formatter=new AchievementFormatter(service);
        assertEquals(name,formatter.print(achievement, Locale.getDefault()));
    }

    @Test
    public void testParse(){
        String name="Triunfador";
        AchievementFormatter formatter=new AchievementFormatter(service);
        try{
            formatter.parse(name, Locale.getDefault());
        }catch(Exception ex){
            fail("The formater did not find an existing achievement ('"+name+"'");
        }
        try{
            formatter.parse("Este nombre de logro no existeeeeerrr!!!", Locale.getDefault());
            fail("The formater should have thrown an exception!!!!");
        }catch(Exception ex){             }
    }

}
