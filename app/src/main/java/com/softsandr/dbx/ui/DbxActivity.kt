package com.softsandr.dbx.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.HORIZONTAL
import android.util.Log
import com.softsandr.dbx.R
import com.softsandr.dbx.client.DbxClient
import com.softsandr.dbx.client.PicassoDbxClient
import kotlinx.android.synthetic.main.main.*

class DbxActivity : AppCompatActivity() {

    private var dbxClient: DbxClient? = null
    private var carouselAdapter: CarouselAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initDbx()
        initUi()
    }

    private fun initDbx() {
        dbxClient = DbxClient(getString(R.string.dbx_app_oauth2_token), CLIENT_IDENTIFIER)
    }

    private fun initUi() {
        dbxClient?.let {
            carouselAdapter = CarouselAdapter(PicassoDbxClient(DbxActivity@ this, it).picasso, null)
            main_recycler?.layoutManager = LinearLayoutManager(DbxActivity@ this, HORIZONTAL, false)
            main_recycler?.adapter = carouselAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        dbxClient?.let {
            FilesUtils.loadFiles(it, "/Photos").subscribe {
                Log.d(LOG_TAG, "" + it?.entries?.size)
                it?.entries?.let { it1 -> carouselAdapter?.setItems(it1) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        val LOG_TAG = DbxActivity::class.java.simpleName
        val CLIENT_IDENTIFIER = "dropbox_api_demo"
    }
}