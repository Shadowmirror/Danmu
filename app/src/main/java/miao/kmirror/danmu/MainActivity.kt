package miao.kmirror.danmu

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miao.kmirror.danmu.handler.event.simpleEventHandler
import miao.kmirror.danmu.handler.message.simpleMessageHandler
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, TextToSpeech.OnInitListener {

    private var liveNumber: Int = 0
    private val TAG = "MainActivity"
    private val danmuList = ArrayList<String>()
    private lateinit var btnEnter: Button
    private lateinit var etLiveNumber: EditText
    private lateinit var danmuListView: RecyclerView
    private lateinit var adapter: DanmuAdapter
    private lateinit var mSpeech: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSpeech = TextToSpeech(applicationContext, this)
        mSpeech.language = Locale.CHINA
        btnEnter = findViewById(R.id.btnEnter)
        etLiveNumber = findViewById(R.id.etLiveNumber)
        danmuListView = findViewById(R.id.danmuListView)

        val linearLayoutManager = LinearLayoutManager(this)
        danmuListView.layoutManager = linearLayoutManager

        adapter = DanmuAdapter(danmuList)
        danmuListView.adapter = adapter
        btnEnter.setOnClickListener {
            var temp = etLiveNumber.text.toString()
            if (temp.isEmpty()) temp = "0"
            liveNumber = temp.toInt()
            Log.d(TAG, "onCreate: $liveNumber")
            if (liveNumber <= 0) {
                Toast.makeText(this, "直播间号不能为空或0", Toast.LENGTH_SHORT).show()
            } else {
                loginLive(liveNumber)
            }
        }
    }

    fun loginLive(liveNumber: Int) {
        val cirrus = Cirrus(
            eventHandler = simpleEventHandler {
                onConnected {
                    launch {
                        withContext(Dispatchers.Main){
                            adapter.addData("进入直播间成功")
                            mSpeech.speak("进入直播间成功", TextToSpeech.QUEUE_ADD, null, "")
                            danmuListView.scrollToPosition(adapter.itemCount - 1)

                        }
                    }
                }

            },
            messageHandler = simpleMessageHandler {
                onReceiveDanmaku { user, said ->
                    Log.d("弹幕消息", "$user: $said")
                    launch {
                        withContext(Dispatchers.Main) {
                            adapter.addData("$user: $said")
                            mSpeech.speak("$user 说： $said", TextToSpeech.QUEUE_ADD, null, "")
                            danmuListView.scrollToPosition(adapter.itemCount - 1)
                        }
                    }

                }
                onUserEnterInLiveRoom {
                    Log.d("用户进入", "$it 进入直播间")
                    launch {
                        withContext(Dispatchers.Main) {
                            adapter.addData("$it 进入直播间")
                            mSpeech.speak("欢迎 $it 加入直播间", TextToSpeech.QUEUE_ADD, null, "")
                            danmuListView.scrollToPosition(adapter.itemCount - 1)
                        }
                    }
                }
                onReceiveGift { user, num, giftName ->
                    launch {
                        withContext(Dispatchers.Main) {
                            adapter.addData("$user 送了 $num 个$giftName")
                            mSpeech.speak(
                                "谢谢 $user 送的 $num 个$giftName",
                                TextToSpeech.QUEUE_ADD,
                                null,
                                ""
                            )
                            danmuListView.scrollToPosition(adapter.itemCount - 1)
                        }
                    }
                }
            }
        )
        cirrus.connectToBLive(liveNumber)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.i(TAG, "onInit: TTS init Success")
        } else {
            Log.i(TAG, "onInit: TTS init False")
        }
    }

    override fun onDestroy() {
        mSpeech.stop()
        mSpeech.shutdown()
        super.onDestroy()
    }
}