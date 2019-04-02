import {NativeModules} from 'react-native';

const {FaceRecognitionReactNative} = NativeModules;

export function detectFaces(imageUrl) {
    return FaceRecognitionReactNative.detectFaces(imageUrl);
}
