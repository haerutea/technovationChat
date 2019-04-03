# Mock IA/Technovation
</br>
Assignment name: Mental Health chat app </br>
Student name: Tsz Yan Au
<h6>I apologize for the repo name...</6>
<p><b>Current login/signup is only available though email and password!</b></p>
<h2>Launching/running/debugging the app</h2>
The repo is an Android Studio project, so Android Studio is needed to run the repo as of now.  Currently the app launchs at AuthActivity (which is changed in AndroidManifest.xml but don't touch I guess)
just press the green arrow to run, make sure config is app, like below:  

![alt text](https://i.imgur.com/KBxB1ra.png)

<h4><b>Note: Although min target SDK is 16, devices with API 27 or higher are highly recommended.</b></h4>
</br>
<h2>Description of Solution</h2>
<h3>Overall goal</h3>
After logging in, the user will be met with the profile screen, displaying personal information, and has a button which allows user to and is also where the future implementation of the diary will be located.  After the user presses the chat button, the displayed screen will switch to a screen with multiple choices for different categories.  Through the categories chosen, the user will be matched with another user who 'fulfills' those requirements.

<h3>Current state</h3>
<ul>
  <li>Login/signup works.</li>
  <li>Added ConenctActivity – in charge of receiving choices from user and connecting user to another.</li>
  <li>Chat connecting broke again, a branch where both userOne and userTwo are the same user keeps being created.</li>
  <li>Edited AndroidManifest.xml file so correct labels are displayed and correct parents are assigned.</li>
  <li>Fixed Chat object so it flows better with Firebase.</li>
</ul>

<h3>Todo</h3>
<ul
  <li>Add save chat feature</li>
  <li>change 'if user is free' logic.  Right now user is 'free' if they're logged on.  Maybe allow users to set destinated timeframes?</li>
  <li>Create filter for inappropriate words in chats.</li>
  <li>OPTIONAL: add stats?</li>
  <li>LESS IMPORTANT: Maybe change app name under AndriodManifest.xml</li>
</ul>

<h3>Description of each Activity</h3>
<h4>AuthActivity</h4>
Where the user gets to choose to log in or sign up.  It also overrides the onStart() method to check if the user is already signed in, which it will then start the Profile Activity.  If user presses log in button, it switches to LogInActivity.  If user presses sign up button, it switches to SignUpActivity.

</br>
<h4>LogInActivity</h4>
A log in screen that offers login via email/password.  If log in button is pressed, if credentials are valid, start ProfileActivity.  Up button (or the back button) will go back to AuthActivity, as its hierarchical parent is AuthActivity.

</br>
<h4>SignUpActivity</h4>
A sign up screen that offers login via email/password.  If sign up button is pressed, if credentials are valid, start ProfileActivity.  Up button (or the back button) will go back to AuthActivity, as its hierarchical parent is AuthActivity.

</br>
<h4>ProfileActivity</h4>
A profile screen which shows the account's email address.  If chat button is pressed, start ConnectActivity.  If log out button is pressed, mAuth.signOut() is called  and start AuthActivity.

</br>
<h4>ConnectActivity</h4>
A screen with dropdown menus for users to choose preferences/focus of the chat.  If find user button is pressed, attempt to connect users.  If successful, start ChatActivity.  Else, display a Toast on screen stating the error message.  If built-in back button is pressed, start ProfileActivity.

</br>
<h4>ChatActivity</h4>
Where the chat happens, including display and sending of chat messages.  If built-in back button is pressed, delete chat log (unless specified) and start ProfileActivity.
