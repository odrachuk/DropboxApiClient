package com.softsandr.dbx.ui

import com.dropbox.core.v2.files.DbxUserFilesRequests
import com.dropbox.core.v2.files.ListFolderResult
import com.softsandr.dbx.client.DbxClient
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers

object FilesUtils {
    fun loadFiles(client: DbxClient, directory: String): Observable<ListFolderResult?> {
        return Observable.just(client.files()).flatMap(Func1<DbxUserFilesRequests, Observable<ListFolderResult?>> { dbxUserFilesRequests ->
            return@Func1 Observable.just(dbxUserFilesRequests.listFolder(directory))
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}