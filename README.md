# vet-clinic-api
REST API for a vet clinic


General end point:
http://localhost:8080/api/v1/

-----------------------------------------------------------------------

Authentication endpoints:

http://localhost:8080/api/v1/register{POST}

REQUIRES - pass JSON with username,email and password

RETURNS - DTO with passed username, email and hashed password

Example request:
{
    "username" : "username",
    "email" : "email@gmail.com",
    "password": "password"
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

DESCRIPTION - Creates a new Pet entity

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

http://localhost:8080/api/v1/users/pets{PUT}

DESCRIPTION - Add an existing Pet to an Owner(User)

REQUIRES - Pass the pet id as path variable with name petId

RETURNS - DTO with id, name, specie, breed and owner(user) of the pet

Example request: http://localhost:8080/api/v1/users/addPet&petId=***





http://localhost:8080/api/v1/users/pets{GET}

DESCRIPTION - Get all of current pets of User(Owner)

REQUIRES - 

RETURNS - List with DTOs with id, name, specie, breed and owner(user) of a pet

Example request: http://localhost:8080/api/v1/users/pets




http://localhost:8080/api/v1/users/pets{DELETE}

DESCRIPTION - Deletes a Pet from Owner(User)

REQUIRES - pet id as Path Variable with name "petId"

RETURNS - DTO with id, name, specie, breed and owner(user) of the deleted pet

Example request: http://localhost:8080/api/v1/users/pets&petId=***

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
