#include <string.h>
#include <jni.h>
#include <dlfcn.h>
#include <stdio.h>

#include "extgl.h"

static void* lib_gl_handle;

bool extgl_Open(JNIEnv *env) {
	if (lib_gl_handle != NULL)
		return true;
	lib_gl_handle = dlopen("libGL.so.1", RTLD_LAZY | RTLD_GLOBAL);
	if (lib_gl_handle == NULL) {
		throwFormattedException(env, "Error loading libGL.so.1: %s", dlerror());
		return false;
	}
	return true;
}

void *extgl_GetProcAddress(const char *name) {
	void *t = dlsym(lib_gl_handle, name);
	if (t == NULL) {
	        printfDebug("Could not locate symbol %s\n", name);
	}
	return t;
}

void extgl_Close(void) {
	dlclose(lib_gl_handle);
	lib_gl_handle = NULL;
}