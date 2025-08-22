package com.viettel.tvbox

import android.app.Application
import com.viettel.tvbox.services.RetrofitInstance

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.init(this)
    }
}

