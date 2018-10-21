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
Use the following command to run test:
    
    # mvn test

# How to build
To build the project use the following command:
    
    # mvn package
After that, a file named **tree-0.0.1.war** is created under the target folder, for running this file you should have a database, start a database by using following command:
    
    # docker 
After starting the docker you can run the application:
    
    # java -jar target/tree-0.0.1.war
You can use this as a executable and deployable war file.   
For 
