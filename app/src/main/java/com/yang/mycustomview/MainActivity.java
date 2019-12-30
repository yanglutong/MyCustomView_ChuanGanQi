package com.yang.mycustomview;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private Handler handler1;
    /**
     * handler子线程
     */
    private Button mBt;
    private String string;
    private SensorManager manager;
    /**
     * X
     */
    private Button mX;
    /**
     * Y
     */
    private Button mY;
    /**
     *
     */
    private Button mZ;
    /**
     * 方向传感器X
     */
    private Button mXx;
    /**
     * 方向传感器Y
     */
    private Button mYy;
    /**
     * 方向传感器Z
     */
    private Button mZz;
    private TextView mTvSun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initCustom();
    }

    private void initCustom() {
        //获取传感器管理器
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //通过管理器获取到每一个传感器 Sensor.type_all 所有的
        List<Sensor> list = manager.getSensorList(Sensor.TYPE_ALL);
        //遍历集合 获取到传感器的每一个名字
        for (Sensor sensor : list) {
            Log.i("杨路通", "initCustom: " + sensor.getName());
        }
        Log.i("杨路通", "initCustom: " + list.size());

        //最常用的一个方法 注册事件
        //参数1 sensorEventlistener监听器
        //参数2 sensor 一个服务可能有多个sensor实现 此处调用getdefaultsensor获取
        //默认的sensor
        //参数三 模式 可选数据变化的刷新频率 多少微秒取一次


    }

    @Override
    protected void onResume() {
        super.onResume();
        //加速传感器
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        //方向传感器
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        //光感传感器
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //初始化looper
                Looper.prepare();

                //在子线程创建Handler对象
                handler1 = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            String str;
                            str = (String) msg.obj;
                            Toast.makeText(MainActivity.this, "" + str, Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                //开启无限轮循，从消息队列中取出消息
                Looper.loop();
            }
        }.start();
    }

    private void initView() {
        mBt = (Button) findViewById(R.id.bt);
        mBt.setOnClickListener(this);
        mX = (Button) findViewById(R.id.x);
        mX.setOnClickListener(this);
        mY = (Button) findViewById(R.id.y);
        mY.setOnClickListener(this);
        mZ = (Button) findViewById(R.id.z);
        mZ.setOnClickListener(this);
        mXx = (Button) findViewById(R.id.Xx);
        mYy = (Button) findViewById(R.id.Yy);
        mZz = (Button) findViewById(R.id.Zz);
        mTvSun = (TextView) findViewById(R.id.tvSun);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.bt:
                //点击主线程向子线程发送消息
                Message obtain = Message.obtain();
                string = "Hi 我是主线程发送的消息";
                obtain.what = 1;
                obtain.obj = string;
                handler1.sendMessage(obtain);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.x:
                break;
            case R.id.y:
                break;
            case R.id.z:
                break;
        }
    }

    //监听回调发生改变的监听
    @Override
    public void onSensorChanged(SensorEvent event) {
        //SensorEvent 所有数据都是在这个对象中封装的
        //Accuracy//精度
        //sensor 传感器对象
        //timestamp 时间戳
        //values
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            //加速传感器的数据
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            mX.setText("X" + x + "");
            mY.setText("Y" + y + "");
            mZ.setText("Z" + z + "");

            //摇一摇
            if (Math.abs(x) > 20 || Math.abs(y) > 20 || Math.abs(z) > 20) {
                //触发摇一摇
                shake();
            }
        } else if (type == Sensor.TYPE_ORIENTATION) {
            //绕某个轴转过的角度
            float z = event.values[0];
            float x = event.values[1];
            float y = event.values[2];

            mXx.setText("绕x轴转过的角度" + x);
            mYy.setText("绕Y轴转过的角度" + y);
            mZz.setText("绕Z轴转过的角度" + z);

        } else if (type == Sensor.TYPE_LIGHT) {
            mTvSun.setText("光传感器"+event.values[0]);
        }

    }

    private void shake() {
        //音响+震动
        MediaPlayer player = MediaPlayer.create(this, R.raw.a);
        player.start();

        //震动
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(1500);

        //震动2
        //这里使用的是长整型数组，数组的a【0】表示静止的时间 a【1】 代表震动的时间
        //a【2】 表示静止的时间 a[3] 震动的时间 ....以此类推
        long[] patter = {1000, 1000, 1000, 50};
        vibrator.vibrate(patter, -1);

    }

    //数据发生改变的监听
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
