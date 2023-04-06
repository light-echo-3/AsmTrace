// IOnNewBookArrivedListener.aidl
package com.example.testandroid51.aidl;

import com.example.testandroid51.aidl.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}