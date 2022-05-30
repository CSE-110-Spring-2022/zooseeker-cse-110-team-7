package com.example.zooseekercse110team7;


/**
 * The purpose of this class is to speed up (even if minor or negligible) the app if any particular
 * methods, functions, or logic is only needed during the development process.
 * As an example, if there is a loop to see items from an array, there is not need for that in the
 * final product so it should only be allowed in "debug mode".
 *
 * Usage:
 * if(GlobalDebug.DEBUG){
 *     //your code here
 *  }
 * */
public class GlobalDebug {
    public static boolean DEBUG = true; // set this to false on release
}

