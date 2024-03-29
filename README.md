# vet-clinic-api
REST API for a vet clinic


General end point:
http://localhost:8080/api/v1/
-----------------------------------------------------------------------
Authentication endpoints:

http://localhost:8080/api/v1/register{POST}

REQUIRES - pass JSON with username,email, password, first name, last name, phone number(optional)

RETURNS - DTO with passed username and email

Example request:
{
    "username" : "username",
    "email" : "email@gmail.com",
    "password": "password",
    "fName" : "FirstName",
    "lName": "LastName",
    "phoneNumber" : "01234546789"
}




http://localhost:8080/api/v1/login{POST}

REQUIRES - pass JSON with username, password

RETURNS - DTO with variable "token"(JWT is there)

Example request:
{
    "username" : "username",
    "password": "password"
}


------------------------------------------------------------------------
(NEED TO PASS JWT)
Pet endpoints:

http://localhost:8080/api/v1/pets{POST}

DESCRIPTION - Creates a new Pet entity and binds it to current User

REQUIRES - pass JSON with name(String)(min-3, max-15), specie(String)(min-3, max-10) 
and breed(String)(min-3, max-15) of the Pet

RETURNS - DTO with id, name, specie, breed and owner(user) of the pet

Example reuqest:
{
    "name" : "PetName",
    "specie" : "Specie",
    "breed": "Breed"
}


--------------------------------------------------------------------------
(NEED TO PASS JWT)
User endpoints:

http://localhost:8080/api/v1/users{GET}

DESCRIPTION - Returns information about the currently logged user

REQUIRES -

RETURNS - DTO with information about the logged user

Example request: http://localhost:8080/api/v1/users



http://localhost:8080/api/v1/users/pets{GET}

DESCRIPTION - Get all of current pets of User(Owner)

REQUIRES - 

RETURNS - List with DTOs with id, name, specie, breed and owner(user) of a pet

Example request: http://localhost:8080/api/v1/users/pets




http://localhost:8080/api/v1/users/pets{PATCH}

DESCRIPTION - Updates Pet's property with a given one

REQUIRES - Parameter petId and name of the changed property(breed, specie, name)

RETURNS - Updated Pets's info

Example request://localhost:8080/api/v1/users/pets?petId=***

{
	"specie" : "ChangedSpecie"
}





http://localhost:8080/api/v1/users/pets{DELETE}

DESCRIPTION - Deletes a Pet from Owner(User)

REQUIRES - pet id as Path Variable with name "petId"

RETURNS - DTO with id, name, specie, breed and owner(user) of the deleted pet

Example request: http://localhost:8080/api/v1/users/pet?&petId=***





http://localhost:8080/api/v1/users/visits{GET}

DESCRIPTION - Get all Visits for logged User

REQUIRES - 

RETURNS - List with DTOs with id, name, specie, breed and owner(user) of a pet

Example request: http://localhost:8080/api/v1/users/visits




http://localhost:8080/api/v1/users{PATCH}

DESCRIPTION - Updates current User's property with a given one

REQUIRES - Parameter userId and name of the changed property(fName, lName, phoneNumber)

RETURNS - Updated User's info

Example request://localhost:8080/api/v1/users?userId=***

{
	"fName" : "ChangedName"
}




--------------------------------------------------------------------------
(NEED TO PASS JWT)
Visits endpoints:

http://localhost:8080/api/v1/visits{POST}

DESCRIPTION - Creates a Visit with User(Owner), Pet of the User(Owner) and Vet

REQUIRES - Pass date, time, description, Pet and Vet

RETURNS - DTO with id, date, time, description, Pet, Vet and User

Example request:

{
    "date": "2024-10-05",
    "time": "12:08:17",
    "description" : "Random description",
    "pet" : {
        "id" : "57cf7587-2e68-4713-8eb0-750aa501941e"
    },
    "vet" : {
        "id" : "266cd58b-263c-4bed-823a-f1065e3c3b4a"
    }
}



--------------------------------------------------------------------------
(NEED TO PASS JWT AND ADMIN AUTHORITY)
Admin endpoints:

http://localhost:8080/api/v1/admin/users{GET}

DESCRIPTION - Fetches all the users from database

REQUIRES - 

RETURNS - List of UserInfoDTOs

Example request://localhost:8080/api/v1/admin/users



http://localhost:8080/api/v1/admin/users{DELETE}

DESCRIPTION - Deletes a User from database

REQUIRES - Parameter userId

RETURNS - Deleted user's info

Example request://localhost:8080/api/v1/admin/users?userId=***



http://localhost:8080/api/v1/users{PATCH}

DESCRIPTION - Updates current User's property with a given one

REQUIRES - Parameter userId and name of the changed property(fName, lName, phoneNumber)

RETURNS - Updated User's info

Example request://localhost:8080/api/v1/users?userId=***

{
	"fName" : "ChangedName"
}




http://localhost:8080/api/v1/users/pets{PATCH}

DESCRIPTION - Updates Pet's property with a given one

REQUIRES - Parameter petId and name of the changed property(breed, specie, name)

RETURNS - Updated Pets's info

Example request://localhost:8080/api/v1/users/pets?petId=***

{
	"specie" : "ChangedSpecie"
}



http://localhost:8080/api/v1/admin/users/{userId}
http://localhost:8080/api/v1/admin/users/15616-156-156165-1156 
DESCRIPTION - Get single user profile details, using UserID



http://localhost:8080/api/v1/admin/users/{userId}/pets{GET}
DESCRIPTION - Get pets from profile, using UserID



--------------------------------------------------------------------------
(NEED TO PASS JWT AND ADMIN AUTHORITY)
MEDICATIONS ENDPOINTS:

http://localhost:8080/api/v1/meds{POST}

REQUIRES - 
{
        "name": "Asd",
        "type": "asd",
        "quantity": 23,
        "description": "asd"
}


http://localhost:8080/api/v1/meds?medId=***{DELETE}

REQUIRES - Parameter petId




http://localhost:8080/api/v1/meds?medId=***{PATCH}

REQUIRES - (DONT PASS QUANTITY) DTO with changed property/ies 
{
        "name": "Asd",
        "type": "asd",
        "description": "asd"
} or
REQUIRES - 
{
        "name": "Asd"
}


http://localhost:8080/api/v1/meds/{medId}{PATCH}
ONLY TO UPDATE QUANTITY

REQUIRES - DTO with quantity to subtract
{
        "quantity": 23
}




http://localhost:8080/api/v1/meds{GET}

DESCRIPTION - Retrieves all medications

