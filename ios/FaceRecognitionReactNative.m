
#import "FaceRecognitionReactNative.h"
#import <UIKit/UIKit.h>
#import <Firebase/Firebase.h>

#define ERROR_CANNOT_PROCESS_IMAGE_KEY @"E_CANNOT_PROCESS_IMAGE"
#define ERROR_CANNOT_PROCESS_IMAGE_MSG @"Cannot read this image"
#define ERROR_CANNOT_DETECT_FACE_KEY @"E_CANNOT_DETECT_FACE"

@implementation FaceRecognitionReactNative

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(detectFaces:(NSString*)source resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    @try {
        NSURL *originalURL;
        if([source containsString:@"assets-library"])
        {
            originalURL=[NSURL URLWithString:source];
        }
        else if([source hasPrefix:@"file://"]) {
            originalURL=[NSURL fileURLWithPath:[source substringFromIndex:7]];
        }
        else if([source hasPrefix:@"http://"]||[source hasPrefix:@"https://"]) {
            originalURL=[NSURL URLWithString:source];
        }
        else{
            originalURL=[NSURL URLWithString:source];
        }
        FIRVisionFaceDetectorOptions *options = [[FIRVisionFaceDetectorOptions alloc] init];
        options.performanceMode = FIRVisionFaceDetectorPerformanceModeAccurate;
        options.landmarkMode = FIRVisionFaceDetectorLandmarkModeAll;
        options.classificationMode = FIRVisionFaceDetectorClassificationModeAll;
        FIRVision *vision = [FIRVision vision];
        FIRVisionFaceDetector *faceDetector = [vision faceDetector];
        NSData *data = [NSData dataWithContentsOfURL:originalURL];
        UIImage *uiImage = [[UIImage alloc] initWithData:data];
        FIRVisionImage *image = [[FIRVisionImage alloc] initWithImage:uiImage];
        [faceDetector processImage:image
                        completion:^(NSArray<FIRVisionFace *> *faces,
                                     NSError *error) {
                            if (error != nil) {
                                reject(ERROR_CANNOT_DETECT_FACE_KEY, [error localizedDescription], error);
                            } else if (faces != nil&&[faces count]>0) {
                                NSMutableArray *faceResult=[NSMutableArray arrayWithCapacity:[faces count]];
                                for (FIRVisionFace *face in faces) {
                                    CGRect faceFrame = face.frame;
                                    CGFloat eulerY=0;
                                    CGFloat eulerZ=0;
                                    if (face.hasHeadEulerAngleY) {
                                        eulerY = face.headEulerAngleY;
                                    }
                                    if (face.hasHeadEulerAngleZ) {
                                        eulerZ = face.headEulerAngleZ;
                                    }
                                    [faceResult addObject:@{@"rect":@{
                                                                    @"x":[NSNumber numberWithFloat:faceFrame.origin.x],
                                                                    @"y":[NSNumber numberWithFloat:faceFrame.origin.y],
                                                                    @"width":[NSNumber numberWithFloat:faceFrame.size.width],
                                                                    @"height":[NSNumber numberWithFloat:faceFrame.size.height],
                                                                    },
                                                            @"eulerY":[NSNumber numberWithFloat:eulerY],
                                                            @"eulerZ":[NSNumber numberWithFloat:eulerZ]
                                                            }];
                                }
                                resolve(@{
                                          @"faces":faceResult
                                          });
                            }
                            else{
                                resolve(@{
                                          @"faces":[NSNull null]
                                          });
                            }
                        }];
    } @catch(NSException *e) {
        reject(ERROR_CANNOT_PROCESS_IMAGE_KEY, ERROR_CANNOT_PROCESS_IMAGE_MSG, nil);
    }
}

@end

