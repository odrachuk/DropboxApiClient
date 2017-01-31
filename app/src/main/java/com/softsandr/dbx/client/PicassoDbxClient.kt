package com.softsandr.dbx.client

import android.content.Context
import android.net.Uri
import com.dropbox.core.DbxException
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.ThumbnailFormat
import com.dropbox.core.v2.files.ThumbnailSize
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import java.io.IOException

class PicassoDbxClient(ctx: Context, dbxClient: DbxClient) {
    lateinit var picasso: Picasso

    init {
        picasso = Picasso.Builder(ctx)
                .downloader(OkHttpDownloader(ctx))
                .addRequestHandler(ThumbnailRequestHandler(dbxClient, ThumbnailSize.W640H480))
                .build()
    }

    class ThumbnailRequestHandler(private val dbxClient: DbxClientV2,
                                  private val thumbnailSize: ThumbnailSize) : RequestHandler() {

        override fun canHandleRequest(data: Request): Boolean {
            return SCHEME == data.uri.scheme && HOST == data.uri.host
        }

        @Throws(IOException::class)
        override fun load(request: Request, networkPolicy: Int): RequestHandler.Result {
            try {
                val downloader = dbxClient.files().getThumbnailBuilder(request.uri.path)
                        .withFormat(ThumbnailFormat.JPEG)
                        .withSize(thumbnailSize)
                        .start()

                return RequestHandler.Result(downloader.inputStream, Picasso.LoadedFrom.NETWORK)
            } catch (e: DbxException) {
                throw IOException(e)
            }
        }

        companion object {

            private val SCHEME = "dropbox"
            private val HOST = "dropbox"

            fun buildPicassoUri(file: FileMetadata): Uri {
                return Uri.Builder()
                        .scheme(SCHEME)
                        .authority(HOST)
                        .path(file.pathLower).build()
            }
        }
    }
}