# KtorEasy
This is a project to demonstrate a suggested Ktor architecture. It includes Backend implementation with Ktor, MariaDB database connection with Hikari, Docker command to build Backend container and Docker compose to run both database and backend instance.

## Architecture explained
![arch_image](https://github.com/mathias21/KtorEasy/blob/master/ktorArch.png?raw=true)<br>
This is a suggested architecture that pretends to follow CLEAN principles. The main idea behind it is to define each layer with one purpose and to allow unit testing each layer independently.

To achieve this, I'm using Koin as a dependency injection framework. 
https://insert-koin.io/

Also, to make each layer testable, Controllers and APIs should always implement an interface that will be injected where they are used, instead of the implemented type.

### Application.kt
File to define Java app main method, Koin setup and configuration initialisation.

### Module.kt
Application extension function where we configure Ktor application with features, interceptors and routing.

### Modules
Each module will define routes related with its functionality. It will be a Route function extension to configure it and it will receive an injected controller object. This layer is responsible of reacting to API call, receive parameters and invoke controllers function with them.

### Controllers
There will be one controller per module and it will receive by injection those API objects it will use to handle module requests. Here all the business logic should be defined. Controller will fetch/write data through API objects and unhappy paths will be handled throwing exceptions and answered by status pages.

### APIs
This layer will use DAOs to recover/write information from/in database. It will be responsible of transforming data if needed.

### DAOs
DAOs objects will define tables and those operations to do with them. It will also map table rows into API expected objects. 

## How to run it
This project it is prepared to be run in Docker via [docker-compose](https://docs.docker.com/compose/). With a simple commands you will have it deployed to Docker with a MariaDB container linked to the backend instance. Just run: `docker-compose up`.

MariaDB is accesible through port `3308` with root password `mypassword` by default. The backend is accesible through port `3510`. You can change this settings in the `docker-compose.yml` file.