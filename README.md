# Poképlan
**Data retrieval from Firebase**

The data is only retrieved from Firebase once (i.e. at the start). When changes are made in the database, the local data (stored in an array list) is also updated so that there is no need to retrieve the data from Firebase everytime, which would lessen the chances of data being shown in the app without the app having the updated version (since Firebase works asynchronously).
 
# app flow
**register/login**
- initial-screen -> register-a -> register-b -> home
- initial-screen -> login -> home

**activities w / navbar**
- home
- start-timer
- task-list
- dex
- options

**focus timer**
- start-timer -> ongoing-timer -> done-timer-a -> done-timer-b -> start-timer
- start-timer -> ongoing-timer -> stopped-timer -> start-timer

**task list**
- task-list -> view-task -> finish-task
- task-list -> create-task -> view-task
