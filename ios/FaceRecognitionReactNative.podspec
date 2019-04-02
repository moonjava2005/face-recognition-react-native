require 'json'
package = JSON.parse(File.read(File.join(__dir__, 'package.json')))
Pod::Spec.new do |s|
  s.name         = "FaceRecognitionReactNative"
  s.version      = package['version']
  s.summary      = package['description']
  s.description  = package['description']
  s.homepage     = package['homepage']
  s.license      = package['license']
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = package['author']
  s.platform     = :ios, "9.0"
  s.source       = { :git => "git://github.com/moonjava2005/face-recognition-react-native.git", :tag => s.version }
  s.source_files  = "FaceRecognitionReactNative/**/*.{h,m}"
  s.requires_arc = true


  s.dependency 'React'
  s.dependency 'Firebase/MLVision'
  s.dependency 'Firebase/MLVisionFaceModel'

end

  