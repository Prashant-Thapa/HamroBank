package com.example.hamrobank.listener;

import com.example.hamrobank.service.SchedulerService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;

/**
 * Application listener for startup and shutdown events
 */
@WebListener
public class ApplicationListener implements ServletContextListener {
    
    private static final Logger LOGGER = Logger.getLogger(ApplicationListener.class.getName());
    
    /**
     * Called when the application is starting up
     * 
     * @param sce ServletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Application starting up");
        
        // Start the scheduler service
        SchedulerService.getInstance().start();
    }
    
    /**
     * Called when the application is shutting down
     * 
     * @param sce ServletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application shutting down");
        
        // Stop the scheduler service
        SchedulerService.getInstance().stop();
    }
}
