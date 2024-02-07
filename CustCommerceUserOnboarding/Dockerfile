FROM maven:3.8.3-openjdk-17
VOLUME /tmp
ADD target/CustCommerceUserOnboarding-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
