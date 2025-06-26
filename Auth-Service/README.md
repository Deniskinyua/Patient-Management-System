## Authentication Service

> Authentication Service & API Gateway Integration
- To route all authentication requests, the routes are added to the API gateway's application.properties/yaml.
- Remember to remove the authentication service ports from docker compose to prevent access of the service from external
requests. 
- Use the format:
````
spring:
  cloud:
    gateway:
      routes:
        - id: authentication-service-route
          uri: http://<auth service host name>:<port>
          predicates:
            - Path=</path/**>
````