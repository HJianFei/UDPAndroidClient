package com.apace.udp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apace.udp.entity.BaseMsg;
import com.apace.udp.entity.TargetInfo;
import com.apace.udp.entity.UdpMsg;
import com.apace.udp.listener.UdpListener;
import com.apace.udp.utils.StringValidationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, UdpListener {


    private UDPUtils udpUtils;
    private EditText input_ip_port;
    private EditText input_text;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_ip_port = (EditText) findViewById(R.id.input_ip_port);
        input_text = (EditText) findViewById(R.id.input_text);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (udpUtils != null) {
            udpUtils.removeUdpClientListener(this);
            udpUtils.stopUdpServer();
        }
    }

    @Override
    public void onClick(View v) {
        String temp = input_ip_port.getText().toString().trim();
        String[] temp2 = temp.split(":");
        String text = input_text.getText().toString().trim();
        TargetInfo targetInfo;
        if (temp2.length == 2 && StringValidationUtils.validateRegex(temp2[0], StringValidationUtils.RegexIP)
                && StringValidationUtils.validateRegex(temp2[1], StringValidationUtils.RegexPort)) {
            targetInfo = new TargetInfo(temp2[0], Integer.parseInt(temp2[1]));
            if (udpUtils == null) {
                udpUtils = UDPUtils.getUdpClient();
                udpUtils.addUdpClientListener(this);
            }
            udpUtils.config(new UdpClientConfig.Builder()
                    .setLocalPort(Integer.parseInt(temp2[1])).create());
            udpUtils.sendMsg(new UdpMsg(text, targetInfo, BaseMsg.MsgType.Send), false);
        }
    }


    @Override
    public void onStarted(UDPUtils udpUtils) {

    }

    @Override
    public void onStoped(UDPUtils udpUtils) {

    }

    @Override
    public void onSended(UDPUtils udpUtils, UdpMsg udpMsg) {
    }

    @Override
    public void onReceive(UDPUtils client, UdpMsg msg) {
    }

    @Override
    public void onError(UDPUtils client, String msg, Exception e) {
    }
}

