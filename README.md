# Android Library Signature View
* Written in Kotlin
* Written with OpenCV 3.4.3<br/> 
* Provides the signature with the minimum bounding box.
* Signature is returned in bitmap<br/>

<img align="center" width="283" height="448" src="https://user-images.githubusercontent.com/34654924/50117761-dad0cb80-021b-11e9-9267-68dc719295cd.jpeg" hspace="20"> <img align="center" width="283" height="448" src="https://user-images.githubusercontent.com/34654924/50117765-ddcbbc00-021b-11e9-8e42-8ae5f89133d2.jpeg"> 

## Improvements
You are free for make any improvement. Please share your code.

## Getting Started
This Library was developed for minSdkVersion 21 and targetSdkVersion 28 (for now)

### Installing

Add it in your root build.gradle at the end of repositories

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  
  Add the dependency
  
  ```
  dependencies {
	        implementation 'com.github.nsmarinro.SignatureView:librarySignature:0.1.1'
	}
  ```
  
  ## Usage
  
  See *app/src/main/java/nsmarinro/signatureview/ExampleActivity* for more detailed code example of how to use the library.
  
  * Add signature View
  ```
  <nsmarinro.librarysignature.SignatureView
            android:id="@+id/signatureView"
            android:layout_width="match_parent"
            android:layout_height="300dp" />
 ```
 
 * Get signature data
 
 ```signatureClear()``` - Clear Signature<br/> 
 ```getSignatureBitmap()``` - Get Signature without bounding box (final signature)<br/>
 ```getSignatureBitmap(true)``` - Get Signature with bounding boxes (draw the bounding boxes detected)<br/>
  ```isSignature()``` - if something is already drawn or not<br/>
 
 
 ## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
