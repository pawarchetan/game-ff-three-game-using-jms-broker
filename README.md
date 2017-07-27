# takeaway

# Technology Stack :
  - Java 8
  - ActiveMQ
  - Lombok 
  - Maven
  
# Implementation Details :
  - Playes will use JMS to communication with each other through JMS Broker.
  - each queue is identified by player name.
  - Implementation contains 2 main project / sorce code repositories.
    - gameofthree - contains source code for gameofthree logic
    - gameofthreejms - contains source code for gameofthree jms broker

# Future Scope :
  - More Test Coverage

# How To Run :
  - start JMS Broker
    - mvn spring-boot:run
  - start first player with profile - p1
    - mvn spring-boot:run -Dspring.profiles.active=p1
  - start second player with profile - p2
    - mvn spring-boot:run -Dspring.profiles.active=p2
    
