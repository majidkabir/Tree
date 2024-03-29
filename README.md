# Company Tree
Tradeshift Challenge
# Challenge Description
We in Amazing Co need to model how our company is structured so we can do awesome stuff.

We have a root node (only one) and several children nodes, each one with its own children as well. It's a tree-based structure.

Something like:

         root
        /    \
       a      b
       | 
       c

We need two HTTP APIs that will serve the two basic operations:

1) Get all children nodes of a given node (the given node can be anyone in the tree structure).
2) Change the parent node of a given node (the given node can be anyone in the tree structure).

They need to answer quickly, even with tons of nodes. Also, we can't afford to lose this information, so some sort of persistence is required.

Each node should have the following info:
1) node identification
2) who is the parent node
3) who is the root node
4) the height of the node. In the above example, height(root) = 0 and height(a) == 1.

Our boss is evil and we can only have docker and docker-compose on our machines. So your server needs to be ran using them.

# How to test
Use the following command to run tests:
    
    # mvn test

# How to build
To build the project use the following command:
    
    # mvn package
After that, a file named **tree-0.0.1.war** is created under the target folder
# How to tun
For running this program you should have a **PostgreSQL** database, start a database by using following command:
    
    # docker run -e POSTGRES_USER=test -e POSTGRES_PASSWORD=test123 -e POSTGRES_DB=tree -p 5432:5432 library/postgres
After starting the docker you can run the application.
## Standalone
You can run it with this command:
    
    # java -jar target/tree-0.0.1.war
## Web container
You can use the war file as a executable and deployable war file.
Copy the war file to an application server web container and start it.

# Deploy on a server with docker
You should change the database address from localhost to app-db in *application.properties* file, before building the project.    
After building the project, copy the created war file to *./docker/web-docker/*
    
    # cp target/tree.0.0.1-SNAPSHOT.war docker/web-docker/
Copy docker folder to your server and run docker-compose command

    # docker-compose up

This command start 3 docker container, one for PostgreSQL database, one for tomcat web server and one for storing the PostgreSQL data.

# Production ready docker
You can get the app docker image with running the following command:
    
    # docker pull majidkabir/tree
Before running this image you shoud start the postgres docker.

# Extra Information
Extra information can be found [here](https://github.com/majidkabir/Tree/wiki)