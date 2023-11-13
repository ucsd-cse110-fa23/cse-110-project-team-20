# PantryPal

This document captures current status of PantryPal for further development.

## Client

![Client implementation diagram](./client.png)

## Server

![Server implementation diagram](./server.png)


## REST API Specification

When we have a resource on the sever, returns resource object.

### `/recipe`

#### GET

GET request returns recipes/recipe that we want to display.

##### without querystring

URL: `/recipe`
Expected response: a list of recipes

```json
{
  "recipes": [
    {
      "title": "Tomato, Cucumber, and Egg Salad",
      "description": "1. Boil the eggs until they are hard-boiled. Let them cool and then peel and chop them.\n2. Wash and dice the tomato and cucumber.\n3. In a large bowl, combine the chopped eggs, diced tomato, and cucumber.\n4. Toss the ingredients together.\n5. Season with salt and pepper to taste.\n6. Serve as a refreshing and healthy dinner salad."
    }
  ]
}
```

##### with querystring

URL: `/recipe?id=1000`
Expected behavior: find the recipe using given index of the list.
Expected response: a single recipe, or empty if does not exist

```json
{
  "title": "Tomato, Cucumber, and Egg Salad",
  "description": "1. Boil the eggs until they are hard-boiled. Let them cool and then peel and chop them.\n2. Wash and dice the tomato and cucumber.\n3. In a large bowl, combine the chopped eggs, diced tomato, and cucumber.\n4. Toss the ingredients together.\n5. Season with salt and pepper to taste.\n6. Serve as a refreshing and healthy dinner salad."
}
```

#### POST

URL: `/recipe`
Request body: `title`, `description`
Expected response: no response

```json
{
  "title": "Tomato, Cucumber, and Egg Salad",
  "description": "..."
}
```

#### PUT

URL: `/recipe?id=1000`
Request body: `title`, `description`
Expected response: no response

```json
{
  "title": "Tomato, Cucumber, and Egg Salad",
  "description": "..."
}
```

#### DELETE

URL: `/recipe?id=1000`
Expected response: no response

### Generating Recipe: `/recipe/generate`

#### POST

URL: `/recipe/generate`
Request body: two audio files 'ingredients_file' and 'meal_type_file'
Expected response: generated recipe by ChatGPT

Note: we don't have an ID yet. User will decide to save it. When click the save button, this information need to be submitted to `/recipe` via POST request method.

```json
{
  "title": "Tomato, Cucumber, and Egg Salad",
  "description": "1. Boil the eggs until they are hard-boiled. Let them cool and then peel and chop them.\n2. Wash and dice the tomato and cucumber.\n3. In a large bowl, combine the chopped eggs, diced tomato, and cucumber.\n4. Toss the ingredients together.\n5. Season with salt and pepper to taste.\n6. Serve as a refreshing and healthy dinner salad."
}
```

## Appendix

### Generating Recipe

![](./generate-recipe-api-steps.png)