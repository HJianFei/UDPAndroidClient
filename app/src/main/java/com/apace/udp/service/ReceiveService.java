package com.apace.udp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.apace.udp.socket.SocketUtil;
import com.apace.udp.thread.ReceiveThread;

public class ReceiveService extends Service {

    private ReceiveThread receiveThread;

    public ReceiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("onResponse", "onStartCommand");
        if (receiveThread == null || !receiveThread.isAlive()) {
            receiveThread = new ReceiveThread(SocketUtil.getSocketUtil().getDatagramSocket());
        }
        if (!receiveThread.isAlive()) {
            receiveThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        if (receiveThread.isInterrupted()) {
            receiveThread.interrupt();
        }
        super.onDestroy();
    }
}
