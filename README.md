# Certification API
Course certification system is implemented at a very basic level. It provides the facilities for user
registration, courses creation and course registration.

## Usage
There will be two types of users, admin and student, they need to get the access token to access the APIs. Access token
can be obtained during sign up or sign in. Course creation and modification is restricted to admin user only.

The API provides the filter option during search by passing 'search' parameter in request. It looks like this:
localhost:8080/courseRegistrations?search=result:PASSED

Notice that an assumption is used that a student can register for a course multiple times if he is not passed yet.

## Requirements
1. JDK 8
2. Maven
3. MySQL  
4. Redis

## Deployment
1. Make copy of `application.yml.sample` as `application.yml` inside `src/main/resources/` directory.
2. Set necessary values inside the respective property files (DB details, Redis details, listening port etc.)
3. Build the project using maven goal `install` on pom.
4. Application jar will be present inside `target/` directory.
5. Deploy & run the executable jar using `java -jar xyz.jar`.