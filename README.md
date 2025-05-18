# Hamro Bank - Online Banking System

Hamro Bank is a secure, scalable, and user-friendly full-stack web application for online banking. It is built using Java, JSP, Java EE, and MySQL, following the MVC architecture.

## Features

### User Authentication and Security
- Secure user registration with password hashing using BCrypt
- Login session management using cookies
- Role-based access control (admin vs. customer)

### Customer Dashboard
- View account balance
- Transfer funds to other users
- View transaction history
- Download receipts for transactions
- View and update profile information
- Upload and manage profile picture
- Deactivate account when no longer needed

### Admin Dashboard
- Manage customer accounts (add, update, delete)
- Activate/deactivate user accounts
- Monitor user activity logs
- Access summarized reports for decision-making
- View and update profile information
- Upload and manage profile picture

## Technology Stack

- **Backend**: Java, Java EE, Servlets, JSP
- **Frontend**: HTML, CSS (plain CSS, no external libraries)
- **Database**: MySQL
- **Security**: BCrypt for password hashing
- **Architecture**: MVC (Model-View-Controller)

## Setup Instructions

### Prerequisites
- JDK 17 or higher
- Apache Tomcat 10 or higher
- MySQL 8.0 or higher
- Maven 3.8 or higher

### Database Setup
1. Use the provided script to set up or update the database:

```bash
./update_database.sh
```

This script will:
- Create the database if it doesn't exist
- Run the full database script if it's a new installation
- Update the schema if the database already exists

Alternatively, you can manually set up the database:

```bash
# Create the database
mysql -u root -p -e "CREATE DATABASE hamro_bank"

# Initialize the database
mysql -u root -p hamro_bank < src/main/resources/database.sql

# Update existing database (if needed)
mysql -u root -p hamro_bank < src/main/resources/alter_users_table.sql
mysql -u root -p hamro_bank < src/main/resources/alter_users_table_active.sql
```

### Configuration
1. Open `src/main/java/com/example/hamrobank/util/DatabaseUtil.java`
2. Update the database connection parameters:
   - `JDBC_URL`: JDBC URL for your MySQL database
   - `JDBC_USER`: MySQL username
   - `JDBC_PASSWORD`: MySQL password

3. Add a default profile picture:
   - Create a PNG image named `default-profile.png`
   - Place it in the `src/main/webapp/images/` directory
   - Recommended size: 200x200 pixels

### Building and Running
1. Clone the repository
2. Navigate to the project directory
3. Build the project using Maven:

```bash
mvn clean package
```

4. Deploy the generated WAR file to Tomcat
5. Access the application at `http://localhost:8080/HamroBank`

## Default Users

The system comes with two default users:

### Admin
- Username: admin
- Password: admin123

### Customer
- Username: customer
- Password: customer123

## Project Structure

```
HamroBank/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── hamrobank/
│   │   │               ├── controller/  # Servlets
│   │   │               ├── dao/         # Data Access Objects
│   │   │               ├── filter/      # Servlet Filters
│   │   │               ├── model/       # Java Beans
│   │   │               └── util/        # Utility Classes
│   │   ├── resources/
│   │   │   └── database.sql  # Database Schema
│   │   └── webapp/
│   │       ├── css/          # CSS Stylesheets
│   │       ├── WEB-INF/
│   │       │   ├── views/    # JSP Pages
│   │       │   └── web.xml   # Web Configuration
│   │       └── index.jsp     # Landing Page
│   └── test/                 # Unit Tests
├── pom.xml                   # Maven Configuration
└── README.md                 # This File
```

## Security Features

- Passwords are hashed using BCrypt before storing in the database
- Session management to prevent unauthorized access
- Role-based access control to restrict access to specific resources
- Input validation to prevent SQL injection and XSS attacks
- CSRF protection for form submissions
- Account deactivation for enhanced privacy and security

## UI Design

The user interface is designed to be attractive, clean, and intuitive, using only plain CSS (no external libraries or frameworks). Key UI features include:

- Responsive design that works on desktop and mobile devices
- Consistent color scheme and typography
- Clear navigation and user feedback
- Interactive elements with hover and focus states
- Accessible form controls and error messages

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- This project was developed as part of a software development course
- Special thanks to all contributors and testers
