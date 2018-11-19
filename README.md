# woopWoopWoop
wwwOOOOOOOOOPPPPPPPPPPPPPPPPPP
</br>
jk 
</br>
<h1>CURRENTLY LOGIN IS ONLY AVAILABLE THROUGH EMAIL & PASSWORD</h1>
<h2>Launching/running/debugging the app</h2>
ok so currently the app launchs at AuthActivity (which is changed in AndroidManifest.xml but don't touch I guess)
just press the green arrow to run, make sure config is app, like below:

![alt text](https://i.imgur.com/KBxB1ra.png)

</br>
<h2>AuthActivity</h2>
Where the user gets to choose to log in or sign up.  It also overrides the onStart() method to check if the user is already signed in, which it will then start the Profile Activity.  If user presses log in button, LogInActivity is started.  If user presses sign up button, SignUpActivity is started.

</br>
<h2>LogInActivity</h2>
Extends AuthActivity, a log in screen that offers login via email/password.  If log in button is pressed, if credentials are valid, start ProfileActivity.  Up button (or the back button) will go back to AuthActivity, as its hierarchical parent is AuthActivity.

</br>
<h2>SignUpActivity</h2>
Extends AuthActivity, a sign up screen that offers login via email/password.  If sign up button is pressed, if credentials are valid, start ProfileActivity.  Up button (or the back button) will go back to AuthActivity, as its hierarchical parent is AuthActivity.

</br>
<h2>ProfileActivity</h2>
Extends AuthActivity, a profile screen which shows the account's email address.  If log out button is pressed, mAuth.signOut() is called  and start AuthActivity.

<h6>thank u thank u</h6>
