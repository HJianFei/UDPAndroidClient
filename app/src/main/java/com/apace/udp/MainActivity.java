package com.apace.udp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apace.udp.bean.TargetInfo;
import com.apace.udp.bean.TcpMsg;
import com.apace.udp.bean.UdpMsg;
import com.apace.udp.listener.UdpClientListener;
import com.apace.udp.utils.StringValidationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, UdpClientListener {
    private EditText udpclientEditIp;
    private EditText udpclientEdit;
    private Button udpclientBuSend;
    private XUdp mXUdp;
    private SwitchCompat udpclientSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        udpclientEditIp = (EditText) findViewById(R.id.udpclient_edit_ip);
        udpclientEdit = (EditText) findViewById(R.id.udpclient_edit);
        udpclientBuSend = (Button) findViewById(R.id.udpclient_bu_send);
        udpclientSwitch = (SwitchCompat) findViewById(R.id.udpclient_switch);
        udpclientBuSend.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mXUdp != null) {
            mXUdp.removeUdpClientListener(this);
            mXUdp.stopUdpServer();
        }
    }

    @Override
    public void onClick(View v) {
        String temp = udpclientEditIp.getText().toString().trim();
        String[] temp2 = temp.split(":");
        String text = udpclientEdit.getText().toString().trim();
        TargetInfo targetInfo;
        if (temp2.length == 2 && StringValidationUtils.validateRegex(temp2[0], StringValidationUtils.RegexIP)
                && StringValidationUtils.validateRegex(temp2[1], StringValidationUtils.RegexPort)) {
            targetInfo = new TargetInfo(temp2[0], Integer.parseInt(temp2[1]));
            if (mXUdp == null) {
                mXUdp = XUdp.getUdpClient();
                mXUdp.addUdpClientListener(this);
            }
            mXUdp.config(new UdpClientConfig.Builder()
                    .setLocalPort(8989).create());
            mXUdp.sendMsg(new UdpMsg(text, targetInfo, TcpMsg.MsgType.Send), udpclientSwitch.isChecked());
        } else {
        }
    }



    @Override
    public void onStarted(XUdp XUdp) {


    }

    @Override
    public void onStoped(XUdp XUdp) {

    }

    @Override
    public void onSended(XUdp XUdp, UdpMsg udpMsg) {
    }

    @Override
    public void onReceive(XUdp client, UdpMsg msg) {
    }

    @Override
    public void onError(XUdp client, String msg, Exception e) {
    }
}

