# PantryPal

This project contains PantryPal app and server by Team 20 CSE110 Fall 2023.

# How to Use

## PantryPal Server

1. Download the repository in full. Ensure you have Java JDK, JavaFX, and Gradle installed.
2. Open a terminal and navigate to the root folder "CSE-110-PROJECT-TEAM-20". We recommend Powershell or Git Bash.
3. If you want to use non-mock data, rename `server.dist.properties` to `server.properties` and add a OpenAI API key, and server setting. Also add database connection string as described in Server Configuration section.
4. Run these commands in order, letting each one fully complete before the next:

```
./gradlew runServer
```

**NOTE:** If the server need to be accessed from outside of localhost, please make sure to update the config as well. If the server indicates that `localhost` is in the config, it will prompt possible IP addresses for the configuration.

# How to Use

## PantryPal Client

1. Download the repository in full. Ensure you have Java JDK, JavaFX, and Gradle installed.
2. Open a terminal and navigate to the root folder "CSE-110-PROJECT-TEAM-20". We recommend Powershell or Git Bash.
3. If you want to use non-mock data, rename `app.dist.properties` to `app.properties` and add server IP address.
4. Run these commands in order, letting each one fully complete before the next:

```
./gradlew runApp
```

# Server Configuration

## MongoDB setup

### MongoDB Atlas Cluster setup

Update `server.properties` with a MongoDB connection string from [MongoDB Atlas](https://cloud.mongodb.com/v2). Check [the lab 6](https://docs.google.com/document/d/1mzgUjWQSn3IV68H4V07r1A1fGs21yY9rm188HWWr2Hs/edit#heading=h.z1qnwebb5puw) to get connection string.

```
mongodb.connection_string=mongodb+srv://<username>:<password>@cluster0.ttsc32s.mongodb.net/?retryWrites=true&w=majority
```

The URL of the connection string could be different. Check your dashboard.

### Local setup

If you want to run the mongodb from the local environment, run MongoDB on local machine using docker container.

```bash
docker run -d --name pantrypal-mongo -p 27017:27017  \
      -e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
      -e MONGO_INITDB_ROOT_PASSWORD=secret \
      mongo
```

Update `server.properties` with a MongoDB connection string.

```
mongodb.connection_string=mongodb://mongoadmin:secret@localhost:27017/
```

You can use [MongoDB Compass](https://www.mongodb.com/products/tools/compass) to access/manage local mongoDB.
