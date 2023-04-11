// IOnNewBookArrivedListener.aidl
package com.wuzhu.testandroid51.aidl;

import com.wuzhu.testandroid51.aidl.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}