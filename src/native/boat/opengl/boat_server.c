
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <dlfcn.h>

bool (*eglSwapBuffers)();
bool (*eglMakeCurrent)();
int (*getWindowWidth)();
int (*getWindowHeight)();

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_BoatServer_init(JNIEnv *env, jclass clazz) {

	void* handle = dlopen("libserver.so", RTLD_LAZY);
	eglSwapBuffers = (bool (*)())dlsym(handle, "eglSwapBuffers");
	eglMakeCurrent = (bool (*)())dlsym(handle, "eglMakeCurrent");
	getWindowWidth = (int (*)())dlsym(handle, "getWindowWidth");
	getWindowHeight = (int (*)())dlsym(handle, "getWindowHeight");
	
}


JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_eglSwapBuffers(JNIEnv *env, jclass clazz) {

	return eglSwapBuffers();
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_eglMakeCurrent(JNIEnv *env, jclass clazz) {

	return eglMakeCurrent();
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_BoatServer_getWindowWidth(JNIEnv *env, jclass clazz) {
	
	return getWindowWidth();
	
}
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_BoatServer_getWindowHeight(JNIEnv *env, jclass clazz) {
	
	return getWindowHeight();
}


