
package info.moonjava.face.recognition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.InputStream;
import java.util.List;

public class FaceRecognitionReactNativeModule extends ReactContextBaseJavaModule {

    private static final String TAG = "FaceRecognition";
    private static final String CLASS_NAME = "FaceRecognitionReactNative";
    private final ReactApplicationContext reactContext;
    private static final String ERROR_CANNOT_PROCESS_IMAGE_KEY = "E_CANNOT_PROCESS_IMAGE";
    private static final String ERROR_CANNOT_PROCESS_IMAGE_MSG = "Cannot read this image";
    private static final String ERROR_CANNOT_DETECT_FACE_KEY = "E_CANNOT_DETECT_FACE";

    public FaceRecognitionReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @ReactMethod
    public void detectFaces(final String imageUrl, final Promise promise) {
        try {
            Uri uri = Uri.parse(imageUrl);
            InputStream image_stream = reactContext.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
            FirebaseVisionFaceDetectorOptions options =
                    new FirebaseVisionFaceDetectorOptions.Builder()
                            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                            .build();
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options);
            Task<List<FirebaseVisionFace>> result =
                    detector.detectInImage(image)
                            .addOnSuccessListener(
                                    faces -> {
                                        WritableMap resultMap = new WritableNativeMap();
                                        if (faces != null && faces.size() > 0) {
                                            WritableArray resultFaces = new WritableNativeArray();
                                            for (FirebaseVisionFace face : faces) {
                                                Rect bounds = face.getBoundingBox();
                                                WritableMap rectMap = new WritableNativeMap();
                                                rectMap.putDouble("x", bounds.left);
                                                rectMap.putDouble("y", bounds.top);
                                                rectMap.putDouble("width", bounds.width());
                                                rectMap.putDouble("height", bounds.height());
                                                float eulerY = face.getHeadEulerAngleY();
                                                float eulerZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                                                WritableMap faceMap = new WritableNativeMap();
                                                faceMap.putMap("rect", rectMap);
                                                faceMap.putDouble("eulerY", eulerY);
                                                faceMap.putDouble("eulerZ", eulerZ);
                                                resultFaces.pushMap(faceMap);
                                            }
                                            resultMap.putArray("faces", resultFaces);
                                        }
                                        promise.resolve(resultMap);
                                    })
                            .addOnFailureListener(
                                    e -> {
                                        promise.reject(ERROR_CANNOT_DETECT_FACE_KEY, e.getLocalizedMessage(), e);
                                    });
        } catch (Throwable e) {
            promise.reject(ERROR_CANNOT_PROCESS_IMAGE_KEY, ERROR_CANNOT_PROCESS_IMAGE_MSG, e);
        }
    }
}