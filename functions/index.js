const functions = require('firebase-functions');
//all code from https://github.com/firebase/functions-samples/tree/master/fcm-notifications
const admin = require('firebase-admin');
admin.initializeApp();
/**
 * Triggers when a user gets a new requester and sends a notification.
 *
 * Requests adds a flag to `/requests/{requesteeUid}/uid`.
 * Users save their device notification tokens to `/users/{requesteeUid}/notificationTokens/{notificationToken}`.
 */

exports.requestNotif = functions.database.ref('/requests/{requestedUid}/uid/{requesterUid}')
    .onWrite((change, context) => {
      const requesterUid = context.params.requesterUid;
      const requestedUid = context.params.requestedUid;
      // If cancelled request, we exit the function.
      if (!change.after.val()) {
        return console.log('User ', requesterUid, 'un-followed user', requestedUid);
      }
      console.log('We have a new follower UID:', requesterUid, 'for user:', requestedUid);

      // Get the list of device notification tokens.
      const getDeviceTokensPromise = admin.database()
          .ref(`/users/${requestedUid}/token`).once('value');

      // Get the requester profile.
      const getFollowerProfilePromise = admin.auth().getUser(requesterUid);

      // The snapshot to the user's tokens.
      let tokensSnapshot;

      // The array containing all the user's tokens.
      let tokens;

      return Promise.all([getDeviceTokensPromise, getFollowerProfilePromise]).then(results => {
        tokensSnapshot = results[0];
        const follower = results[1];

        // Check if there are any device tokens.
        if (!tokensSnapshot.hasChildren()) {
          return console.log('There are no notification tokens to send to.');
        }
        console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
        console.log('Fetched requester profile', follower);

        // Notification details.
        const payload = {
          notification: {
            title: 'You have a new chat request!',
            body: `${follower.displayName} wants to talk to you.`,
          }
        };

        // Listing all tokens as an array.
        tokens = Object.keys(tokensSnapshot.val());
        console.log("tokens: ", tokens);
        // Send notifications to all tokens.
        return admin.messaging().sendToDevice(tokens, payload);
      }).then((response) => {
        // For each message check if there was an error.
        const tokensToRemove = [];
        response.results.forEach((result, index) => {
          const error = result.error;
          if (error) {
            console.error('Failure sending notification to', tokens[index], error);
            // Cleanup the tokens who are not registered anymore.
            if (error.code === 'messaging/invalid-registration-token' ||
                error.code === 'messaging/registration-token-not-registered') {
              tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
            }
          }
        });
        return Promise.all(tokensToRemove);
      });
});