package com.example.hamrobank.service;

import com.example.hamrobank.dao.BudgetDAO;
import com.example.hamrobank.dao.BudgetDAOImpl;
import com.example.hamrobank.model.Budget;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for budgets
 */
public class BudgetService {

    private static final Logger LOGGER = Logger.getLogger(BudgetService.class.getName());
    private final BudgetDAO budgetDAO;
    private final NotificationService notificationService;

    /**
     * Constructor
     */
    public BudgetService() {
        this.budgetDAO = new BudgetDAOImpl();
        this.notificationService = new NotificationService();
    }

    /**
     * Constructor with DAO injection (for testing)
     *
     * @param budgetDAO DAO to use
     * @param notificationService NotificationService to use
     */
    public BudgetService(BudgetDAO budgetDAO, NotificationService notificationService) {
        this.budgetDAO = budgetDAO;
        this.notificationService = notificationService;
    }

    /**
     * Create a new budget
     *
     * @param budget Budget object to create
     * @return created Budget with ID
     */
    public Budget createBudget(Budget budget) {
        try {
            // Set default values if not set
            if (budget.getStartDate() == null) {
                budget.setStartDate(Date.valueOf(LocalDate.now()));
            }

            Budget createdBudget = budgetDAO.createBudget(budget);

            // Send notification
            notificationService.sendBalanceNotification(
                budget.getUserId(),
                "New Budget Created",
                "You have created a new budget: " + budget.getName()
            );

            return createdBudget;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating budget", e);
            throw new RuntimeException("Error creating budget", e);
        }
    }

    /**
     * Get a budget by ID
     *
     * @param budgetId ID of the budget to retrieve
     * @return Budget object if found, null otherwise
     */
    public Budget getBudgetById(int budgetId) {
        try {
            return budgetDAO.getBudgetById(budgetId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting budget by ID", e);
            throw new RuntimeException("Error getting budget by ID", e);
        }
    }

    /**
     * Get all budgets for a user
     *
     * @param userId ID of the user
     * @return List of Budget objects for the user
     */
    public List<Budget> getBudgetsByUserId(int userId) {
        try {
            return budgetDAO.getBudgetsByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting budgets by user ID", e);
            throw new RuntimeException("Error getting budgets by user ID", e);
        }
    }

    /**
     * Get all budgets for a user by category
     *
     * @param userId ID of the user
     * @param categoryId ID of the category
     * @return List of Budget objects for the user and category
     */
    public List<Budget> getBudgetsByCategory(int userId, int categoryId) {
        try {
            return budgetDAO.getBudgetsByCategory(userId, categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting budgets by category", e);
            throw new RuntimeException("Error getting budgets by category", e);
        }
    }

    /**
     * Get all active budgets for a user
     *
     * @param userId ID of the user
     * @return List of active Budget objects for the user
     */
    public List<Budget> getActiveBudgets(int userId) {
        try {
            return budgetDAO.getActiveBudgets(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting active budgets", e);
            throw new RuntimeException("Error getting active budgets", e);
        }
    }

    /**
     * Update a budget
     *
     * @param budget Budget object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateBudget(Budget budget) {
        try {
            return budgetDAO.updateBudget(budget);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating budget", e);
            throw new RuntimeException("Error updating budget", e);
        }
    }

    /**
     * Delete a budget
     *
     * @param budgetId ID of the budget to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteBudget(int budgetId) {
        try {
            return budgetDAO.deleteBudget(budgetId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting budget", e);
            throw new RuntimeException("Error deleting budget", e);
        }
    }

    /**
     * Get spending by category for a user within a date range
     *
     * @param userId ID of the user
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return Map of category IDs to spending amounts
     */
    public Map<Integer, BigDecimal> getSpendingByCategory(int userId, Date startDate, Date endDate) {
        try {
            return budgetDAO.getSpendingByCategory(userId, startDate, endDate);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting spending by category", e);
            throw new RuntimeException("Error getting spending by category", e);
        }
    }

    /**
     * Get budget vs. actual spending for a user
     *
     * @param userId ID of the user
     * @return Map of budget IDs to arrays containing [budget amount, actual spending]
     */
    public Map<Integer, BigDecimal[]> getBudgetVsActual(int userId) {
        try {
            return budgetDAO.getBudgetVsActual(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting budget vs actual", e);
            throw new RuntimeException("Error getting budget vs actual", e);
        }
    }

    /**
     * Check if any budgets are approaching or exceeding their limits
     *
     * @param userId ID of the user
     */
    public void checkBudgetAlerts(int userId) {
        try {
            Map<Integer, BigDecimal[]> budgetVsActual = budgetDAO.getBudgetVsActual(userId);
            List<Budget> activeBudgets = budgetDAO.getActiveBudgets(userId);

            for (Budget budget : activeBudgets) {
                if (budgetVsActual.containsKey(budget.getBudgetId())) {
                    BigDecimal[] values = budgetVsActual.get(budget.getBudgetId());
                    BigDecimal budgetAmount = values[0];
                    BigDecimal actualSpending = values[1];

                    // Calculate percentage spent
                    BigDecimal percentSpent = actualSpending.multiply(new BigDecimal("100"))
                            .divide(budgetAmount, 2, java.math.RoundingMode.HALF_UP);

                    // Check if approaching limit (80% or more)
                    if (percentSpent.compareTo(new BigDecimal("80")) >= 0 &&
                        percentSpent.compareTo(new BigDecimal("100")) < 0) {

                        notificationService.sendBalanceNotification(
                            userId,
                            "Budget Alert",
                            "You have used " + percentSpent + "% of your budget for " + budget.getName()
                        );
                    }

                    // Check if exceeded limit
                    if (percentSpent.compareTo(new BigDecimal("100")) >= 0) {
                        notificationService.sendBalanceNotification(
                            userId,
                            "Budget Exceeded",
                            "You have exceeded your budget for " + budget.getName()
                        );
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking budget alerts", e);
            throw new RuntimeException("Error checking budget alerts", e);
        }
    }

    /**
     * Get spending summary by category for a user
     *
     * @param userId ID of the user
     * @return Map of category names to spending percentages
     */
    public Map<String, Double> getSpendingSummary(int userId) {
        try {
            // Get current date for period calculations
            LocalDate currentDate = LocalDate.now();
            LocalDate startOfMonth = currentDate.withDayOfMonth(1);
            Date sqlStartOfMonth = Date.valueOf(startOfMonth);
            Date sqlCurrentDate = Date.valueOf(currentDate);

            // Get spending by category
            Map<Integer, BigDecimal> spendingByCategory = budgetDAO.getSpendingByCategory(userId, sqlStartOfMonth, sqlCurrentDate);

            // Calculate total spending
            BigDecimal totalSpending = BigDecimal.ZERO;
            for (BigDecimal amount : spendingByCategory.values()) {
                totalSpending = totalSpending.add(amount);
            }

            // Calculate percentages
            Map<String, Double> spendingSummary = new HashMap<>();

            if (totalSpending.compareTo(BigDecimal.ZERO) > 0) {
                for (Map.Entry<Integer, BigDecimal> entry : spendingByCategory.entrySet()) {
                    int categoryId = entry.getKey();
                    BigDecimal amount = entry.getValue();

                    // Get category name (in a real implementation, you would get this from the category service)
                    String categoryName = "Category " + categoryId;

                    // Calculate percentage
                    double percentage = amount.multiply(new BigDecimal("100"))
                            .divide(totalSpending, 2, java.math.RoundingMode.HALF_UP)
                            .doubleValue();

                    spendingSummary.put(categoryName, percentage);
                }
            }

            return spendingSummary;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting spending summary", e);
            throw new RuntimeException("Error getting spending summary", e);
        }
    }
}
