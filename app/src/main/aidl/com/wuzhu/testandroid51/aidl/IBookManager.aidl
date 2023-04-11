// IBookManager.aidl
package com.wuzhu.testandroid51.aidl;
import com.wuzhu.testandroid51.aidl.IOnNewBookArrivedListener;
import com.wuzhu.testandroid51.aidl.Book;
// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}