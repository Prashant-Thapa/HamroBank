-- Add category_id column to transactions table
ALTER TABLE transactions
ADD COLUMN category_id INT NULL,
ADD CONSTRAINT fk_transaction_category
    FOREIGN KEY (category_id)
    REFERENCES transaction_categories(category_id)
    ON DELETE SET NULL;

-- Create transaction_categories table
CREATE TABLE transaction_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    icon VARCHAR(50),
    color VARCHAR(20),
    parent_category_id INT,
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_parent_category
        FOREIGN KEY (parent_category_id)
        REFERENCES transaction_categories(category_id)
        ON DELETE SET NULL
);

-- Create scheduled_transactions table
CREATE TABLE scheduled_transactions (
    scheduled_tx_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    source_account_id INT,
    destination_account_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255),
    frequency VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    next_execution_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_scheduled_tx_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_scheduled_tx_source
        FOREIGN KEY (source_account_id)
        REFERENCES accounts(account_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_scheduled_tx_destination
        FOREIGN KEY (destination_account_id)
        REFERENCES accounts(account_id)
        ON DELETE CASCADE
);

-- Create transaction_templates table
CREATE TABLE transaction_templates (
    template_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    source_account_id INT NOT NULL,
    destination_account_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255),
    category_id INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_template_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_template_source
        FOREIGN KEY (source_account_id)
        REFERENCES accounts(account_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_template_destination
        FOREIGN KEY (destination_account_id)
        REFERENCES accounts(account_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_template_category
        FOREIGN KEY (category_id)
        REFERENCES transaction_categories(category_id)
        ON DELETE SET NULL
);

-- Create savings_goals table
CREATE TABLE savings_goals (
    goal_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    target_amount DECIMAL(10, 2) NOT NULL,
    current_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    start_date DATE NOT NULL,
    target_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    icon VARCHAR(50),
    color VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_goal_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_goal_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(account_id)
        ON DELETE CASCADE
);

-- Create notifications table
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(20) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- Create notification_preferences table
CREATE TABLE notification_preferences (
    preference_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    notification_type VARCHAR(20) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_preference_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT uk_user_type_channel
        UNIQUE (user_id, notification_type, channel)
);

-- Create budgets table
CREATE TABLE budgets (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    period VARCHAR(20) NOT NULL,
    category_id INT,
    start_date DATE NOT NULL,
    end_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_budget_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_budget_category
        FOREIGN KEY (category_id)
        REFERENCES transaction_categories(category_id)
        ON DELETE SET NULL
);

-- Create user_preferences table
CREATE TABLE user_preferences (
    user_id INT PRIMARY KEY,
    theme VARCHAR(20) NOT NULL DEFAULT 'LIGHT',
    dashboard_layout TEXT,
    language VARCHAR(10) NOT NULL DEFAULT 'en',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_preferences_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- Create account_owners table for joint accounts
CREATE TABLE account_owners (
    account_id INT NOT NULL,
    user_id INT NOT NULL,
    permission_level VARCHAR(20) NOT NULL DEFAULT 'FULL',
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, user_id),
    CONSTRAINT fk_owner_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(account_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_owner_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- Insert default transaction categories
INSERT INTO transaction_categories (name, description, icon, color, is_system) VALUES
('Income', 'Money received', 'fa-arrow-down', '#4CAF50', TRUE),
('Salary', 'Regular employment income', 'fa-briefcase', '#4CAF50', TRUE),
('Bonus', 'Additional employment income', 'fa-gift', '#4CAF50', TRUE),
('Investment', 'Income from investments', 'fa-chart-line', '#4CAF50', TRUE),
('Expense', 'Money spent', 'fa-arrow-up', '#F44336', TRUE),
('Food', 'Groceries and dining', 'fa-utensils', '#F44336', TRUE),
('Transportation', 'Public transport and fuel', 'fa-car', '#F44336', TRUE),
('Housing', 'Rent, mortgage, and utilities', 'fa-home', '#F44336', TRUE),
('Entertainment', 'Movies, games, and hobbies', 'fa-film', '#F44336', TRUE),
('Shopping', 'Clothing and general shopping', 'fa-shopping-bag', '#F44336', TRUE),
('Health', 'Medical expenses and insurance', 'fa-heartbeat', '#F44336', TRUE),
('Education', 'Tuition and learning materials', 'fa-graduation-cap', '#F44336', TRUE),
('Travel', 'Vacations and trips', 'fa-plane', '#F44336', TRUE),
('Transfer', 'Money transfers between accounts', 'fa-exchange-alt', '#2196F3', TRUE),
('Withdrawal', 'Cash withdrawals', 'fa-money-bill', '#FF9800', TRUE),
('Deposit', 'Cash deposits', 'fa-piggy-bank', '#9C27B0', TRUE),
('Other', 'Miscellaneous transactions', 'fa-question-circle', '#607D8B', TRUE);

-- Update parent category relationships
UPDATE transaction_categories SET parent_category_id = (SELECT category_id FROM transaction_categories WHERE name = 'Income') 
WHERE name IN ('Salary', 'Bonus', 'Investment');

UPDATE transaction_categories SET parent_category_id = (SELECT category_id FROM transaction_categories WHERE name = 'Expense') 
WHERE name IN ('Food', 'Transportation', 'Housing', 'Entertainment', 'Shopping', 'Health', 'Education', 'Travel');
