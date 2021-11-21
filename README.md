# Assignment - Agile Software Development.

# Health Tracker API

Name: Mozeeb Abdulha

Student ID: 20075835

## Overview.

This project is a RESTful Web App which allows users to keep track of their health by viewing exercises, tracking their meal nutrition intake and checking bmi.

## API endpoints.

-- Users --

+ GET /api/users - Get all Users.
+ GET /api/users/:user-id - Get user by their ID. 
+ GET /api/users/:email - Get user by their email.
+ GET /api/users/count - Get the count of users in the app.
+ POST /api/users/register - Register a new user.
+ POST /api/users/login - Login a user to the app.
+ PATCH /api/users/:user-id - Update the user based on the ID passed.
+ DELETE /api/users/:user-id - Delete the user based on the ID passed.

-- Exercises --
+ GET /api/exercises - Get all Exercises.
+ GET /api/exercises/:exercise-id - Get exercise by their ID.
+ GET /api/exercises/count - Get the total amount of exercises in the app.
+ GET /api/users/:user-id/exercises - Get all the exercises associated with a user by the user ID.
+ POST /api/exercises - Add a new exercise to the app.
+ PATCH /api/exercises/:exercise-id - Update an exercise by passing in the exercise ID.
+ PUT /api/exercises/:exercise-id/increment-view - Update the exercise to increment a view onto the exercise's view field (Add +1 onto it, for statistics).
+ DELETE /api/exercises/:exercise-id - Delete the pet based on the ID passed.
+ DELETE /api/users/:user-id/exercises - Delete users exercises by passing in the user ID.

-- Meals --
+ GET /api/meals - Get all meals in app
+ GET /api/meals/count - Get count of all meals in app
+ GET /api/meals/:meal-id - Get meal by the ID being passed in
+ GET /api/meals/users/:user-id - Get the meals associated with user
+ GET /api/meals/:meal-id/ingredients - Get all the meals ingredients
+ GET /api/meals/:meal-id/ingredients/count/ - Get a count of all the meals ingredients
+ PATCH /api/meals/:meal-id - Update a meal by passing in the meal ID
+ POST /api/meals - Add a new meal in the app
+ POST /api/meals/:meal-id/ingredients/:ingredient-id/assign - Assign the ingredients to a meal
+ PUT /api/meals/:meal-id/increment-loves - Increment the meal loves which will be used for sorting
+ DELETE /api/meals/:meal-id - Delete a meal by passing in the meal ID

-- Ingredients --
+ GET /api/ingredients - Get all ingredients in the app
+ GET /api/ingredients/:ingredient-id - Get an ingredient by passing in the ingredient ID
+ POST /api/ingredients - Add a new ingredient to the app
+ PATCH /api/ingredients/:ingredient-id - Update an ingredient by passing in the ingredient ID
+ DELETE /api/ingredients/:ingredient-id - Delete an ingredient by passing in the ingredient ID

-- UserBmi --
+ GET /api/bmis/users/:user-id/scores - Get all the BMI scores for a user by passing in th user ID, will be used to graph bmi scores
+ POST /api/bmis - Add a new BMI score

## Data model.

-- User Schema --

~~~

    {
        id: integer
        avatar:	string
        fname: string
        lname: string
        email: string
        password: string
        weight:	number
        height:	number
        gender:	string
        age: integer
    }

~~~

-- Sample output --

 ~~~
    [
      {
        "id": 526,
        "avatar": "https://pbs.twimg.com/media/E9sN5jzVUAUgYHn?format=png&name=small",
        "fname": "Joe",
        "lname": "Bloggs",
        "email": "joe@bloggs.com",
        "password": "$2a$10$kbjkBu62VWWafZMH2yp.kuhZh9kYiOW/KJ5iFAsd3vlsMB9CTL3SK",
        "weight": 84.5,
        "height": 183,
        "gender": "M",
        "age": 26
      }
    ]
 ~~~

-- Exercise Schema --

~~~

    {
        id:	integer
        image: string
        name: string
        description: string
        calories: integer
        duration: number
        muscle:	string
        views: integer
        userId:	integer
    }

~~~

-- Sample output --

 ~~~
    [
      {
        "id": 54,
        "image": "https://cdn1.dotesports.com/wp-content/uploads/2018/08/11151637/128fb6e6-7b6a-4b25-8b16-6e562f9c288d.jpg",
        "name": "Calf Raises",
        "description": "Calf raises are a method of exercising the gastrocnemius, tibialis posterior, peroneals and soleus muscles of the lower leg.",
        "calories": 65,
        "duration": 25,
        "muscle": "Calf",
        "views": 5,
        "userId": 526
      }
    ]
 ~~~

-- Meal Schema --

~~~

    {
        id: integer
        image: string
        name: string
        energy: integer
        calories: integer
        protein: number
        fat: number
        carbs: number
        sodium: number
        loves: integer
        userId: integer
    }

~~~

-- Sample output --

 ~~~
    [
          {
            "id": 54,
            "image": "https://cdn1.dotesports.com/wp-content/uploads/2018/08/11151637/128fb6e6-7b6a-4b25-8b16-6e562f9c288d.jpg",
            "name": "Beef Stew",
            "energy": 15,
            "calories": 14,
            "protein": 1.05,
            "fat": 0.2,
            "carbs": 0.02,
            "sodium": 6.26,
            "loves": 45,
            "userId": 526
          }
    ]
 ~~~

-- Ingredient Schema --

~~~

    {
        id: integer
        image: string
        name: string
        energy: integer
        calories: integer
        protein: number
        fat: number
        carbs: number
        sodium: number         
    }

~~~

-- Sample output --

 ~~~
    [
          {
            "id": 54,
            "image": "https://cdn1.dotesports.com/wp-content/uploads/2018/08/11151637/128fb6e6-7b6a-4b25-8b16-6e562f9c288d.jpg",
            "name": "Beef",
            "energy": 15,
            "calories": 14,
            "protein": 1.05,
            "fat": 0.2,
            "carbs": 0.02,
            "sodium": 6.26
          }
    ]
 ~~~

-- UserBmi Schema --

~~~

    {
        id: integer
        bmi: number
        userId: integer
    }

~~~

-- Sample output --

 ~~~
    [
          {
            "id": 54,
            "bmi": 20.5,
            "userId": 526
          }
    ]
 ~~~
