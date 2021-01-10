### Restaurant Voting System

---
_Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**_.

**The task is**:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

---
#### Users

- Mike (Email: admin@gmail.com, password: admin),
- Nick (Email: user@gmail.com, password: userpass);

---
## CURL commands for the restaurant voting system REST API.

### Restaurant

**Create restaurant**
```
curl -s -X POST -d '{"name":"Restaurant3"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/restaurants --user admin@gmail.com:admin
```
**Get restaurant**
```
curl -s http://localhost:8080/voting/rest/restaurants/100016 --user user@gmail.com:userpass
```
**Get restaurant with a menu**
```
curl -s http://localhost:8080/voting/rest/restaurants/with/100016 --user user@gmail.com:userpass
```
**Get all restaurants**
```
curl -s http://localhost:8080/voting/rest/restaurants/ --user user@gmail.com:userpass
```
**Update restaurant**
```
curl -s -X PUT -d '{"id":100016,"name":"RestThree"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/restaurants/100016 --user admin@gmail.com:admin
```
**Delete restaurant**
```
curl -s -X DELETE http://localhost:8080/voting/rest/restaurants/100016 --user admin@gmail.com:admin
```

### Dish

**Create dish**
```
curl -s -X POST -d '{"name":"Sandwich","price":6,"date":"2020-12-21"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/dishes/100002 --user admin@gmail.com:admin
```
**Create a menu**
```
curl -s -X POST -d '[{"name":"Salad","price":4,"date":"2020-12-21"},{"name":"Tea","price":2,"date":"2020-12-21"}]' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/dishes/menu/100002 --user admin@gmail.com:admin
```
**Get dish**
```
curl -s http://localhost:8080/voting/rest/dishes/100016?restaurantId=100002 --user user@gmail.com:userpass
```
**Get dish with a restaurant**
```
curl -s http://localhost:8080/voting/rest/dishes/with/100016?restaurantId=100002 --user user@gmail.com:userpass
```
**Get all dishes**
```
curl -s http://localhost:8080/voting/rest/dishes --user user@gmail.com:userpass
```
**Get menu**
```
curl -s http://localhost:8080/voting/rest/dishes/menu/100002 --user user@gmail.com:userpass
```
**Get menu by date**
```
curl -s http://localhost:8080/voting/rest/dishes/menu/100002?date=2020-12-21 --user user@gmail.com:userpass
```
**Update dish**
```
curl -s -X PUT -d '{"id":100016,"name":"Sandwich with tune","price":8,"date":"2020-12-21"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/dishes/100016?restaurantId=100002 --user admin@gmail.com:admin
```
**Delete dish**
```
curl -s -X DELETE http://localhost:8080/voting/rest/dishes/100016?restaurantId=100002 --user admin@gmail.com:admin
```

### User

#### Admin

**Create user**
```
curl -s -X POST -d '{"name":"New","email":"new@gmail.com","password":"password","enabled":true,"roles":["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/users --user admin@gmail.com:admin
```
**Get user**
```
curl -s http://localhost:8080/voting/rest/admin/users/100016 --user admin@gmail.com:admin
```
**Get user by Email**
```
curl -s http://localhost:8080/voting/rest/admin/users/by?email=new@gmail.com --user admin@gmail.com:admin
```
**Get all users**
```
curl -s http://localhost:8080/voting/rest/admin/users --user admin@gmail.com:admin
```
**Update user**
```
curl -s -X PUT -d '{"id":100016,"name":"NewAdmin","email":"newadmin@gmail.com","password":"password","enabled":true,"roles":["USER","ADMIN"]}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/admin/users/100016 --user admin@gmail.com:admin
```
**Delete user**
```
curl -s -X DELETE http://localhost:8080/voting/rest/admin/users/100016 --user admin@gmail.com:admin
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
curl -s -X POST -d '{"restaurantId":100002}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes --user user@gmail.com:userpass
```
**Get vote**
```
curl -s http://localhost:8080/voting/rest/votes/100016 --user user@gmail.com:userpass
```
**Get all votes**
```
curl -s http://localhost:8080/voting/rest/votes --user user@gmail.com:userpass
```
**Get all votes of user**
```
curl -s http://localhost:8080/voting/rest/votes/by?userId=100000 --user user@gmail.com:userpass
```
**Get all votes for restaurant**
```
curl -s http://localhost:8080/voting/rest/votes/by?restaurantId=100002 --user user@gmail.com:userpass
```
**Update vote**
```
curl -s -X PUT -d '{"id":100016,"restaurantId":100003}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/votes/100016 --user user@gmail.com:userpass
```
**Delete vote**
```
curl -s -X DELETE http://localhost:8080/voting/rest/votes/100016 --user user@gmail.com:userpass
```