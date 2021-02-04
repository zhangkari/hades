package com.class100.yunshixun

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.class100.atropos.env.context.AtPrefs
import com.class100.hades.http.HaApiCallback
import com.class100.hades.http.HaApiResponse
import com.class100.hades.http.HaHttpClient
import com.class100.hades.http.ysx.YsxApiToken
import com.class100.yunshixun.model.request.ReqYsxCreateMeeting
import com.class100.yunshixun.model.request.ReqYsxMyMeetings
import com.class100.yunshixun.model.request.ReqYsxToken
import com.class100.yunshixun.model.response.RespYsxToken
import kotlinx.android.synthetic.main.activity_devops.*

class DevOpsActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DevOpsActivity"
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

    private fun init() {
        val data = AtPrefs.get(DEV_OPS_URL_HISTORY, "");
        et_url.setText(data)
        btn_go.setOnClickListener {

        }

        btn_camera.setOnClickListener {
        }

        btn_get_token.setOnClickListener {
            requestToken()
        }

        btn_create_meeting.setOnClickListener {
            createMeeting()
        }

        btn_my_meetings.setOnClickListener {
            requestMyMeetings()
        }
    }

    private fun requestToken() {
        HaHttpClient.getInstance().enqueue(
            ReqYsxToken(
                "15928695284"
            ),
            object : HaApiCallback<HaApiResponse<RespYsxToken>> {
                override fun onError(code: Int, message: String?) {
                    Log.d(TAG, "getToken error:$code, message")
                }

                override fun onSuccess(resp: HaApiResponse<RespYsxToken>?) {
                    resp?.data?.token?.let { YsxApiToken.saveToken(it) }
                    Log.d(TAG, "getToken ok:token=>${resp?.data?.token}")
                }
            })
    }

    private fun createMeeting() {
        HaHttpClient.getInstance().enqueue(
            ReqYsxCreateMeeting(),
            object : HaApiCallback<RespYsxToken> {
                override fun onError(code: Int, message: String?) {
                    Log.d(TAG, "createMeeting error:$code, message")
                }

                override fun onSuccess(content: RespYsxToken?) {
                    Log.d(TAG, "createMeeting ok:token=>${content?.token}")
                }
            })
    }

    private fun requestMyMeetings() {
        val req = ReqYsxMyMeetings();
        Log.d(TAG, "req url:" + req.completeUrl);
        HaHttpClient.getInstance().enqueue(
            req,
            object : HaApiCallback<RespYsxToken> {
                override fun onError(code: Int, message: String?) {
                    Log.d(TAG, "myMeetings error:$code, message")
                }

                override fun onSuccess(content: RespYsxToken?) {
                    Log.d(TAG, "myMeetings ok:token=>${content?.token}")
                }
            })
    }
}