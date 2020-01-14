# Web Engineering 2019-2020 / URL Shortener

[![Build Status](https://travis-ci.org/AlexGuti14/UrlShortener.svg?branch=master)](https://travis-ci.org/AlexGuti14/UrlShortener)

This is the start repository for the project developed in this course. 

The __project__ is a [Spring Boot](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that offers a minimum set of functionalities shared by all subprojects.

* __Short URL creation service__:  `POST /` creates a shortened URL from a URL in the request parameter `url`.
* __Short URL list service__:  `GET /list` return a list of shortened URLs in db.
* __Short URL QR service__:  `GET /qr` return a qr of shortened URL associated with the parameter `hash`.
* __Redirection service__: `GET /{id}` redirects the request to a URL associated with the parameter `id`.
* __Database service__: Persistence and retrieval of `ShortURL` and `Click` objects.
* __CSV service__: `WebSockets` creates a shortened URLs from a CSV with URLs.


The application can be run as follows:

### DOCKER:

1) Create file .jar
```
.\gradlew build
```

or

```
gradle build
```

2) Create docker image

```
docker build -t urlshortener .
```

3) Up docker-compose

```
docker-compose up -d
```

4) Copy .sql in docker master container
```
docker cp schema.sql mysql-url:/schema.sql
```

5) Create tables in Master db. Automatically this db is copied in Slave container
```
docker exec -i -t mysql-url /bin/bash
mysql -uroot -proot url < schema.sql
```


DOWN DOCKER:
1) Down docker-compose

```
docker-compose down -v
```


OTHER DOCKER COMMANDS:

docker images: view docker images

docker ps: list of execute container

docker rm: remove container

docker rmi: remove image

docker search: search image

docker pull: download image