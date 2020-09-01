# StoreFront
Storefront is a mobile app that allows shoppers to easily find the products they are looking for in stores, and store owners the ability to easily add their product selection to the store layout for customers to search, thus saving them time and money.

This project was implemented using the following **Firebase** services: <br>
* **Authentication** for owner login
* **Cloud Functions** as POST endpoints for updating the store attributes in Firestore db (layout update or new product added) 
* **Storage** for storing the images of the products the owner adds - the image URL is then stored in the Firestore db as an attribute of the product associated with it
* **Firestore** for storing the store information per owner (this includes store layout and all product information ie. location, quantity, image URL, name, etc)


Please watch the demo below which also provides an in-depth explanation for how this was implemented:

[![StoreFront Demo](http://img.youtube.com/vi/T1N5sZI_OZ4/0.jpg)](https://www.youtube.com/watch?v=T1N5sZI_OZ4)
<br>
https://www.youtube.com/watch?v=T1N5sZI_OZ4
