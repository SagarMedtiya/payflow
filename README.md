## How to Run the Application
 
```bash
# Clone the repository
git clone <repository-url>
 
# Navigate to project directory
cd payflow
 
# Run with Maven
mvn spring-boot:run
 
# or build and run JAR
mvn clean package
java -jar target/payflow-1.0.0.jar
```
 
The application will start on `https://localhost:8080`
 
---
 
## Layer Responsibilities
 
### Entity Layer
Defines data modules as JPA entities, mapping Java objects to database.
 
### Repository Layer
Provides CRUD operations and custom query methods using Spring Data JPA.
 
### Service Layer
Contains business logic, orchestrates repositories, and handles transactions.
 
### Controller Layer
Handles HTTP requests, validates input, and returns JSON responses.
 
---
 
## Spring Boot Features in PayFlow
 
### 1. Embedded Server (Tomcat)
PayFlow runs without external server setup. The `spring-boot-starter-web` dependency includes Tomcat, which starts automatically when running the application.
 
### 2. Auto-Configuration
Spring Boot automatically configures H2 database, JPA, and web components based on `pom.xml`. No manual XML configuration needed.
 
### 3. Production-Ready Defaults
The `application.properties` file provides sensible defaults like H2 console access, SQL logging, and JSON serialization out of the box.
 
---
 
## SQL Generated for `findByUpiId`
 
```sql
select u1_0.user_id, u1_0.balance, u1_0.name, u1_0.phone_number, u1_0.upi_id
from users u1_0
where u1_0.upi_id=?
```
 
**Explanation:**
 
a) JPA derives this from the method name `findByUpiId` — "findBy" indicates a SELECT query, and "UpiId" maps to the `upiId` field in the User entity.
 
b) The `?` placeholder represents a parameter in a prepared statement. It gets replaced with the actual UPI ID value when the query executes, preventing SQL injection.
 
---
 
## cURL Commands & Testing
 
### 1. Register User — `POST /users`
 
```bash
# Successful request with @RequestBody
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"}'
 
# Response
{"userId":1,"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"}
 
# Register second user
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Amit Kumar","upiId":"amit@okaxis","balance":3000.0,"phoneNumber":"9876543211"}'
```
 
### 2. Get All Users — `GET /users`
 
```bash
curl -X GET http://localhost:8080/users
 
# Response
[{"userId":1,"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"},{"userId":2,"name":"Amit Kumar","upiId":"amit@okaxis","balance":3000.0,"phoneNumber":"9876543211"}]
```
 
### 3. Get User by ID — `GET /users/{id}`
 
```bash
curl -X GET http://localhost:8080/users/1
 
# Response
{"userId":1,"name":"Priya Sharma","upiId":"priya@okaxis","balance":5000.0,"phoneNumber":"9876543210"}
```
 
### 4. Send Money — `POST /transactions`
 
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"senderUpiId":"priya@okaxis","receiverUpiId":"amit@okaxis","amount":200.0,"note":"dinner split"}'
 
# Response
{"transactionId":1,"senderUpiId":"priya@okaxis","receiverUpiId":"amit@okaxis","amount":200.0,"note":"dinner split"}
```
 
---
 
## Comparison of Custom Query Approaches
 
| Approach | Example | Pros | Cons |
|---|---|---|---|
| Derived Method Names | `findByUpiId(String upiId)` | Simple, no JPQL needed, compile-time checking | Limited to simple queries, complex joins difficult |
| `@Query` with JPQL | `@Query("SELECT u FROM User u WHERE u.balance > :min")` | Database-independent, supports complex queries, type-safe | Requires learning JPQL syntax |
| Native SQL | `@Query(value = "SELECT * FROM users WHERE balance > ?1", nativeQuery=true)` | Full SQL power, database-specific features | Ties code to specific database, no JPA entity mapping, risk of SQL injection |
 
### Why Native Queries Are Least Preferred
 
Native queries break database portability, bypass JPA's first-level cache and persistence context, require manual result mapping, and can lead to SQL injection if parameters aren't properly handled. They should only be used for database-specific features or complex performance optimizations when absolutely necessary.
 
---
 
## Conceptual Write-up
 
### 1. Request Lifecycle
When curl sends `POST /users` with JSON, the request first hits the embedded Tomcat server. Tomcat forwards it to Spring's `DispatcherServlet`, which acts as the front controller. The `DispatcherServlet` consults `HandlerMapping` to find which controller method handles `/users POST`. It then uses the `HandlerAdapter` to invoke `UserController.registerUser()`, passing the deserialized User object. The `HandlerAdapter` also manages argument resolution (like `@RequestBody`) and return value handling before sending the HTTP response back.
 
### 2. Serialization
The `@RequestBody` annotation triggers Spring's `MappingJackson2HttpMessageConverter`, which uses the Jackson library to convert JSON to a Java User object. If the JSON key is `"upi_id"` but the Java field is `upiId` (camelCase), Jackson's default naming strategy won't match. The field would be null unless you add `@JsonProperty("upi_id")` on the field or configure Spring to use snake_case with `spring.jackson.property-naming-strategy=SNAKE_CASE`.
 
### 3. Spring Boot Features
- **Embedded Server:** Tomcat is embedded in PayFlow — no separate server installation needed.
- **Auto-Configuration:** Based on dependencies in `pom.xml`, Spring Boot automatically configures H2 database, JPA, and JSON converters without any XML configuration.
- **Production-Ready Defaults:** The `application.properties` file provides health checks, H2 console access, and SQL logging out of the box.
### 4. Spring vs. Spring Boot
With plain Spring, you would need to manually configure the `DispatcherServlet` in `web.xml`, component scanning via XML or Java config, ViewResolvers, and JPA `EntityManagerFactory` with DataSource beans. You'd also need to set up H2 database connections manually and configure transaction management. Spring Boot auto-configuration eliminates all this boilerplate — dependencies alone determine the configuration, with sensible defaults that can be overridden via properties files.
 
### 5. Stateless REST
Stateless means each HTTP request contains all necessary information for the server to process it independently — the server doesn't store any session information between requests. For PayFlow running on three servers behind a load balancer, statelessness means any server can handle any request. Without a stateless design, requests would break if a user's second request went to a different server that didn't have their session data. Stateless design allows horizontal scaling where each server is identical and requests can be freely distributed.
 
### 6. Persistence
If transactions were stored in a Java List, every server restart would wipe all transaction records. This is unacceptable for a payment app because financial transactions must be durable and auditable. Loss of transaction data means losing evidence of money transfers, inability to resolve disputes, incorrect account balances, and potential regulatory violations. Payment apps require ACID properties — specifically durability — which databases provide by persisting data to disk even after power loss or restart.












   
