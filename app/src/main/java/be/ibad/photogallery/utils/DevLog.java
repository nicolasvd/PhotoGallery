package be.ibad.photogallery.utils;

import android.util.Log;

import be.ibad.photogallery.BuildConfig;


public class DevLog {
    public static void log(int priority, String message) {
        if (BuildConfig.DEBUG && message != null) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.println(priority, className + "." + methodName + "():" + lineNumber, message);
        }
    }

    public static void d(String message) {
        if (BuildConfig.DEBUG && message != null) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.println(Log.DEBUG, className + "." + methodName + "():" + lineNumber, message);
        }
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG && message != null) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.println(Log.ERROR, className + "." + methodName + "():" + lineNumber, message);
        }
    }

    public static void i(String message) {
        if (BuildConfig.DEBUG && message != null) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.println(Log.INFO, className + "." + methodName + "():" + lineNumber, message);
        }
    }

    private static void printStackTrace(Exception e) {
        if (BuildConfig.DEBUG && e != null) {
            e.printStackTrace();
        }
    }

    public static void w(String message, Exception exception) {
        if (BuildConfig.DEBUG && message != null) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.println(Log.ERROR, className + "." + methodName + "():" + lineNumber, message);
            printStackTrace(exception);
        }
    }

}
