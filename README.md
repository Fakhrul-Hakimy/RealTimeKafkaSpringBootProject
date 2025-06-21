
## Title of your application (a unique title)
## Abstract (in 300 words)

   1. Background
      
      With the rapid growth of video content on platforms like YouTube, timely and scalable analysis of video data is crucial for content creators and marketers to make informed decisions. Distributed real-time data processing systems enable continuous analysis of streaming data, which traditional batch systems cannot efficiently support.
  
   3. Problem Statement (from article)
      
      Batch processing systems process large datasets in scheduled intervals, which can lead to delays, late-arriving data issues, and inconsistencies across data copies. These limitations restrict timely decision-making and real-time insights. In contrast, real-time streaming systems, such as those built with Apache Kafka, provide low-latency and continuous data processing capabilities. However, implementing such systems requires overcoming challenges related to scalability, fault tolerance, and data consistency in distributed environments (Waehner, 2025; Eyer, 2025; GeeksforGeeks, 2025).
      
   5. Main objective
      
      This project aims to design and implement a distributed real-time pipeline to analyze YouTube video metadata. The system retrieves video data through the YouTube Data API, streams the data via Kafka, filters and routes comments based on content length to either a Telegram Bot or a consumer web app, and visualizes analytics through Spring Boot applications.
      
   7. Methodology
      
      Using a microservices architecture containerized by Docker, the project consists of five components: a Spring Boot producer application, Kafka streaming platform, Telegram Bot, MySQL database, and Spring Boot consumer application. Data flows from user input through producer to Kafka, where conditional logic directs the stream to appropriate consumers for real-time processing and analysis.
   
   9. Result
       
       The system effectively handles real-time YouTube data ingestion, processing, and analytics presentation. Kafka’s distributed streaming ensures scalability and reliability, while the Telegram Bot provides instant notifications for specific comment types.
       
   11. Conclusion
       
       This project demonstrates that integrating real-time streaming technologies and microservices architecture can address the limitations of batch processing in YouTube data analytics, delivering scalable, timely, and interactive data insights.


       

## System Architecture (MUST be included in your presentation)

![Blank board](https://github.com/user-attachments/assets/21d7bd2e-8074-4d83-a4e8-f7718dbf1d98)


## Link for Docker Image

## Instructions on how to run Docker.
### Prerequisites
- Docker and Docker Compose installed on your system
- Git to clone the repository

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-repo/youtube-analytics.git
   cd youtube-analytics
   ```

2. **Create environment variables file**
   Create a `.env` file in the `docker` directory with the following variables:
   ```
   YOUTUBE_API_KEY=your_youtube_api_key
   TELEGRAM_BOT_TOKEN=your_telegram_bot_token
   TELEGRAM_BOT_USERNAME=your_bot_username
   TELEGRAM_CHAT_ID=your_chat_id
   ```

3. **Start the application**
   ```bash
   cd docker
   docker-compose up -d
   ```

4. **Verify services are running**
   ```bash
   docker-compose ps
   ```

5. **Access the applications**
   - Producer API: http://localhost:8080
   - Consumer Dashboard: http://localhost:8081

6. **View logs**
   ```bash
   # View logs for all services
   docker-compose logs -f
   
   # View logs for a specific service
   docker-compose logs -f spring-producer
   ```

7. **Stop the application**
   ```bash
   docker-compose down
   ```

### Troubleshooting

- If services fail to start, check logs with `docker-compose logs`
- Ensure all required environment variables are set in the `.env` file
- Verify that ports 8080, 8081, and 9092 are not already in use


## List of all the endpoints

## User manual/guideline for system configuration (if any)

## User manual/guideline for testing the system

## Link for the YouTube Presentation

## Result/Output (Screenshot of the output)

## References (Not less than 20)
1. Amazon. (2022). Amazon Elastic Compute Cloud Documentation. Amazon.com. https://docs.aws.amazon.com/ec2/
2. Baeldung. (2020, September 29). Introduction to Apache Kafka with Java. https://www.baeldung.com/kafka
3. Confluent. (n.d.). Java client for Apache Kafka. Confluent, Inc. Retrieved May 23, 2025, from https://docs.confluent.io/kafka-clients/java/current/overview.html
4. Docker, Inc. (n.d.). Docker documentation. https://docs.docker.com/
5. Eyer, A. (2025). Real-time data stream processing scalability guide. Eyer AI Blog. https://www.eyer.ai/blog/real-time-data-stream-processing-scalability-guide/
6. Fernandez, F. (2024, March 8). Building a YouTube data analysis Telegram Bot powered by Kafka and ksqlDB [Blog post]. Medium. Retrieved June 15, 2025, from https://medium.com/@ofelipefernandez/building-a-youtube-data-analysis-telegram-bot-powered-by-kafka-and-ksqldb-9b6ee430f2c8
7. Fadatare, R. (2024, May). Spring Boot with Apache Kafka using Docker Compose: a step‑by‑step tutorial. Java Guides. Retrieved June 15, 2025, from https://www.javaguides.net/2024/05/spring-boot-with-apache-kafka-using-docker-compose.html
8. GeeksforGeeks. (2021, March 18). Apache Kafka in Java. https://www.geeksforgeeks.org/apache-kafka-in-java/
9. GeeksforGeeks. (2025). Real-time data processing: Challenges and solutions for streaming data. https://www.geeksforgeeks.org/real-time-data-processing-challenges-and-solutions-for-streaming-data/
10. Kreps, J., Narkhede, N., & Rao, J. (2011). Kafka: A Distributed Messaging System for Log Processing. Proceedings of the NetDB. Retrieved from https://dl.acm.org/doi/10.1145/1966445.1966449
11. Oracle. (n.d.). Java SE documentation. https://docs.oracle.com/en/java/javase/
12. Waehner, K. (2025, April 1). The top 20 problems with batch processing and how to fix them with data streaming. Kai Waehner Blog. https://www.kai-waehner.de/blog/2025/04/01/the-top-20-problems-with-batch-processing-and-how-to-fix-them-with-data-streaming/
13. Venturelli, I. (2024, August 5; last updated March 4, 2025). Spring Boot and Kafka: Real-time data processing. Retrieved from https://igventurelli.io/spring-boot-and-kafka-real-time-data-processing/
14. Vogels, W. (2020, January 6). Eventually consistent: Building reliable distributed systems. ACM Queue. https://queue.acm.org/detail.cfm?id=945134
15. Apache Kafka. (n.d.). Documentation. The Apache Software Foundation. Retrieved May 23, 2025, from https://kafka.apache.org/documentation/
16. Apache Software Foundation. (n.d.). Kafka KRaft mode documentation (KIP-500). https://cwiki.apache.org/confluence/display/KAFKA/KIP-500%3A+Replace+ZooKeeper+with+a+Self-Managed+Metadata+Quorum
17. Apache Software Foundation. (n.d.). Apache Kafka quickstart. https://kafka.apache.org/quickstart
18. Spring Boot :: Spring Boot. (n.d.). Docs.spring.io. https://docs.spring.io/spring-boot/index.html
19. Kreps, J., Narkhede, N., & Rao, J. (2011). Kafka: A Distributed Messaging System for Log Processing. Proceedings of the NetDB. Retrieved from https://dl.acm.org/doi/10.1145/1966445.1966449
20. Apache Software Foundation. (n.d.). Apache Kafka documentation. https://kafka.apache.org/documentation/

‌

## User manual for installing your application on Cloud (Bonus 5%)


