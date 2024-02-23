#import "MicroatmsdkPlugin.h"
#if __has_include(<microatmsdk/microatmsdk-Swift.h>)
#import <microatmsdk/microatmsdk-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "microatmsdk-Swift.h"
#endif

@implementation MicroatmsdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMicroatmsdkPlugin registerWithRegistrar:registrar];
}
@end
