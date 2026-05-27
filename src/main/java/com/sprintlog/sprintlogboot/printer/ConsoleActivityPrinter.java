package com.sprintlog.sprintlogboot.printer;


import com.sprintlog.sprintlogboot.domain.LearningActivity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("console")
@Primary
public class ConsoleActivityPrinter implements ActivityPrinter {


    //interface
    @Override
    public void print(LearningActivity activity) {
        System.out.println(
                "[" + activity.getActivityType() + "]"
                        + " #" + activity.getId()
                        + " " + activity.getTitle()
                        + " - " + activity.getMinutes() + "분"
                        + " - " + activity.getDetailText()
                        + " - " + activity.getVisibilityText() + " d\\^_^/b"
        );

    }

}
