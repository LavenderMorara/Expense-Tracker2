💰 Personal Finance Tracker (Java Full Stack Project)
📌 Overview

The Personal Finance Tracker is a full-stack Java-based web application that helps users manage their finances by tracking expenses, setting budgets, and analyzing spending patterns.

This system is designed to promote better financial habits by providing clear insights into where money is being spent and helping users stay within their budget limits.

🎯 Objectives
Enable users to record and manage daily expenses
Provide budgeting tools to control spending
Generate reports and insights on financial behavior
Offer a simple and intuitive interface for financial tracking
🧠 Features
✅ User Management
User registration and login
Secure authentication
Personalized dashboard
✅ Expense Tracking
Add, edit, and delete expenses
Categorize expenses (Food, Rent, Transport, etc.)
View expense history
✅ Budget Management
Set monthly budgets per category
Track spending against budgets
Alerts when approaching/exceeding limits
✅ Categories
Predefined categories (Food, Rent, Transport, Entertainment)
Option to create custom categories
✅ Reports & Analytics
Total spending summary
Category-wise breakdown
Monthly reports
Visual charts (bar/pie charts)
🏗️ System Architecture
🔹 Backend
Java
Spring Boot (REST API)
🔹 Frontend
HTML, CSS, JavaScript (or React for advanced implementation)
🔹 Database
MySQL
🔹 Tools
Git & GitHub
Postman (API testing)
🧱 System Design
Key Entities:
User
Expense
Budget
Category
Relationships:
A user can have multiple expenses
A user can set multiple budgets
Each expense belongs to one category
🗄️ Database Schema
Users Table
Field	Type
id	INT (PK)
name	VARCHAR
email	VARCHAR
password	VARCHAR
Expenses Table
Field	Type
id	INT (PK)
user_id	INT (FK)
amount	DOUBLE
category	VARCHAR
date	DATE
description	TEXT
Budgets Table
Field	Type
id	INT (PK)
user_id	INT (FK)
category	VARCHAR
limit_amount	DOUBLE
Categories Table
Field	Type
id	INT (PK)
name	VARCHAR
🔄 System Inputs & Outputs
Inputs:
User credentials (login/register)
Expense details (amount, category, date, description)
Budget limits
Outputs:
Expense records
Budget alerts
Financial summaries
Visual reports (charts)
⚙️ Installation & Setup
Prerequisites:
Java JDK 17+
Maven
MySQL
IDE (IntelliJ / Eclipse / VS Code)
Steps:

Clone the repository:

git clone https://github.com/your-username/personal-finance-tracker.git

Navigate to the project directory:

cd personal-finance-tracker

Configure database in application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=yourpassword

Run the application:

mvn spring-boot:run

Open in browser:

http://localhost:8080
🧪 Sample Use Case
User logs in
Adds expenses (e.g., Food – KES 500)
Sets monthly budget (e.g., Food – KES 5000)
System tracks spending
User views monthly report and insights
🚀 Future Enhancements
Email notifications for budget alerts
Export reports (PDF/Excel)
Mobile app integration
AI-based spending insights
Multi-currency support
📊 Difficulty Level

Intermediate to Advanced (6.5/10)

Covers Object-Oriented Programming
Database design & integration
REST API development
Frontend-backend interaction