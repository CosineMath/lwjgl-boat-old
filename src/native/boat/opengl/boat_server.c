
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <dlfcn.h>
#include <common_tools.h>



bool (*eglSwapBuffers)();
bool (*eglMakeCurrent)(void*);
void* (*eglCreateContext)(void*);
bool (*eglDestroyContext)(void*);
void* (*eglGetCurrentContext)();
bool (*eglSwapInterval)(int);

int (*getWindowWidth)();
int (*getWindowHeight)();

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_BoatServer_init(JNIEnv *env, jclass clazz) {

	void* handle = dlopen("libserver.so", RTLD_LAZY);
	eglSwapBuffers = (bool (*)())dlsym(handle, "eglSwapBuffers");
	eglMakeCurrent = (bool (*)(void*))dlsym(handle, "eglMakeCurrent");
	eglCreateContext = (void* (*)(void*))dlsym(handle, "eglCreateContext");
	eglDestroyContext = (bool (*)(void*))dlsym(handle, "eglDestroyContext");
	eglGetCurrentContext = (void* (*)(void*))dlsym(handle, "eglGetCurrentContext");
	eglSwapInterval = (bool (*)(int))dlsym(handle, "eglSwapInterval");
	getWindowWidth = (int (*)())dlsym(handle, "getWindowWidth");
	getWindowHeight = (int (*)())dlsym(handle, "getWindowHeight");
}


JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_eglSwapBuffers(JNIEnv *env, jclass clazz) {

	return eglSwapBuffers();
}
JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_eglSwapInterval(JNIEnv *env, jclass clazz, jint value) {

	return eglSwapInterval(value);
}
JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_eglMakeCurrent(JNIEnv *env, jclass clazz, jobject context_handle) {
	void** context = (*env)->GetDirectBufferAddress(env, context_handle);
	return eglMakeCurrent(*context);
}


JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_releaseCurrentContext(JNIEnv *env, jclass clazz) {
	return eglMakeCurrent(NULL);
}
JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_BoatServer_eglCreateContext(JNIEnv *env, jclass clazz, jobject shared_context_handle) {
	jobject context_handle = newJavaManagedByteBuffer(env, sizeof(void*));
	if (context_handle == NULL) {
		throwException(env, "Could not allocate handle buffer");
		return NULL;
	}
	void** context = (*env)->GetDirectBufferAddress(env, context_handle);
        
	void* shared_context = NULL;
	if (shared_context_handle != NULL) {
		shared_context = *(void**)(*env)->GetDirectBufferAddress(env, shared_context_handle);
	}
	
	*context = eglCreateContext(shared_context);
	
	if (context == NULL) {
		throwException(env, "Could not create EGL context");
		
	}

	return context_handle;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_isCurrentContext (JNIEnv *env, jclass clazz, jobject context_handle) {
	void** context = (*env)->GetDirectBufferAddress(env, context_handle);
	return (*context) == eglGetCurrentContext();
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_BoatServer_eglDestroyContext (JNIEnv *env, jclass clazz, jobject context_handle) {
	void** context = (*env)->GetDirectBufferAddress(env, context_handle);
	return eglDestroyContext(*context);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_BoatServer_getWindowWidth(JNIEnv *env, jclass clazz) {
	
	return getWindowWidth();
	
}
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_BoatServer_getWindowHeight(JNIEnv *env, jclass clazz) {
	
	return getWindowHeight();
}


