# KtorEasy
This is a project to demonstrate a suggested Ktor architecture. It includes Backend implementation with Ktor, MariaDB database connection with Hikari, Docker command to build Backend container and Docker compose to run both database and backend instance.

## Architecture explained
This is a suggested architecture that pretends to follow CLEAN principles. The main idea behind it is to define each layer with one purpose and to allow unit testing each layer independently.

To achieve this, I'm using Koin as a dependency injection framework. 
https://insert-koin.io/


## How to run it
This project it is prepared to be run in Docker. With just two simple commands you will have it deployed to Docker with MariaDB container linked to the backend instance. Following these steps will do the magic:

1. Run **buildContainer.sh** file to create Backend Docker container image.
2. Once this is done, run **docker-compose.yml** file.
