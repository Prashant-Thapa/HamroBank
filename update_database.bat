@echo off
REM Script to update the Hamro Bank database schema

echo Updating Hamro Bank database schema...

REM Check if mysql command is available
where mysql >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Error: MySQL client not found. Please install MySQL client.
    exit /b 1
)

REM Prompt for MySQL credentials
set /p mysql_user=Enter MySQL username (default: root):
if "%mysql_user%"=="" set mysql_user=root

set /p mysql_password=Enter MySQL password:

REM Set database name
set db_name=hamro_bank

REM Check if database exists
echo Checking if database exists...
mysql -u %mysql_user% -p%mysql_password% -e "USE %db_name%" 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Database '%db_name%' does not exist. Creating it...
    mysql -u %mysql_user% -p%mysql_password% -e "CREATE DATABASE %db_name%"

    REM Run the full database script
    echo Initializing database schema...
    mysql -u %mysql_user% -p%mysql_password% %db_name% < src\main\resources\database.sql

    if %ERRORLEVEL% EQU 0 (
        echo Database initialized successfully.
    ) else (
        echo Error initializing database.
        exit /b 1
    )
) else (
    REM Run the alter scripts to update the database schema
    echo Updating existing database schema...
    mysql -u %mysql_user% -p%mysql_password% %db_name% < src\main\resources\alter_users_table.sql
    mysql -u %mysql_user% -p%mysql_password% %db_name% < src\main\resources\alter_users_table_active.sql

    if %ERRORLEVEL% EQU 0 (
        echo Database schema updated successfully.
    ) else (
        echo Error updating database schema.
        exit /b 1
    )
)

echo Database update completed.
