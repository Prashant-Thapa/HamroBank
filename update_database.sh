#!/bin/bash

# Script to update the database schema for Hamro Bank

echo "Updating Hamro Bank database schema..."

# Check if mysql command is available
if ! command -v mysql &> /dev/null; then
    echo "Error: MySQL client not found. Please install MySQL client."
    exit 1
fi

# Prompt for MySQL credentials
read -p "Enter MySQL username (default: root): " mysql_user
mysql_user=${mysql_user:-root}

read -s -p "Enter MySQL password: " mysql_password
echo

# Set database name
db_name="hamro_bank"

# Check if database exists
echo "Checking if database exists..."
if ! mysql -u "$mysql_user" -p"$mysql_password" -e "USE $db_name" 2>/dev/null; then
    echo "Database '$db_name' does not exist. Creating it..."
    mysql -u "$mysql_user" -p"$mysql_password" -e "CREATE DATABASE $db_name"

    # Run the full database script
    echo "Initializing database schema..."
    mysql -u "$mysql_user" -p"$mysql_password" "$db_name" < src/main/resources/database.sql

    if [ $? -eq 0 ]; then
        echo "Database initialized successfully."
    else
        echo "Error initializing database."
        exit 1
    fi
else
    # Run the alter scripts to update the database schema
    echo "Updating existing database schema..."
    mysql -u "$mysql_user" -p"$mysql_password" "$db_name" < src/main/resources/alter_users_table.sql
    mysql -u "$mysql_user" -p"$mysql_password" "$db_name" < src/main/resources/alter_users_table_active.sql

    if [ $? -eq 0 ]; then
        echo "Database schema updated successfully."
    else
        echo "Error updating database schema."
        exit 1
    fi
fi

echo "Database update completed."
