# Analysis & Development Project - Mars 2052 - server project
This is the **server side start-project** for Project II. 

This start project provides the basic scaffolding for an openapi webserver and an example bridge class for websockets.

There is already a fully working minimal example api with all the necessary classes.

Example classes (except WebServer.java) are allowed to be modified or deleted.

## Before you start:
- Choose Zulu jdk version 11 or opendjk 11 (Configure through this through intelij)
- Make sure to clone **all** the repositories **client**, **server** & **documentation**
    - **Use the following folder structure**
        - root_folder_with_name_of_choice
            - client
            - documentation
            - server

## Local CI testing
You can **run** the Sonar validator **locally!**

There is no need to push to the server to check if you are compliant with our rules.
In the interest of sparing the server, please result to local testing as often as possible.

**If everyone pushes to test, the remote will not last.**

To run the analysis open the gradle tab (right side, with the elephant icon), open Tasks, open verification and run (double click)
the taks **test** then **jacocoTestCoverageVerification** then **jacocoTestReport** then **sonarqube**.

The results should now be available on the public sonar. (see section production endpoints)

## Configuring properties
All properties for a local setup are located in **conf/config.json**.

The remote properties are located on the remote server.

Adding new properties to the local config file is perfectly fine.

However, to apply new properties or property modifications on the server please contact mr. Blomme on MS Teams.

Provide a valid config file in json format with filename config-group-XX.

Every thursday at 13:30 the new properties will be pushed to the server.

Please, test the config file thoroughly on your local machine as mistakes will not be fixed until the next thursday at 13:30.

## What's included
  - A very basic openapi specification
    - localhost:8080/api
  - H2 database web console
  - The setup of a vert.x and openapi (WebServer.java)
  - Minimal H2 repository class
  - A starter class for the RTC topic (MarsRtcBridge.java)
  - Database maintain scripts

## How to run the start project locally
In Intelij choose gradle task **run**.
- Make sure to implement the folder structure as described in section **before you start**.
    - Otherwise, Vert.x will not find the openapi specification.

## Location OpenApi Specification
The location of the openapi specification is defined in the file **config**.

The property is called **api.url**.

By default, the local setup will pick the openapi specification located in the **documentation** repository in the folder **api-spec**.

As mentioned before, it's very important to implement the correct folder structure.

If for some reason you want to use another openapi specification, please let the property **api.url** point the correct specification.
Don't forget to also change the **config file** in the test resources. This property allows relative and absolute paths.

By default this property is assigend the value:
```json
"api": {
    "url": "../documentation/api-spec/openapi-mars.yaml"
  }
```

## Local endpoints
 - H2 web client
   - localhost:9000
   - url: ~/mars-db
   - no credentials
 - Web api
   - localhost:8080/api/
 - Web client
   - launch through webstorm/phpstorm (see client-side readme)
  
## Production endpoints
 - H2 web client
   - https://project-ii.ti.howest.be/db-16
   - url: jdbc:h2:/opt/group-16/db-16
   - username:group-16
   - password:see Leho for details.
 - Useful information
   - Server logs
     - https://project-ii.ti.howest.be/monitor/logs/group-16
   - Swagger Interface
     - https://project-ii.ti.howest.be/monitor/swagger/group-16
     - Through this GUI remote & local API testing is possible!
   - Overview of all Mars API's
     - https://project-ii.ti.howest.be/monitor/overview/
     - Please complete the openapi.yaml file to contribute useful information to the overview page.
 - Web client project
   - https://project-ii.ti.howest.be/mars-16
 - Sonar
   - https://sonar.ti.howest.be/dashboard?id=2021.project-ii%3Amars-server-16
   - https://sonar.ti.howest.be/dashboard?id=2021.project-ii%3Amars-client-16

## Keep the database up to date
There is no need to manually add entries into the database.

Please use the scripts: **db-create** and **db-populate** in the resource folder.

Everytime you deploy a new version to production the database will be refreshed to the state described in the before mentioned scripts.

The **db-create** script is responsible for create the database structure (tables, primary keys, ...)

The **db-populate** script is responsible for populating the database with useful data.

## Adding a new openapi call
   1. Update the openapi specification in the documentation repo
   2. Update the function **buildRouter** in the class **MarsOpenApiBridge**
      1. Map the operationId (openapi id) to a new function in the class **MarsOpenApiBridge**
      1. Create this new function in the **MarsOpenApiBridge**
      1. At this point you might want to leave this function empty.
   3. Add the wanted functionality to the controller layer and the layers below.
   4. Add a new response function in the **Response** class if needed.
   5. Complete the function from step 2.ii
   6. Write unit tests