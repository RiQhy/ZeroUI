# ZeroUI
School project. <br>
App was made with kotlin. <br>
App for testing different Zero UI elements. 

# Starting The Project
Use Android Studio and android phone to use the project. <br>
It is recommended to use android phone because emulator doesn't support all the sensors needed for the app. <br>
Also newer versions of the vision-task("com.google.mediapipe:tasks-vision:0.10.0") will cause errors and it tries to use one thread for more than one action. <br>
Getting the recognizer to work in combosable was not possible as of this time.

# Views and what they mean
Motion Sensor view is where the app uses sensors to detect phones movement. <br>
In Gaze view the app tracks users eye movement. <br>
Gesture view uses camera to see users hand(s) to recognize different hand signs and displayes what the hand sign is in our app.

# What can you do in the app?
User can use voice to control where to go in Front view. <br>
User can check Motion Sensor view. There is a step counter and rotation vector. <br>
User can check Gaze view. There through camera the app will recognize users face and if eyes are open or closed. <br>
Also in Gaze view if user closes eyes for approximately 5 seconds the user will be navigated back to Front View. <br>
If in Gaze view camera doesnt detect face for 30 seconds the app will close. <br>
User can check Gesture view. There through camera user can make different hand signs and the app recognizes them. <br>
There are currently seven recognized hand signs implemented. <br>

# Screenshots

<img src= "https://github.com/RiQhy/ZeroUI/blob/main/app/src/main/res/drawable/zerouifrontpage.jpg" width="250" height="500">
<img src= "https://github.com/RiQhy/ZeroUI/blob/main/app/src/main/res/drawable/zerouigazeview.jpg" width="250" height="500"> <br>
<img src= "https://github.com/RiQhy/ZeroUI/blob/main/app/src/main/res/drawable/zerouigesturecamerascreen.jpg" width="250" height="500">
<img src= "https://github.com/RiQhy/ZeroUI/blob/main/app/src/main/res/drawable/zerouigesturegallery.jpg" width="250" height="500"> <br>
<img src= "https://github.com/RiQhy/ZeroUI/blob/main/app/src/main/res/drawable/zerouimotionsensor.jpg" width="250" height="500">


# Team
Christian Olkkonen - Log Keeper <br>
Emil Zghaib - Decider <br>
Semen Morozov - Expert Coder <br>
Riku Nokelainen - Faciliator
