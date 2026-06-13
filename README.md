How to Run the Application

# Clone the repository
git clone <repository-url>

# Navigate to project directory
cd payflow

# Run with Maven
mvn spring-boot:run

# or build and run JAR
mvn clean package
java -jar target/payflow-1.0.0.jar

Th application will start on https://localhost:8080

Layer Responsibilities

Entity Layer
Defines data modules as JPA entities, mapping Java objects to database

Repository Layer
Provides CRUD operations and custom query methods using spring Data JPA

Service Layer
Contains business logic, orchestrates repositories, and handles transactions.

Controller Layer
Handles HTTP requests, validates input, and returns JSON responses.

Spring Boot features in PayFlow
1. Embedded Server (Tomcat)
   
   PayFlow runs without external server setup. The spring-boot-starter-web dependency includes Tomcat, which starts automatically when running the aoplication.
2. Auto-Configuration
   Spring Boot automatically configures H2 database, JPA, web components based on pom.xml. No manual XML configuration needed.
  
3. Production-Ready Default
   The application.properties file provides, sensible defaults like H2 console access, SQL Logging and JSON serialization out of box.

SQL Generated for findByUpiId:
select u1_0.user_id,u1_0.balance,u1_0.name,u1_0.phone_number,u1_0.upi_id 
from users u1_0 
where u1_0.upi_id=?

Explanation:
a) JPA derives this from the method name findByUpiId - "findBy" indicates a SELECT query, and "UpiId" maps to the upiId filed in the User entity.

b) The ? placeholder represents a parameter in prepared statement - it gets replaced with the actual UPI ID value when the query executes, preventing SQL injection.

cURL Commands & Testing

1. Register User (POST /users)
# Successful request with @RequestBody
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"}'

# Response: {"userId":1,"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"}

# Register second user
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Amit Kumar","upiId":"amit@okaxis","balance":3000.0,"phoneNumber":"9876543211"}'

2. Get All Users (GET /users)

curl -X GET http://localhost:8080/users

# Response: [{"userId":1,"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"},{"userId":2,"name":"Amit Kumar","upiId":"amit@okaxis","balance":3000.0,"phoneNumber":"9876543211"}]

3. Get User by ID (GET /users/{id})

curl -X GET http://localhost:8080/users/1

# Response: {"userId":1,"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"}

4. Send Money (POST /transactions)
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"senderUpiId":"priya@okaxis","receiverUpiId":"amit@okaxis","amount":200.0,"note":"dinner split"}'

# Response: {"transactionId":1,"senderUpiId":"priya@okaxis","receiverUpiId":"amit@okaxis","amount":200.0,"note":"dinner split"}
