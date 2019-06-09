# BottomSheet

This is a custom bottom sheet where height is specified on the bases of the user requirement irrespective of any quantity of data. If the bottom sheet contains even a single line of word still it will open as per the height specified by the user. And when user scrolls down the bottom sheet, it will go back to its previous state & then it will be closed.

# Installation Process

Add in the Project Gradle :

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

Add this dependency in App gradle: 

dependencies {
    implementation 'com.github.baggolli:BottomSheet:1.0'
}
