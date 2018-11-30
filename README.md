# Parking-App 1.0 Release Notes

NEW FEATURES
	Added User Registration and Login Capabilties
	Added the ability for a user to report open parking spots at their current location
	Improved Price Boundary Filter with dropdown menu
	Added Menu button for 1 click access to all major features
	Improved UI Colors for better visibility

BUG FIXES
	Application will no longer crash when setting filters for spot configurations not yet in the database
	Application will no longer crash in emulator due to device location being loaded improperly



Install Guide for Parking-App 1.0

PRE-REQUISITES
	You must have JDK 1.8 installed and configured before proceeding.  see sun.com/jdk/download
	You must have Android Studio installed and configured.
		The Android Studio Virtual Device should be the Nexus 5x Running API 27, Android 8.1
		Android Studio: https://developer.android.com/studio/
	In order to prevent potential unexpected charges to our team, you must create your own google-services.json file.
		Go to https://console.firebase.google.com/u/0/ and create a new project. Then click on the Android Icon in the 
		middle of the screen and fill out the information to create a google-services.json file.

DOWNlOAD
	https://github.com/jtpeterson/Parking-App/archive/master.zip

BUILD
	Place the google-services.json file in the Parking-App\CurrentPlaceDetailsOnMap\app directory
	Open in Android Studio and press the 'Make Project' Button 

RUNNING APPLICATION
	Within Android Studio Press the 'run' Button, make sure your Virtual Device is selected, and press OK. 