# SignatureView
Android library with OpenCV 3.4.3 which provides a signature view that obtain the signature of the user with the minimum bounding box

# Android Library Signature View
Android library with OpenCV 3.4.3 which provides a signature view that obtain the signature of the user with the minimum bounding box. Signature is returned in bitmap.

## Improvements
You are free for make any improvement. Please share your code improvements.

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
  
  See */ExampleActivity* for more detailed code example of how to use the library.
  
  * Add signature View
  ```
  <nsmarinro.librarysignature.SignatureView
            android:id="@+id/signatureView"
            android:layout_width="match_parent"
            android:layout_height="300dp" />
 ```
 
 * Get signature data
 
 ```signatureClear()``` - Clear Signature<br/> 
 ```getSignatureBitmap()``` - Get Signature without bounding box (Final signature)<br/>
 ```getSignatureBitmap(true)``` - Get Signature with bounding boxes (see the bounding boxes detected)<br/>
  ```isSignature()``` - if something is already drawn or not<br/>
 
 
 ## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
