## Transaction service 
Processes payment transactions

### Build & Test

```
git clone URL
cd transactions
./gradlew build
```

### Run
To run the application download the jar file from the repository 
or build youself (see above on how)

#### Prerequisites
Transaction service requires running Cassandra server. 

#### Configuring Transaction Service
If you already have a Cassandra server setup somewhere 
you can point Transactions Service to is by providing a 
suitable `application.yml` during startup. 

#### Example application YAML
To override defaults you can create `application.yml` file with the 
following content 
```
spring:
  data:
    cassandra:
      keyspace-name: transactions
      contact-points: 127.0.0.1
      datacenter-name: datacenter1
```
and drop it to the same folder as the application jar file. 

#### Example Cassandra server setup with Docker
Assuming you have Docker installed you can setup one like this

`docker run --name transactions-cassandra --volume [absolute path to repository on local FS]/code -w /code -p 127.0.0.1:9042:9042 -p 127.0.0.1:9160:9160 -d cassandra`

for example

`docker run --rm --ti --name transactions-cassandra --volume /Users/admin/dev/transactions:/code -w /code -p 127.0.0.1:9042:9042 -p 127.0.0.1:9160:9160 -d cassandra`

You can now login into the container and create the keyspace and the tables

`docker exec -it transactions-cassandra bash`

then run

`cqlsh -f 001-initial-create.cql && exit`
 
The transaction service is configured to connect to Cassandra database 
running on the localhost and use keyspace `transactions`

Once the above is done you can start the application
`java -jar build/libs/transactions-service-0.0.1.jar`

The application is configured to accept connections on `http://localhost:8080`
Navigate to `http://localhost:8080/swagger-ui.html` for endpoints
and datamodel documentation.
