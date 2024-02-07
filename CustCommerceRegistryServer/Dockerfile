FROM maven:3.8.3-openjdk-17
VOLUME /tmp
ADD target/CustCommerceRegistryServer-1.0.0.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]