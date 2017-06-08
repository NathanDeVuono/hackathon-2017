from maven:3.5-jdk-8

add . /usr/src/app

workdir /usr/src/app

run mvn install

cmd mvn spring-boot:run
