# Getting Started

### Reference Documentation

## Get User Details

<strong>Endpoint: http://localhost:8080/api/userdetails/{id}
<br>Action: GET
<br>
Field ID must be a integer and must be in the database to be shown.
<br><br>
Exceptions: 
<br>
User not found, id is not present in the database
<br>
Field mismatch, the id must be a integer, any other informed it will display an error.
<br><br>
Success:<br>
If the request is a success it will display the information of the user and the addres as per the following example.
<br>
```json
{
"id": 1,
"title": "Mr",
"firstName": "Marco",
"lastName": "Araujo",
"gender": "Male",
  "address": {
    "id": 1,
    "street": "Street 1",
    "city": "Sydney",
    "state": "NSW",
    "postCode": "2121"
  }
}
```


## Update User Details
<strong>Endpoint: http://localhost:8080/api/userdetails/{id}
<br>Action: POST
<br>
Field ID must be a integer and must be in the database to be shown.
<br><br>
Exceptions:
<br>
User not found, id is not present in the database
<br>
Field mismatch, the id must be a integer, any other informed it will display an error.
<br>
All the fields in the request body are mandatory, and we can update any of them in anytime.<br>

### Request body Example
```json
{
    "title": "Mr",
    "firstName": "User 10",
    "lastName": "10",
    "gender": "Male",
    "address": {
        "street": "Street 10",
        "city": "Sydney",
        "state": "NSW",
        "postCode": "1234"
    }
}
```
<br>If any of the fields are missing it will be an error, requesting the mandatory field.



## Error Examples

### Field informed not a Integer {id}
```json
{
    "status": "BAD_REQUEST",
    "timestamp": "29-10-2022 11:19:12",
    "message": "The field informed asdasd informed is not compatible with the required field. The type required is Integer",
    "debugMessage": "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \"asdasd\""
}
```

### Not found in Database

```json
{
    "status": "NOT_FOUND",
    "timestamp": "29-10-2022 11:19:53",
    "message": "User has not been found with id 1123"
}
```

### Mandatory field missing
```json
{
    "status": "BAD_REQUEST",
    "timestamp": "29-10-2022 11:20:15",
    "message": "The Gender must be informed",
    "debugMessage": "Field: gender"
}
```


# How to run the application.

Open the root project and run the maven command
<br>
```mvn spring-boot:run```

After the loading, the service will be accessible via the endpoints.