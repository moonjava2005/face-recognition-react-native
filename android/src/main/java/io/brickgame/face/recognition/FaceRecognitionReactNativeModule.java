
package io.brickgame.face.recognition;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class FaceRecognitionReactNativeModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public FaceRecognitionReactNativeModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "FaceRecognitionReactNative";
  }
}