package com.example.hamrobank.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Utility class for date operations
 */
public class DateUtil {
    
    /**
     * Get the start date of the current month
     * 
     * @return start date of the current month
     */
    public static Date getStartOfMonth() {
        LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        return Date.valueOf(startOfMonth);
    }
    
    /**
     * Get the end date of the current month
     * 
     * @return end date of the current month
     */
    public static Date getEndOfMonth() {
        LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        return Date.valueOf(endOfMonth);
    }
    
    /**
     * Get the start date of the current year
     * 
     * @return start date of the current year
     */
    public static Date getStartOfYear() {
        LocalDate startOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        return Date.valueOf(startOfYear);
    }
    
    /**
     * Get the end date of the current year
     * 
     * @return end date of the current year
     */
    public static Date getEndOfYear() {
        LocalDate endOfYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
        return Date.valueOf(endOfYear);
    }
    
    /**
     * Get the date for a specified number of days ago
     * 
     * @param days number of days ago
     * @return date for the specified number of days ago
     */
    public static Date getDaysAgo(int days) {
        LocalDate daysAgo = LocalDate.now().minusDays(days);
        return Date.valueOf(daysAgo);
    }
    
    /**
     * Get the date for a specified number of months ago
     * 
     * @param months number of months ago
     * @return date for the specified number of months ago
     */
    public static Date getMonthsAgo(int months) {
        LocalDate monthsAgo = LocalDate.now().minusMonths(months);
        return Date.valueOf(monthsAgo);
    }
    
    /**
     * Get the date for a specified number of years ago
     * 
     * @param years number of years ago
     * @return date for the specified number of years ago
     */
    public static Date getYearsAgo(int years) {
        LocalDate yearsAgo = LocalDate.now().minusYears(years);
        return Date.valueOf(yearsAgo);
    }
    
    /**
     * Get the date for a specified number of days in the future
     * 
     * @param days number of days in the future
     * @return date for the specified number of days in the future
     */
    public static Date getDaysFromNow(int days) {
        LocalDate daysFromNow = LocalDate.now().plusDays(days);
        return Date.valueOf(daysFromNow);
    }
    
    /**
     * Get the date for a specified number of months in the future
     * 
     * @param months number of months in the future
     * @return date for the specified number of months in the future
     */
    public static Date getMonthsFromNow(int months) {
        LocalDate monthsFromNow = LocalDate.now().plusMonths(months);
        return Date.valueOf(monthsFromNow);
    }
    
    /**
     * Get the date for a specified number of years in the future
     * 
     * @param years number of years in the future
     * @return date for the specified number of years in the future
     */
    public static Date getYearsFromNow(int years) {
        LocalDate yearsFromNow = LocalDate.now().plusYears(years);
        return Date.valueOf(yearsFromNow);
    }
    
    /**
     * Convert java.sql.Date to java.time.LocalDate
     * 
     * @param date java.sql.Date to convert
     * @return converted java.time.LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toLocalDate();
    }
    
    /**
     * Convert java.time.LocalDate to java.sql.Date
     * 
     * @param localDate java.time.LocalDate to convert
     * @return converted java.sql.Date
     */
    public static Date toSqlDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }
    
    /**
     * Check if a date is in the past
     * 
     * @param date date to check
     * @return true if the date is in the past, false otherwise
     */
    public static boolean isPast(Date date) {
        return date.before(new Date(System.currentTimeMillis()));
    }
    
    /**
     * Check if a date is in the future
     * 
     * @param date date to check
     * @return true if the date is in the future, false otherwise
     */
    public static boolean isFuture(Date date) {
        return date.after(new Date(System.currentTimeMillis()));
    }
    
    /**
     * Check if a date is today
     * 
     * @param date date to check
     * @return true if the date is today, false otherwise
     */
    public static boolean isToday(Date date) {
        LocalDate today = LocalDate.now();
        LocalDate checkDate = date.toLocalDate();
        return today.equals(checkDate);
    }
}
