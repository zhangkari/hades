package com.class100.yunshixun

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.class100.atropos.env.context.AtPrefs
import com.class100.hades.http.HaApiCallback
import com.class100.hades.http.HaHttpClient
import com.class100.yunshixun.model.request.YsxTokenRequest
import kotlinx.android.synthetic.main.activity_devops.*

class DevOpsActivity : AppCompatActivity() {
    companion object {
        private val TAG = DevOpsActivity::javaClass.name
        private const val DEV_OPS_URL_HISTORY = "dev_ops_url_history";

        fun launch(context: Context) {
            context.startActivity(Intent(context, DevOpsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devops);
        init()
    }

    fun init() {
        val data = AtPrefs.get(DEV_OPS_URL_HISTORY, "");
        et_url.setText(data)
        btn_go.setOnClickListener {

        }

        btn_camera.setOnClickListener {
        }

        btn_get_token.setOnClickListener {
            HaHttpClient.getInstance().enqueue(
                YsxTokenRequest("15928695284", "ZSC1988love"),
                object : HaApiCallback<String> {
                    override fun onError(code: Int, message: String?) {
                        Log.d(TAG, "error:$code, message")
                    }

                    override fun onSuccess(content: String?) {
                        Log.d(TAG, "ok:$content")
                    }
                })
        }
    }
}