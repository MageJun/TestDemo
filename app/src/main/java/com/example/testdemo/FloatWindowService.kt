package com.example.testdemo

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.GestureDetector.*
import android.widget.TextView
import android.app.usage.UsageStatsManager.INTERVAL_DAILY
import android.content.ActivityNotFoundException
import android.text.TextUtils
import androidx.annotation.NonNull
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*




class FloatWindowService:Service() {



    var mWm: WindowManager?=null
    var mLayoutParams:WindowManager.LayoutParams?=null
    private var infoContent:TextView?=null
    val  view: View by lazy {
        val view = LayoutInflater.from(this).inflate(R.layout.float_view,null,false)
        infoContent = view.findViewById(R.id.info_content)
        view.setOnTouchListener(FloatOnTouchListener())
        view
    }
    val handle = Handler(Handler.Callback {
          val info = it.data!!.getString("info")
        infoContent!!.text = info
         false
    })
//    private val mTask = InnerTask(MyApplication.sInstance!!.applicationContext)
    var mListener:OnClickListener?=null
    fun setListener(listener: OnClickListener){
        mListener = listener
    }
    val mGen =GestureDetector(MyApplication.sInstance,object: OnGestureListener{
        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return false
        }

        override fun onLongPress(p0: MotionEvent?) {
        }

        override fun onShowPress(p0: MotionEvent?) {
        }

        override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            mListener!!.onClick()
            return true
        }

        override fun onDown(p0: MotionEvent?): Boolean {
            return false
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return true
        }


    })
    override fun onBind(p0: Intent?): IBinder? {
        return InnerBinder()
    }

    override fun onCreate() {
        super.onCreate()
        mWm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams()
        mLayoutParams!!.gravity=Gravity.LEFT or Gravity.TOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams!!.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //添加悬浮窗不可聚焦，这样才能操作悬浮窗以外的内容
        mLayoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mLayoutParams!!.width = resources.getDimensionPixelSize(R.dimen.dp_120dp)
        mLayoutParams!!.height = resources.getDimensionPixelSize(R.dimen.dp_200dp)
        mLayoutParams!!.x = 0
        mLayoutParams!!.y = 0




    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startFloatWindow()
        return super.onStartCommand(intent, flags, startId)
    }

    fun startFloatWindow() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(Settings.canDrawOverlays(this)){
                mWm!!.addView(view,mLayoutParams)
            }
        }else{
            mWm!!.addView(view,mLayoutParams)
        }
        infoContent.also {
            val isAccessibleOn = isAccessibilityServiceOn()
            it!!.text=if(isAccessibleOn){"服务服务已开启"} else {"服务服务未开启，点击开启辅助服务"}
            it!!.setOnClickListener {
                if(!isAccessibleOn){
                    val intent = Intent(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
        EventBus.getDefault().register(this)
    }


    fun stopFloatWindow(){
        EventBus.getDefault().unregister(this)
        mWm!!.removeViewImmediate(view)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessageEvent(event:MessageEvent){
        val classInfo = event.info
        infoContent!!.text = classInfo
    }

    inner class InnerBinder :Binder(){
        fun getService():FloatWindowService{
            return this@FloatWindowService
        }
    }

    private inner class FloatOnTouchListener:View.OnTouchListener{
        var x=0
        var y=0
        var downX=0
        var downY=0

        var canMove  =false
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//            mGen.onTouchEvent(p1)
            when(p1!!.action){
                MotionEvent.ACTION_DOWN->{
                    x = p1.rawX.toInt()
                    y = p1.rawY.toInt()
                    downX = x
                    downY =y
                    canMove = false
                }

                MotionEvent.ACTION_MOVE->{
                    val tmpX = p1.rawX.toInt()
                    val tmpY = p1.rawY.toInt()
                    val dx = tmpX-x
                    val dy =tmpY-y
                    if(Math.abs(dx)>5&&Math.abs(dy)>5){
                        canMove = true
                    }

                    if(canMove) {
                        mLayoutParams!!.x = mLayoutParams!!.x + dx
                        mLayoutParams!!.y = mLayoutParams!!.y + dy
                        x = tmpX
                        y = tmpY
                        mWm!!.updateViewLayout(view, mLayoutParams)
                    }
                }
                MotionEvent.ACTION_UP->{
                    if((Math.abs(downX-p1.rawX)<10)&&(Math.abs(downY-p1.rawY)<10)){
                        mListener!!.onClick()
                    }
                }


            }

            return false
        }
    }

    interface OnClickListener{
        fun onClick()
    }
    //判断辅助服务是否开启
    fun Context.isAccessibilityServiceOn(): Boolean {
        var service = "${packageName}/${AccessibilityHelperService::class.java.canonicalName}"
        var enabled = Settings.Secure.getInt(applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        var splitter = TextUtils.SimpleStringSplitter(':')
        if (enabled == 1) {
            var settingValue = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                splitter.setString(settingValue)
                while (splitter.hasNext()) {
                    var accessibilityService = splitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /*private inner class InnerTask(var context:Context):Runnable{
        var isRunning = false
        var lastCls =""
        var isReuqestPermission = false
        override fun run() {
            while (isRunning) {
                val string = getTopActivity()
                if(lastCls != string) {
                    lastCls = string
                    val msg = Message.obtain()
                    val bundle = Bundle()
                    bundle.putString("info", string)
                    msg.data = bundle
                    handle.sendMessage(msg)
                }
                Thread.sleep(1*1000)
            }
        }

        private fun getTopActivity():String{
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
                val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val cn = am.getRunningTasks(1)[0].topActivity
                Log.d("FloatWindowService", "topActivity = " + cn!!.className)
                return "${cn!!.className}"
            }else{
                if(checkAppUsagePermission(context)){
                    return getTopActivityPackageName(context)
                }else{
                    if(!isReuqestPermission){

                        requestAppUsagePermission(context)
                        isReuqestPermission = true
                    }
                }
            }

            return "unknown"
        }

        fun checkAppUsagePermission(context: Context): Boolean {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                val usageStatsManager =
                    context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager ?: return false
                val currentTime = System.currentTimeMillis()
                // try to get app usage state in last 1 min
                val stats = usageStatsManager.queryUsageStats(
                    INTERVAL_DAILY,
                    currentTime - 60 * 1000,
                    currentTime
                )
                return stats.size != 0
            }else{

                return true
            }

        }

        fun requestAppUsagePermission(context: Context) {
            val intent = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.i("FloatWindowService", "Start usage access settings activity fail!")
            }

        }


        fun getTopActivityPackageName(@NonNull context: Context): String {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                    ?: return "Unknown"

                var topActivityPackageName = "Unknown"
                val time = System.currentTimeMillis()
                // 查询最后十秒钟使用应用统计数据
                val usageStatsList =
                    usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
                // 以最后使用时间为标准进行排序
                if (usageStatsList != null) {
                    val sortedMap = TreeMap<Long, UsageStats>()
                    for (usageStats in usageStatsList) {
                        sortedMap.put(usageStats.lastTimeUsed, usageStats)
                    }
                    if (sortedMap.size !== 0) {
                        topActivityPackageName = sortedMap.get(sortedMap.lastKey())!!.getPackageName()
                        Log.d("FloatWindowService", "Top activity package name = $topActivityPackageName")
                    }
                }
            return topActivityPackageName
            }
            return "Unknown"
        }


    }*/


}