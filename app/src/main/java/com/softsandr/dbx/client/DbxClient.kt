package com.softsandr.dbx.client

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.http.OkHttp3Requestor
import com.dropbox.core.v2.DbxClientV2

class DbxClient(accessToken: String, clientIdentifier: String) : DbxClientV2(
        DbxRequestConfig.newBuilder(clientIdentifier).withHttpRequestor(OkHttp3Requestor.INSTANCE).build(),
        accessToken)
