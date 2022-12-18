# CPS Project
Car Parking System - Software Engineering Final Project.

## Structure
Pay attention to the three modules:
1. **client** - a simple client built using JavaFX and OCSF. We use EventBus (which implements the mediator pattern) in order to pass events between classes (in this case: between SimpleClient and PrimaryController).
2. **server** - a simple server built using OCSF.
3. **entities** - a shared module where all the entities of the project live.

## Running
Project created and maintained using 'Maven'.
1. Build the project using `clean install` via 'Maven' in the project **root** directory.
2. Run the server using the `exec:java` 'Maven' goal in the **server** module.
3. Run the client using the `javafx:run` 'Maven' goal in the **client** module.
4. The application is up and running!