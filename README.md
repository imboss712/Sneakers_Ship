# Sneaker Ship

## About

Sneaker Ship is an Android application that serves as a Sneaker Shopping Cart. It is built using the
MVVM architecture pattern and implemented in Kotlin.

## Features

#### Home Page:

* Displays a grid of available sneakers.
* Each item contains the image, price, and name of the sneaker.

#### Sneaker Details Page:

* Contains the selected sneaker's name, image, size, color, and price.
* The "Add to Cart" button adds the sneaker to a cart page.

### Cart Page:

* Contains all the sneakers added in the cart.
* Displays their image, price, and quantity.
* Items can be added and removed from the cart.
* Also Provides information about the total items and total price.

## Permissions

* Internet

## Libraries

* Glide - For image loading
* Room DB - For storing items in Cart
* Coroutines - For reading sneakers from json file and with Room DB
* Gson - For serialization and deserialization of Java objects to and from JSON
* Lifecycle Components - To use LiveData and ViewModels
* Navigation Components - To navigate between screens

***

# Development Process

At the beginning, I went through the JSON structure of a Sneaker object. Based on my observations,
I assumed that the price of a sneaker remains constant across all genders, sizes, and colors.

Initially, I created two data classes, namely Sneaker and CartItem.
Here, CartItem has information about a Sneaker and the quantity the user intends to add to the cart.

I was thinking to create an additional class for ShoppingCart to hold all cart items,
But as I decided to use ViewModel, to store the cart items within the ViewModel itself.

As there are two data sources: one for displaying sneakers on the home screen and another for
showing cart items.'
To implement abstraction, I created two repositories - SneakerRepository and CartRepository.

SneakerRepository is responsible for returning a list of sneakers sourced from the local JSON file.
And CartRepository gets cart items stored in the RoomDB.
Here, I have also defined the functions for adding, removing, and updating the items in the cart.

To address configuration changes and manage business logic,
I created two ViewModels - SneakersViewModel and CartViewModel.

For navigation, I implemented two bottom tabs: one for displaying available sneakers and another for
showing items added to the cart.

I have used Flow and LiveData to receive updates whenever there are changes.

Additionally, I integrated RoomDB to persistently store cart items in the database.
This ensures that if a user relaunches the app, their cart items will be retrieved from the
database.

## Screenshots

<img src="https://github.com/imboss712/Sneakers_Ship/blob/master/screenshots/home.png" width="250" height="500">

<img src="https://github.com/imboss712/Sneakers_Ship/blob/master/screenshots/home_search.png" width="250" height="500">

<img src="https://github.com/imboss712/Sneakers_Ship/blob/master/screenshots/sneaker_detail.png" width="250" height="500">

<img src="https://github.com/imboss712/Sneakers_Ship/blob/master/screenshots/sneaker_cart.png" width="250" height="500">

<img src="https://github.com/imboss712/Sneakers_Ship/blob/master/screenshots/home_dark.png" width="250" height="500">

<img src="https://github.com/imboss712/Sneakers_Ship/blob/master/screenshots/cart_dark.png" width="250" height="500">