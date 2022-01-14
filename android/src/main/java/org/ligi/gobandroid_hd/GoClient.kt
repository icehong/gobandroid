package org.ligi.gobandroid_hd

import android.os.Handler
import android.util.Log
import kotlinx.coroutines.*
import java.io.*
import java.lang.Runnable
import java.lang.Thread.sleep
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.concurrent.SynchronousQueue

class GoClient (private val ip: String, private val port: Int){
    //普通数据交互接口
    private var sc: Socket? = null

    //普通交互流
    private var dout: OutputStream? = null
    private var din: InputStreamReader? = null

    //已连接标记
    val bConnected get() = sc != null && din != null && dout != null
    private val queue = SynchronousQueue<String>(true)

    private lateinit var threadsend: Thread

    /**
     * 初始化普通交互连接
     */
    fun initConnect() :Boolean {
        var i = 0 ;
        while (!bConnected && i < 5){
            try {
                sc = Socket(ip, port) //通过socket连接服务器
                din = InputStreamReader(sc?.getInputStream(),"UTF-8")
                dout = sc?.getOutputStream()    //获取输出流
                sc?.soTimeout = 10000  //设置连接超时限制
                if (bConnected) {
                    Log.d("GoClient", "connect server successful")
                    return true
                } else {
                    Log.d("GoClient", "connect server failed(${i}), now retry...")
                }
            } catch (e: IOException) {      //获取输入输出流是可能报IOException的，所以必须try-catch
                e.printStackTrace()
            }
            i++
            sleep((1000 * i).toLong())
        }
        return false
    }


    fun asynSendMessage(message: String?) {
        queue.put(message)
    }
    fun sendMessage(message: String?) : Boolean{
         Log.d("GoClient", "SEND: ${message}")
         return sendMessage(message?.toByteArray())
    }

    fun sendMessage(message: ByteArray?): Boolean {
        try {
            if (bConnected) {
                if (message != null) {        //判断输出流或者消息是否为空，为空的话会产生null pointer错误
                    dout?.write(message)
                    dout?.flush()
                    return true
                } else Log.d("GoClient", "The message to be sent is empty")
            }
        } catch (e: IOException) {
            Log.d("GoClient", "send message failed: crash")
        }
        return false
    }

    fun sendThread() {
        val mRunnable = Runnable {
            run {
                while (bConnected) {
                    try {
                        val msg = queue.take()
                        Log.d("GoClient", "sendMessage ${msg}")
                        sendMessage(msg)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        break
                    }
                }

            }
        }
        threadsend = Thread(mRunnable)
        threadsend.start()
    }

    fun receiveThread(mHandler: Handler){
        val mRunnable = Runnable {
            run {
                val inMessage = CharArray(1024)
                while (bConnected) {
                    try {
                        val a = din?.read(inMessage) //a存储返回消息的长度
                        if (a == null || a <= -1) {	//接受到的消息没有长度，即代表服务端发送了空的消息
                            sleep(1000)
                            Log.d("GoClient", "no message, delay 1000ms.")
                            continue
                        }
                        var recvMsg = String(inMessage, 0, a) //用string的构造方法来转换字符数组为字符串
                        mHandler.obtainMessage(0,recvMsg).sendToTarget();
                        Log.d("GoClient", "receive: $recvMsg")
                    } catch (e: SocketTimeoutException) {
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        Thread(mRunnable).start()
        Log.d("GoClient", "receive thread started ")
    }

    fun processGTP(command: String): String {
        var rst = "? unknown error \n"
        if (!sendMessage(command + "\n"))
            return rst

        val inMessage = CharArray(1024)
        var len = 0 //每次接收长度
        var length = 0 //已收数据长度
        do {
            try {
                len = din?.read(inMessage, length, 1024 - length)!!
                length += len
            } catch (e: SocketTimeoutException) {
                continue
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        } while (length < 2 || !(inMessage[length - 1] == '\n' && inMessage[length - 2] == '\n'))
        rst = String(inMessage, 0, length)
        Log.d("GoClient", "LEN:${length} RECV:${rst}")
        return rst
    }

    /**
     * 关闭连接
     */
    fun closeConnect() = try {
        din?.close()
        dout?.close()
        sc?.close()
        sc = null
        din = null
        dout = null
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }

    fun close(){
        if(::threadsend.isInitialized)  threadsend.interrupt()
        closeConnect()
    }
}