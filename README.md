# Poképlan
**Data retrieval from Firebase**

The data is only retrieved from Firebase once (i.e. at the start). When changes are made in the database, the local data (stored in an array list) is also updated so that there is no need to retrieve the data from Firebase everytime, which would lessen the chances of data being shown in the app without the app having the updated version (since Firebase works asynchronously).
