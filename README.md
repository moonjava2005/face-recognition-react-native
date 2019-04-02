
# face-recognition-react-native

## Getting started

`$ npm install face-recognition-react-native --save`

Or

`$ yarn add face-recognition-react-native`

### Mostly automatic installation

`$ react-native link face-recognition-react-native`

### Manual installation


#### iOS

1. Add the following to your `Podfile` and run `pod update`:
```
pod 'FaceRecognitionReactNative', :path => '../node_modules/face-recognition-react-native'
```

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import info.moonjava.face.recognition.FaceRecognitionReactNativePackage;` to the imports at the top of the file
  - Add `new FaceRecognitionReactNativePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':face-recognition-react-native'
  	project(':face-recognition-react-native').projectDir = new File(rootProject.projectDir, 	'../node_modules/face-recognition-react-native/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':face-recognition-react-native')
  	```


## Usage
```javascript
import {detectFaces} from "face-recognition-react-native";
detectFaces(imageUrl:string).then(result => {
            
        }, e => {
            
        });
```

### Result Info
* rect: Rectangle of face.
* eulerY: A face with a positive Euler Y angle is turned to the camera's right and to its left.
* eulerZ: A face with a positive Euler Z angle is rotated counter-clockwise relative to the camera.

### Rect Info

* x
* y
* width
* height
