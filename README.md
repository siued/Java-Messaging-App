# Java messaging app

[//]: # (![Compile workflow]&#40;https://github.com/siued/JavaMessageApp/actions/workflows/compile.yml/badge.svg&#41;)

This is a small messaging app written in Java. It uses a Spring Boot backend and will have multiple frontends in the future, including a Java CLI client, Java Swing client and a web client.
It is currently a work in progress, but I am planning to keep working on it to showcase my skills and learn new things. An approximate roadmap is in ```TODO.md```. 

## Running the app

### Command-line version
The app was written in Java 20.0, so compatibility with lower Java version is not guaranteed. To run the application, simply run the ```message_client.MessageClient``` class. The command-line interface will let you interact with the app. For a list of commands, type 'help'. 


## Database schema
![Database schema](db_diagram.png)

The database runs in MongoDB due to ease of use relative to SQL-based databases. The database schema is shown above.  
The three collections contain all the necessary data for the server to run the messaging application. There are better ways to implement this database for heavier workloads and large amounts of data, but this serves fine for a small-scale deployment. 