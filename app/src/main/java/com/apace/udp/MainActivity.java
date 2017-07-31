package com.apace.udp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apace.udp.adapter.SimpleAdapter;
import com.apace.udp.entity.BaseMsg;
import com.apace.udp.entity.TargetInfo;
import com.apace.udp.entity.UdpMsg;
import com.apace.udp.service.ReceiveService;
import com.apace.udp.socket.SocketUtil;
import com.apace.udp.utils.ImageUtils;
import com.apace.udp.utils.StringValidationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private SocketUtil udpUtils;
    private EditText input_ip_port;
    private EditText input_text;
    private Button btnSend;
    private RecyclerView rv_content;
    private List<String> list = new ArrayList<>();
    private SimpleAdapter mAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UdpMsg message = (UdpMsg) msg.obj;
            list.add(message.getSourceDataString());
            Log.d("onResponse", message.getSourceDataString());
            mAdapter.notifyDataSetChanged();
        }
    };
    private ImageView imageView;
    private Button show_dialog;
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    // 裁剪后图片的宽(X)和高(Y),80 X 80 的正方形。
    private static int output_X = 90;
    private static int output_Y = 90;

    private static final String IMAGE_FILE_NAME = "apace.com.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_ip_port = (EditText) findViewById(R.id.input_ip_port);
        input_text = (EditText) findViewById(R.id.input_text);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        rv_content = (RecyclerView) findViewById(R.id.rv_cntent);
        mAdapter = new SimpleAdapter(list, this);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        rv_content.setAdapter(mAdapter);
        imageView = (ImageView) findViewById(R.id.select_img);
        show_dialog = (Button) findViewById(R.id.show_dialog);
        show_dialog.setOnClickListener(this);
        Intent intent = new Intent(this, ReceiveService.class);
        startService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String temp = input_ip_port.getText().toString().trim();
                String[] temp2 = temp.split(":");
                String text = input_text.getText().toString().trim();
                if (text.equals("")) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                TargetInfo targetInfo;
                if (temp2.length == 2 && StringValidationUtils.validateRegex(temp2[0], StringValidationUtils.RegexIP)
                        && StringValidationUtils.validateRegex(temp2[1], StringValidationUtils.RegexPort)) {
                    targetInfo = new TargetInfo(temp2[0], Integer.parseInt(temp2[1]));
                    if (udpUtils == null) {
                        udpUtils = SocketUtil.getSocketUtil();
                    }
                    udpUtils.config(new UDPConfig.Builder()
                            .setLocalPort(Integer.parseInt(temp2[1])).create());
                    udpUtils.sendMsg(new UdpMsg(text, targetInfo, BaseMsg.MsgType.Send));
                }
                break;
            case R.id.show_dialog:
                show_Dialog();
                break;
        }

    }

    private void show_Dialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.pick_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从本地相册选取图片作为头像
                choseHeadImageFromGallery();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tack_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统相机拍照
                choseHeadImageFromCameraCapture();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 调用系统相机拍照获取图片
     */
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    /**
     * 从系统图库中选择相片
     */
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG).show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 提取保存裁剪之后的图片数据
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            imageView.setImageBitmap(photo);
            try {
                byte[] bitmapByte = ImageUtils.getBitmapByte(photo);
                String temp = input_ip_port.getText().toString().trim();
                String[] temp2 = temp.split(":");
                TargetInfo targetInfo;
                if (temp2.length == 2 && StringValidationUtils.validateRegex(temp2[0], StringValidationUtils.RegexIP)
                        && StringValidationUtils.validateRegex(temp2[1], StringValidationUtils.RegexPort)) {
                    targetInfo = new TargetInfo(temp2[0], Integer.parseInt(temp2[1]));
                    if (udpUtils == null) {
                        udpUtils = SocketUtil.getSocketUtil();
                    }
                    udpUtils.config(new UDPConfig.Builder()
                            .setLocalPort(Integer.parseInt(temp2[1])).create());
                    udpUtils.sendMsg(new UdpMsg(bitmapByte, targetInfo, BaseMsg.MsgType.Send));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

}

