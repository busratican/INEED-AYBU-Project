#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ineed_ybu_ineed_1app_1lastest_LoginActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
