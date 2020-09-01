const functions = require("firebase-functions");
const admin = require("firebase-admin");
const serviceAccount = require("./credentials.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://store-front-backend.firebaseio.com",
  storageBucket: "store-front-backend.appspot.com/"
});
const db = admin.firestore();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
exports.helloWorld = functions.https.onCall((request, response) => {
  return {
    message: "Hello From Firebase"
  };
});

exports.getProducts = functions.https.onCall(async (data, context) => {
  try {
    let all_products = await db.collection("Products").get();
    const products = all_products.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));
    console.log({ products });
    return { products };
  } catch (err) {
    throw new functions.https.HttpsError(
      "not-found",
      "Could not fetch products",
      {
        testing: "this is a test return value"
      }
    );
  }
});

exports.addProduct = functions.https.onCall((data, context) => {
  let product = data;
  let productID = new Date().getTime().toString();
  //saving to Firebase Firestore DB product info
  return db
    .collection("Products")
    .doc(productID)
    .set({
      id: productID,
      name: product.name,
      quantity: product.quantity,
      location: product.location,
      pic:
        "https://storage.googleapis.com/store-front-backend.appspot.com/Products/" +
        productID +
        ".jpg"
    })
    .then(ref => {
      //now converting base64 image to image file fit for storing in the Firebase Storage db
      //link to image will be as written above

      var type = "image/jpeg";
      var fileName = productID + ".jpg";
      var imageBuffer = Buffer.from(product.pic, "base64");

      // Upload the image to the bucket
      return admin
        .storage()
        .bucket()
        .file("Products/" + fileName)
        .save(imageBuffer, {
          metadata: { contentType: type }
        })
        .then(result => {
          return "SUCCESS";
        });
    })
    .catch(function(error) {
      throw new functions.https.HttpsError(
        "not-found",
        "Could not add product",
        {
          testing: "this is a test return value"
        }
      );
    });
});

exports.removeProduct = functions.https.onCall((data, context) => {
  let productID = data.productID;
  let deleteProd = db
    .collection("Products")
    .doc(productID)
    .delete();

  return deleteProd
    .then(res => {
      return admin
        .storage()
        .bucket()
        .file("Products/" + productID + ".jpg")
        .delete()
        .then(function() {
          return "SUCCESS";
        })
        .catch(function(error) {
          throw new functions.https.HttpsError(
            "not-found",
            "Could not delete product picture from storage",
            {
              testing: "this is a test return value"
            }
          );
        });
    })
    .catch(function(error) {
      throw new functions.https.HttpsError(
        "not-found",
        "Could not delete product from DB",
        {
          testing: "this is a test return value"
        }
      );
    });
});

exports.updateStore = functions.https.onCall((data, context) => {
  let storeStr = data.storeStr;
  console.log(storeStr);
  let storeJsonData = JSON.parse(storeStr);
  console.log(storeJsonData);
});

/*exports.addFloor = functions.https.onCall((data, context) => {
  const floor = data;
  const id = db.collection("Floors").doc().id;
  const res = db
    .collection("Floors")
    .doc(id)
    .set(floor);
  return { id };
}); */

exports.upsertStore = functions.https.onCall(async (data, context) => {
  //Need to parse through all the floors fields
  //For each floor object need to parse it save it to the db and then grab the reference
  let store_data = data;
  let all_stores = await db.collection("Stores").get();
  const stores = all_stores.docs.map(doc => ({
    id: doc.id,
    ...doc.data()
  }));

  let id;
  if (stores[0]) {
    id = stores[0].id;
  } else {
    id = db.collection("Stores").doc().id;
  }
  const res = db
    .collection("Stores")
    .doc(id)
    .set(store_data);
  return res;
});

exports.getStore = functions.https.onCall(async (data, context) => {
  let store_data = data;
  let all_stores = await db.collection("Stores").get();
  const stores = all_stores.docs.map(doc => ({
    id: doc.id,
    ...doc.data()
  }));
  return { stores };
});

exports.validateLogin = functions.https.onCall((data, context) => {
  console.log(JSON.stringify(data));
  const user = data.user;
  const pass = data.pass;
  return (user==="rnnn" && pass==="storefront") ||
          (user==="admin" && pass === "password")
});
