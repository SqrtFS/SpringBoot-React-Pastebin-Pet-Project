FULL STACK PROJECT But with its features, each post is unique and has its own hash, 
each user has their own post, for processing huge data and also for convenience, S3 storage is used. 
Minio can be replaced with other storage such as Amazon and Google, added async Spring events for background solutions.
Spring events can be replaced with Kafka, depending on your taste, JWT is used for security.
and the frontend is on React JS. And the final part can be done with Docker Compose.


> Live Demo: (Add link if deployed) | **API Docs:** http://localhost:8080/swagger-ui.html

---

🛠 Technology Stack

Backend
* **Language:** Java 17
* **Framework:** Spring Boot 3
* **Security:** Spring Security, JWT (JSON Web Tokens)
* **Data:** Spring Data JPA, Hibernate
* **Testing:** JUnit 5, Mockito, (Testcontainers - optional)

Frontend
* Library: React.js
* Build Tool: Vite
* Routing: React Router DOM
* HTTP Client: Axios

Infrastructure
* Database: MySQL
* Cache: Redis
* Object Storage:** MinIO
* DevOps: Docker, Docker Compose, Maven

🐳 Getting Started (Run with Docker)

You can run the entire infrastructure with a single command.




HOW TO RUN ->
--------------Steps--------------

before starting be sure that you filled jwt secret key just generate in a any web site base64 and put inside (INSIDE THE FILE .env.example !!!)

1.  Clone the repository:
    ```bash
    git clone [https://github.com/YOUR_USERNAME/SpringBoot-React-Pastebin.git](https://github.com/YOUR_USERNAME/SpringBoot-React-Pastebin.git)
    cd SpringBoot-React-Pastebin
    ```

2.  Build and Run:
    ```bash
    docker compose up -d --build
    ```

3.  Access the Application:**
     Frontend: [http://localhost](http://localhost)
      Backend API: [http://localhost:8080](http://localhost:8080)
       MinIO Console: [http://localhost:9090](http://localhost:9090) (User/Pass: `minio` / `minioroot`)
    
i recommend to you to connect DB with DBeaver tool

---

📂 Project Structure
```text
Pastebin-Pet-Project/
├── api/                 # Spring Boot REST Controllers & Config
├── core/                # Domain Entities, Services, Repositories
├── frontend-reactjs/    # React Application
├── docker-compose.yml   # Infrastructure orchestration
└── pom.xml              # Maven Parent POM
```


![creator](https://img.shields.io/badge/Created_by-Sultanmurat_Yeldar-blue?style=for-the-badge)

