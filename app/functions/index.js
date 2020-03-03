const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// Limit log database size
// Max number of logs
const MAX_LOG_COUNT = 3;

// Removes siblings of the node that element that triggered the function if there are more than MAX_LOG_COUNT.
exports.logAdded = functions.firestore.document('bmwLog/{docID}').onWrite((change, context) => {
     if (!change.before.exists) {
         // New document Created : add one to count
         db.doc(docRef).update({numberOfDocs: FieldValue.increment(1)});
     }

 return;
 });

exports.truncate = functions.database.ref('bmwLog').onWrite(async (change) => {
  const parentRef = change.after.ref.parent;
  const snapshot = await parentRef.once('value');
  if (snapshot.numChildren() >= MAX_LOG_COUNT) {
    let childCount = 0;
    const updates = {};
    snapshot.forEach((child) => {
      if (++childCount <= snapshot.numChildren() - MAX_LOG_COUNT) {
        updates[child.key] = null;
      }
    });
    // Update the parent. This effectively removes the extra children.
    return parentRef.update(updates);
  }
  return null;
});