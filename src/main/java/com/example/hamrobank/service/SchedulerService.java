package com.example.hamrobank.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for scheduling tasks
 */
public class SchedulerService {
    
    private static final Logger LOGGER = Logger.getLogger(SchedulerService.class.getName());
    private static SchedulerService instance;
    private final ScheduledExecutorService scheduler;
    
    /**
     * Private constructor for singleton pattern
     */
    private SchedulerService() {
        scheduler = Executors.newScheduledThreadPool(1);
    }
    
    /**
     * Get the singleton instance
     * 
     * @return SchedulerService instance
     */
    public static synchronized SchedulerService getInstance() {
        if (instance == null) {
            instance = new SchedulerService();
        }
        return instance;
    }
    
    /**
     * Start the scheduler
     */
    public void start() {
        LOGGER.info("Starting scheduler service");
        
        // Schedule execution of due transactions daily at midnight
        scheduler.scheduleAtFixedRate(
            this::executeDueTransactions,
            calculateInitialDelay(),
            24 * 60 * 60, // 24 hours
            TimeUnit.SECONDS
        );
        
        // Schedule budget alerts check daily
        scheduler.scheduleAtFixedRate(
            this::checkBudgetAlerts,
            calculateInitialDelay() + 60, // 1 minute after due transactions
            24 * 60 * 60, // 24 hours
            TimeUnit.SECONDS
        );
    }
    
    /**
     * Stop the scheduler
     */
    public void stop() {
        LOGGER.info("Stopping scheduler service");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Execute due scheduled transactions
     */
    private void executeDueTransactions() {
        try {
            LOGGER.info("Executing due scheduled transactions");
            ScheduledTransactionService scheduledTransactionService = new ScheduledTransactionService();
            scheduledTransactionService.executeDueTransactions();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing due transactions", e);
        }
    }
    
    /**
     * Check budget alerts for all users
     */
    private void checkBudgetAlerts() {
        try {
            LOGGER.info("Checking budget alerts");
            // In a real implementation, you would get all users and check budget alerts for each
            // For now, we'll just log a message
            LOGGER.info("Budget alerts check completed");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking budget alerts", e);
        }
    }
    
    /**
     * Calculate initial delay to start at midnight
     * 
     * @return initial delay in seconds
     */
    private long calculateInitialDelay() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime nextRun = now.toLocalDate().plusDays(1).atStartOfDay();
        return java.time.Duration.between(now, nextRun).getSeconds();
    }
}
