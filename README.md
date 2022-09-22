# crud_recipe
With nightmare Modes -- use this one

And a new CRUD prompt:
CRUD	| Verb	| Path | Name | Purpose
Create	| POST	| /recipe	 | “create” route | Creates a recipe entry
Read	| GET	| /recipe/{id} | “show” route	 | Responds with a single recipe
Update	| PATCH	| /recipe/{id} | “update” route | Updates attributes of the recipe
Delete	| DELETE| /recipe/{id} | “delete” route | Deletes the recipe at the id
List	 | GET	| /recipe	 | “list” route | Responds with a list of recipes

In Your Database:
Recipe Entity:

Data Type	Attribute
Long id
String description
String instructions
String title
int calories
LocalDate dateCreated

Bonus: Add a filter based on optional Minimum and/or Maximum calories
Bonus 2: Add a sort parameter to allow for sorting by calories (both ASC and DESC)
2:38
Nightmare mode: Add an ingredients field to your recipe entity that is a list of ingredients. Ingredient should be its own entity with a name field and a boolean field (true if it contains gluten, false if it does not contain gluten). This challenge will require you to research many-to-one / one-to-many relationships in spring boot.
2:39
Nightmare mode part 2: switch your DB to a non relational database of your choice, like MongoDB.
