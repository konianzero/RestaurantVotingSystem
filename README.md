# Restaurant Voting System

_A restaurant voting system REST API using Hibernate/Spring/SpringMVC_.

Voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed.

Each restaurant provides new menu each day.

---

**Launch**
```
mvn clean package org.codehaus.cargo:cargo-maven2-plugin:1.8.2:run
```

URL: [http://localhost:8080/voting](http://localhost:8080/voting)

---

## Users

| Name  | Email             | Password  | Roles         |
|-------|-------------------|-----------|---------------|
| Mike  | admin@gmail.com   | admin     | Admin, User   |
| Nick  | user@gmail.com    | userpass  | User          |

---

## API documentation

[Swagger Api Documentation](http://localhost:8080/voting/swagger-ui.html#/)

| API        | Method | Description                             | URL                                                         | User           |
|------------|--------|-----------------------------------------|-------------------------------------------------------------|----------------|
| Admin      | POST   | Create user                             | {URL}/rest/admin/users                                      | Admin          |
|            | GET    | Get user                                | {URL}/rest/admin/users/{userId}                             | Admin          |
|            | GET    | Get user by Email                       | {URL}/rest/admin/users/by?email={email}                     | Admin          |
|            | GET    | Get all user                            | {URL}/rest/admin/users                                      | Admin          |
|            | UPDATE | Update user                             | {URL}/rest/admin/users/{userId}                             | Admin          |
|            | DELETE | Delete user                             | {URL}/rest/admin/users/{userId}                             | Admin          |
| Profile    | POST   | Create user                             | {URL}/rest/profile/register                                 | Not Authorized |
|            | GET    | Get user                                | {URL}/rest/profile                                          | Authorized     |
|            | UPDATE | Update user                             | {URL}/rest/profile                                          | Authorized     |
|            | DELETE | Delete user                             | {URL}/rest/profile                                          | Authorized     |
| Restaurant | POST   | Create restaurant                       | {URL}/rest/restaurants                                      | Admin          |
|            | GET    | Get restaurant                          | {URL}/rest/restaurants/{restaurantId}                       | Authorized     |
|            | GET    | Get restaurant with dishes              | {URL}/rest/restaurants/{restaurantId}/with                  | Authorized     |
|            | GET    | Get all restaurants                     | {URL}/rest/restaurants/                                     | Authorized     |
|            | GET    | Get all restaurants with today's dishes | {URL}/rest/restaurants/today                                | Authorized     |  
|            | PUT    | Update restaurant                       | {URL}/rest/restaurants/{restaurantId}                       | Admin          |
|            | DELETE | Delete restaurant                       | {URL}/rest/restaurants/{restaurantId}                       | Admin          |
| Dish       | POST   | Create dish                             | {URL}/rest/dishes                                           | Admin          |
|            | GET    | Get dish                                | {URL}/rest/dishes/{dishId}                                  | Authorized     |
|            | GET    | Get dish with restaurant                | {URL}/rest/dishes/{dishId}/with                             | Authorized     |
|            | GET    | Get all dishes by restaurant            | {URL}/rest/dishes?restaurantId={restaurantId}               | Authorized     |
|            | GET    | Get all dishes by restaurant and date   | {URL}/rest/dishes?restaurantId={restaurantId}&date={date}   | Authorized     |
|            | PUT    | Update dish                             | {URL}/rest/dishes/{dishId}                                  | Admin          |
|            | DELETE | Delete dish                             | {URL}/rest/dishes/{dishId}                                  | Admin          |
| Vote       | PUT    | Create vote                             | {URL}/rest/votes?restaurantId={restaurantId}                | Authorized     |
|            | GET    | Get vote                                | {URL}/rest/votes/{voteId}                                   | Authorized     |
|            | GET    | Get last vote of user                   | {URL}/rest/votes/last                                       | Authorized     |
|            | PATCH  | Update vote                             | {URL}/rest/votes?restaurantId={restaurantId}                | Authorized     |

---

## CURL commands for the restaurant voting system REST API.

### Restaurant

**Create restaurant**
```
curl -s -X POST -d '{"name":"Restaurant3"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/restaurants --user admin@gmail.com:admin
```
**Get restaurant**
```
curl -s http://localhost:8080/voting/rest/restaurants/100002 --user user@gmail.com:userpass
```
**Get restaurant with dishes**
```
curl -s http://localhost:8080/voting/rest/restaurants/100002/with --user user@gmail.com:userpass
```
**Get all restaurants**
```
curl -s http://localhost:8080/voting/rest/restaurants/ --user user@gmail.com:userpass
```
**Get all restaurants with today's dishes**
```
curl -s http://localhost:8080/voting/rest/restaurants/today --user user@gmail.com:userpass
```
**Update restaurant**
```
curl -s -X PUT -d '{"id":100002,"name":"RestThree"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/restaurants/100002 --user admin@gmail.com:admin
```
**Delete restaurant**
```
curl -s -X DELETE http://localhost:8080/voting/rest/restaurants/100002 --user admin@gmail.com:admin
```

### Dish

**Create dish**
```
curl -s -X POST -d '{"restaurantId":100002,"name":"Sandwich","price":6}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/dishes --user admin@gmail.com:admin
```
**Get dish**
```
curl -s http://localhost:8080/voting/rest/dishes/100004 --user user@gmail.com:userpass
```
**Get dish with a restaurant**
```
curl -s http://localhost:8080/voting/rest/dishes/100004/with --user user@gmail.com:userpass
```
**Get all dishes by restaurant**
```
curl -s http://localhost:8080/voting/rest/dishes?restaurantId=100002 --user user@gmail.com:userpass
```
**Get all dishes by restaurant and date**
```
curl -s "http://localhost:8080/voting/rest/dishes?restaurantId=100002&date=2021-02-06" --user user@gmail.com:userpass
```
**Update dish**
```
curl -s -X PUT -d '{"id":100004,"restaurantId":100002,"name":"Sandwich with tune","price":8}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/dishes/100004 --user admin@gmail.com:admin
```
**Delete dish**
```
curl -s -X DELETE http://localhost:8080/voting/rest/dishes/100004 --user admin@gmail.com:admin
```

### User

#### Admin

**Create user**
```
curl -s -X POST -d '{"name":"New","email":"new@gmail.com","password":"password","enabled":true,"roles":["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/users --user admin@gmail.com:admin
```
**Get user**
```
curl -s http://localhost:8080/voting/rest/admin/users/100001 --user admin@gmail.com:admin
```
**Get user by Email**
```
curl -s http://localhost:8080/voting/rest/admin/users/by?email=user@gmail.com --user admin@gmail.com:admin
```
**Get all users**
```
curl -s http://localhost:8080/voting/rest/admin/users --user admin@gmail.com:admin
```
**Update user**
```
curl -s -X PUT -d '{"id":100001,"name":"NewAdmin","email":"newadmin@gmail.com","password":"password","enabled":true,"roles":["USER","ADMIN"]}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/admin/users/100001 --user admin@gmail.com:admin
```
**Delete user**
```
curl -s -X DELETE http://localhost:8080/voting/rest/admin/users/100001 --user admin@gmail.com:admin
```

#### User

**Register user**
```
curl -s -X POST -d '{"name":"New","email":"new@gmail.com","password":"password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/profile/register
```
**Get user**
```
curl -s http://localhost:8080/voting/rest/profile --user user@gmail.com:userpass
```
**Update user**
```
curl -s -X PUT -d '{"name":"UpdatedUser","email":"updated@gmail.com","password":"passupdate"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/profile --user user@gmail.com:userpass
```
**Delete user**
```
curl -s -X DELETE http://localhost:8080/voting/rest/profile --user user@gmail.com:userpass
```

### Vote

**Create vote**
```
curl -s -X PUT -H 'Content-Type: application/json' http://localhost:8080/voting/rest/votes?restaurantId=100002 --user user@gmail.com:userpass
```
**Get vote**
```
curl -s http://localhost:8080/voting/rest/votes/100013 --user user@gmail.com:userpass
```
**Get last vote of user**
```
curl -s http://localhost:8080/voting/rest/votes/last --user admin@gmail.com:admin
```
**Update vote**
```
curl -s -X PATCH http://localhost:8080/voting/rest/votes?restaurantId=100002 --user admin@gmail.com:admin

```