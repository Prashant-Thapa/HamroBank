-- Schema updates for Hamro Bank enhancements

-- Transaction Categories
CREATE TABLE IF NOT EXISTS transaction_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    icon VARCHAR(50),
    color VARCHAR(20),
    parent_category_id INT,
    is_system BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_category_id) REFERENCES transaction_categories(category_id) ON DELETE SET NULL
);

-- Add category to transactions
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS category_id INT;
ALTER TABLE transactions ADD CONSTRAINT IF NOT EXISTS fk_transaction_category 
    FOREIGN KEY (category_id) REFERENCES transaction_categories(category_id) ON DELETE SET NULL;

-- Scheduled Transactions
CREATE TABLE IF NOT EXISTS scheduled_transactions (
    scheduled_tx_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    source_account_id INT NOT NULL,
    destination_account_id INT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    description VARCHAR(255),
    frequency VARCHAR(20) NOT NULL, -- ONCE, DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
    start_date DATE NOT NULL,
    end_date DATE,
    next_execution_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, PAUSED, COMPLETED, FAILED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (source_account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (destination_account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Transaction Templates
CREATE TABLE IF NOT EXISTS transaction_templates (
    template_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    source_account_id INT NOT NULL,
    destination_account_id INT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    description VARCHAR(255),
    category_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (source_account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (destination_account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES transaction_categories(category_id) ON DELETE SET NULL
);

-- Savings Goals
CREATE TABLE IF NOT EXISTS savings_goals (
    goal_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    target_amount DECIMAL(19,4) NOT NULL,
    current_amount DECIMAL(19,4) DEFAULT 0,
    start_date DATE NOT NULL,
    target_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, COMPLETED, CANCELLED
    icon VARCHAR(50),
    color VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Notifications
CREATE TABLE IF NOT EXISTS notification_preferences (
    preference_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    notification_type VARCHAR(50) NOT NULL, -- TRANSACTION, BALANCE, SECURITY, MARKETING
    channel VARCHAR(20) NOT NULL, -- EMAIL, SMS, PUSH, IN_APP
    is_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY (user_id, notification_type, channel)
);

CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Account Customization
ALTER TABLE accounts ADD COLUMN IF NOT EXISTS nickname VARCHAR(100);
ALTER TABLE accounts ADD COLUMN IF NOT EXISTS is_favorite BOOLEAN DEFAULT FALSE;
ALTER TABLE accounts ADD COLUMN IF NOT EXISTS color VARCHAR(20);
ALTER TABLE accounts ADD COLUMN IF NOT EXISTS icon VARCHAR(50);

-- Joint Accounts
CREATE TABLE IF NOT EXISTS account_owners (
    account_id INT NOT NULL,
    user_id INT NOT NULL,
    permission_level VARCHAR(20) NOT NULL DEFAULT 'FULL', -- FULL, VIEW_ONLY, TRANSACT
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, user_id),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Budget Tracking
CREATE TABLE IF NOT EXISTS budgets (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    period VARCHAR(20) NOT NULL, -- MONTHLY, QUARTERLY, YEARLY
    category_id INT,
    start_date DATE NOT NULL,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES transaction_categories(category_id) ON DELETE SET NULL
);

-- User Preferences
CREATE TABLE IF NOT EXISTS user_preferences (
    user_id INT PRIMARY KEY,
    theme VARCHAR(20) DEFAULT 'LIGHT', -- LIGHT, DARK, SYSTEM
    dashboard_layout JSON,
    language VARCHAR(10) DEFAULT 'en',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Insert default transaction categories
INSERT INTO transaction_categories (name, description, icon, color, is_system) VALUES
('Housing', 'Rent, mortgage, property taxes', 'home', '#4CAF50', TRUE),
('Transportation', 'Car payments, gas, public transit', 'directions_car', '#2196F3', TRUE),
('Food', 'Groceries, restaurants, takeout', 'restaurant', '#FF9800', TRUE),
('Utilities', 'Electricity, water, gas, internet', 'power', '#9C27B0', TRUE),
('Insurance', 'Health, auto, home, life insurance', 'security', '#F44336', TRUE),
('Medical', 'Doctor visits, medications, procedures', 'local_hospital', '#E91E63', TRUE),
('Savings', 'Deposits to savings accounts', 'savings', '#009688', TRUE),
('Personal', 'Clothing, cosmetics, personal care', 'person', '#607D8B', TRUE),
('Entertainment', 'Movies, games, hobbies, subscriptions', 'theaters', '#673AB7', TRUE),
('Education', 'Tuition, books, courses', 'school', '#3F51B5', TRUE),
('Gifts', 'Presents, donations, charity', 'card_giftcard', '#795548', TRUE),
('Travel', 'Flights, hotels, vacations', 'flight', '#FFC107', TRUE),
('Business', 'Business expenses', 'business', '#616161', TRUE),
('Income', 'Salary, freelance, investments', 'trending_up', '#4CAF50', TRUE),
('Transfer', 'Account transfers', 'swap_horiz', '#2196F3', TRUE),
('Other', 'Miscellaneous expenses', 'more_horiz', '#9E9E9E', TRUE);
