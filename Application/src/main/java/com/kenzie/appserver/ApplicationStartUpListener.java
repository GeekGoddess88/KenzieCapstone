package com.kenzie.appserver;


import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.task.DrinkTask;
import com.kenzie.capstone.service.task.IngredientTask;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;


@Component
public class ApplicationStartUpListener {

    private ServiceComponent serviceComponent;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Perform any application start-up tasks
        this.serviceComponent = DaggerServiceComponent.create();
    }

    public ServiceComponent getServiceComponent() {
        return this.serviceComponent;
    }
}
