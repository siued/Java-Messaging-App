# Java messaging app

This is a small messaging app written in Java. It uses a Spring Boot backend and will have multiple frontends in the future, including a Java Swing client and a web client.
It is currently a work in progress, but I am planning to keep working on it to showcase my skills and learn new things. An approximate roadmap is in ```TODO.md```. 

## Running the app

## 

## Database schema
![Database schema](db_diagram.png)

The database runs in MongoDB due to ease of use relative to SQL-based databases. The database schema is shown above.  
The three collections contain all the necessary data for the server to run the messaging application. There are better ways to implement this database for heavier workloads and large amounts of data, but this serves fine for a small-scale deployment. 