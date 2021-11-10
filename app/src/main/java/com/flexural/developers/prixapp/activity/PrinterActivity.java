package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.AirtimeAdapter;
import com.flexural.developers.prixapp.model.Airtime;
import com.flexural.developers.prixapp.utils.BluetoothUtil;
import com.flexural.developers.prixapp.utils.ButtonDelayUtils;
import com.flexural.developers.prixapp.utils.ESCUtil;
import com.flexural.developers.prixapp.utils.HandlerUtils;
import com.flexural.developers.prixapp.utils.ThreadPoolManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class PrinterActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String prod_id, pin_no, serial_no;

    private RecyclerView mAirtimeRecycler;
    private List<Airtime> airtimeList;
    private AirtimeAdapter airtimeAdapter;
    private TextView mAirtimeAmount;

    private String URL =  BASE_URL + "airtimeList.php";

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothPrinterDevice = null;
    private BluetoothSocket socket = null;

    private Button btn_printer_test,btn_leftMargin_test;
    private Button btn_exit;
    private Button btn_load_bluetoothPrinter,btn_printer_init;
    /*定义打印机状态*/
    private final int PRINTER_NORMAL = 0;
    private final int PRINTER_ERROR_UNKNOWN = 5;
    /*打印机当前状态*/
    private int printerStatus = PRINTER_ERROR_UNKNOWN;

    /*定义状态广播*/
    private final String  PRINTER_NORMAL_ACTION = "com.iposprinter.iposprinterservice.NORMAL_ACTION";
    private final String  PRINTER_PAPERLESS_ACTION = "com.iposprinter.iposprinterservice.PAPERLESS_ACTION";
    private final String  PRINTER_PAPEREXISTS_ACTION = "com.iposprinter.iposprinterservice.PAPEREXISTS_ACTION";
    private final String  PRINTER_THP_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_HIGHTEMP_ACTION";
    private final String  PRINTER_THP_NORMALTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_NORMALTEMP_ACTION";
    private final String  PRINTER_MOTOR_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.MOTOR_HIGHTEMP_ACTION";
    private final String  PRINTER_BUSY_ACTION = "com.iposprinter.iposprinterservice.BUSY_ACTION";
    private final String  PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION = "com.iposprinter.iposprinterservice.CURRENT_TASK_PRINT_COMPLETE_ACTION";

    /*定义消息*/
    private final int MSG_TEST                               = 1;
    private final int MSG_IS_NORMAL                          = 2;
    private final int MSG_IS_BUSY                            = 3;
    private final int MSG_PAPER_LESS                         = 4;
    private final int MSG_PAPER_EXISTS                       = 5;
    private final int MSG_THP_HIGH_TEMP                      = 6;
    private final int MSG_THP_TEMP_NORMAL                    = 7;
    private final int MSG_MOTOR_HIGH_TEMP                    = 8;
    private final int MSG_MOTOR_HIGH_TEMP_INIT_PRINTER       = 9;
    private final int MSG_CURRENT_TASK_PRINT_COMPLETE     = 10;

    /*循环打印类型*/
    private final int  MULTI_THREAD_LOOP_PRINT  = 1;
    private final int  DEFAULT_LOOP_PRINT       = 0;

    //循环打印标志位
    private       int  loopPrintFlag            = DEFAULT_LOOP_PRINT;

    private boolean isBluetoothOpen = false;
    private Random random = new Random();
    private HandlerUtils.PrinterHandler mPrinterHandler;

    /**
     * 消息处理
     */
    private HandlerUtils.IHandlerIntent mHandlerIntent = new HandlerUtils.IHandlerIntent() {
        @Override
        public void handlerIntent(Message msg)
        {
            switch (msg.what)
            {
                case MSG_TEST:
                    break;
                case MSG_IS_NORMAL:
                    if (getPrinterStatus() == PRINTER_NORMAL) {
                        print_loop(loopPrintFlag);
                    }
                    break;
                case MSG_IS_BUSY:
                    Toast.makeText(PrinterActivity.this, R.string.printer_is_working, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_LESS:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(PrinterActivity.this, R.string.out_of_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_EXISTS:
                    Toast.makeText(PrinterActivity.this, R.string.exists_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_THP_HIGH_TEMP:
                    Toast.makeText(PrinterActivity.this, R.string.printer_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_MOTOR_HIGH_TEMP:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(PrinterActivity.this, R.string.motor_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    mPrinterHandler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP_INIT_PRINTER, 180000);  //马达高温报警，等待3分钟后复位打印机
                    break;
                case MSG_MOTOR_HIGH_TEMP_INIT_PRINTER:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    printerInit();
                    break;
                case MSG_CURRENT_TASK_PRINT_COMPLETE:
                    Toast.makeText(PrinterActivity.this, R.string.printer_current_task_print_complete, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver IPosPrinterStatusListener = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            if(action == null)
            {
                Log.d(TAG,"IPosPrinterStatusListener onReceive action = null");
                return;
            }
            // Log.d(TAG,"IPosPrinterStatusListener action = "+action);
            if(action.equals(PRINTER_NORMAL_ACTION))
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_IS_NORMAL,0);
            }
            else if (action.equals(PRINTER_PAPERLESS_ACTION))
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_PAPER_LESS,0);
            }
            else if (action.equals(PRINTER_BUSY_ACTION))
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_IS_BUSY,0);
            }
            else if (action.equals(PRINTER_PAPEREXISTS_ACTION))
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_PAPER_EXISTS,0);
            }
            else if (action.equals(PRINTER_THP_HIGHTEMP_ACTION))
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_THP_HIGH_TEMP,0);
            }
            else if (action.equals(PRINTER_THP_NORMALTEMP_ACTION))
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_THP_TEMP_NORMAL,0);
            }
            else if (action.equals(PRINTER_MOTOR_HIGHTEMP_ACTION))  //此时当前任务会继续打印，完成当前任务后，请等待2分钟以上时间，继续下一个打印任务
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP,0);
            }
            else if(action.equals(PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION))
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_CURRENT_TASK_PRINT_COMPLETE,0);
            }
            else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
            {
                int state= intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("aaa", "STATE_OFF 蓝牙关闭");
                        isBluetoothOpen = false;
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("aaa", "STATE_TURNING_OFF 蓝牙正在关闭");
                        isBluetoothOpen = false;
                        if(mBluetoothAdapter != null)
                            mBluetoothAdapter = null;
                        if(mBluetoothPrinterDevice != null)
                            mBluetoothPrinterDevice = null;
                        try {
                            if (socket != null && (socket.isConnected())) {
                                socket.close();
                                socket = null;
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d("aaa", "STATE_ON 蓝牙开启");
                        loopPrintFlag = DEFAULT_LOOP_PRINT;
                        isBluetoothOpen = true;
                        LoadBluetoothPrinter();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        isBluetoothOpen = true;
                        Log.d("aaa", "STATE_TURNING_ON 蓝牙正在开启");
                        break;
                }
            }
            else
            {
                mPrinterHandler.sendEmptyMessageDelayed(MSG_TEST,0);
            }
        }
    };

    private void setButtonEnable(boolean flag){
        btn_load_bluetoothPrinter.setEnabled(flag);
        btn_printer_init.setEnabled(flag);
        btn_printer_test.setEnabled(flag);
        btn_leftMargin_test.setEnabled(flag);
        btn_exit.setEnabled(flag);
    }

    private void innitView() {
        btn_load_bluetoothPrinter = findViewById(R.id.btn_load_bluetoothPrinter);
        btn_printer_init = findViewById(R.id.btn_printer_init);
        btn_printer_test = findViewById(R.id.btn_printer_test);
        btn_leftMargin_test = findViewById(R.id.btn_leftMargin_test);
        btn_exit = findViewById(R.id.btn_exit);

        btn_load_bluetoothPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopPrintFlag = DEFAULT_LOOP_PRINT;
                LoadBluetoothPrinter();
            }
        });
        btn_printer_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopPrintFlag = DEFAULT_LOOP_PRINT;
                if(getPrinterStatus() == PRINTER_NORMAL)
                    printerInit();
            }
        });
        btn_leftMargin_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopPrintFlag = DEFAULT_LOOP_PRINT;
                finish();
            }
        });
        setButtonEnable(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

        mPrinterHandler = new HandlerUtils.PrinterHandler(mHandlerIntent);

        mAirtimeRecycler = findViewById(R.id.recycler_view);
        mAirtimeAmount = findViewById(R.id.airtime_amount);

        mAirtimeRecycler.setLayoutManager(new LinearLayoutManager(this));

        airtimeList = new ArrayList<>();

        innitView();
        receiveIntent();

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String selectedAirtime = intent.getStringExtra("airtime");
        mAirtimeAmount.setText("R" + selectedAirtime);
        getAirtime(selectedAirtime);

    }

    private void getAirtime(String selectedAirtime) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        prod_id = object.getString("prod_id");
                        pin_no = object.getString("pin_no");
                        serial_no = object.getString("serial_no");
                        String status = object.getString("status");
                        String expired_date = object.getString("expired_date");

                        if (prod_id.equals(selectedAirtime) && status.equals("Available")) {
                            Airtime airtime = new Airtime(prod_id, pin_no, serial_no, status, expired_date);
                            airtimeList.add(airtime);

                            btn_printer_test.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(getPrinterStatus() == PRINTER_NORMAL)
                                        bluetoothPrinterTest(prod_id, pin_no, serial_no);
                                }
                            });

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                airtimeAdapter = new AirtimeAdapter(PrinterActivity.this, airtimeList);
                mAirtimeRecycler.setAdapter(airtimeAdapter);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PrinterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(PrinterActivity.this).add(stringRequest);
    }


    @Override
    protected void onResume()
    {
        // Log.d(TAG, "activity onResume");
        super.onResume();
        //注册打印机状态接收器
        IntentFilter printerStatusFilter = new IntentFilter();
        printerStatusFilter.addAction(PRINTER_NORMAL_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPERLESS_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPEREXISTS_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_NORMALTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_MOTOR_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_BUSY_ACTION);
        printerStatusFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(IPosPrinterStatusListener,printerStatusFilter);

        loopPrintFlag = DEFAULT_LOOP_PRINT;
        LoadBluetoothPrinter();
        if(getPrinterStatus() == PRINTER_NORMAL) {
            printerInit();

        }
    }

    @Override
    protected void onPause()
    {
        // Log.d(TAG, "activity onPause");
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        // Log.e(TAG, "activity onStop");
        super.onStop();
        unregisterReceiver(IPosPrinterStatusListener);
        loopPrintFlag = DEFAULT_LOOP_PRINT;
    }

    @Override
    protected void onDestroy()
    {
        // Log.d(TAG, "activity onDestroy");
        super.onDestroy();
        mPrinterHandler.removeCallbacksAndMessages(null);
        if(mBluetoothAdapter != null)
            mBluetoothAdapter = null;
        if(mBluetoothPrinterDevice != null)
            mBluetoothPrinterDevice = null;
        try {
            if (socket != null && (socket.isConnected())) {
                socket.close();
                socket = null;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void print_loop(int flag)
    {
        switch (flag)
        {
            case MULTI_THREAD_LOOP_PRINT:
                multiThreadPrintTest();
                break;
            default:
                break;
        }
    }

    public void multiThreadPrintTest()
    {
        switch (random.nextInt(16))
        {
            case 0:
                bluetoothPrinterTest(prod_id, pin_no, serial_no);
                break;
            default:
                break;
        }
    }

    public void LoadBluetoothPrinter()
    {
        // 1: Get BluetoothAdapter
        mBluetoothAdapter = BluetoothUtil.getBluetoothAdapter();
        if(mBluetoothAdapter == null)
        {
            Toast.makeText(getBaseContext(), R.string.get_BluetoothAdapter_fail, Toast.LENGTH_LONG).show();
            isBluetoothOpen = false;
            return;
        }
        else
        {
            isBluetoothOpen =true;
        }
        //2: Get bluetoothPrinter Devices
        mBluetoothPrinterDevice = BluetoothUtil.getIposPrinterDevice(mBluetoothAdapter);
        if(mBluetoothPrinterDevice == null)
        {
            Toast.makeText(getBaseContext(), R.string.get_BluetoothPrinterDevice_fail, Toast.LENGTH_LONG).show();
            return;
        }
        //3: Get connect Socket
        try {
            socket = BluetoothUtil.getSocket(mBluetoothPrinterDevice);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        Toast.makeText(getBaseContext(), R.string.get_BluetoothPrinterDevice_success, Toast.LENGTH_LONG).show();
    }

    public int getPrinterStatus()
    {
        byte[] statusData = new byte[3];
        if(!isBluetoothOpen)
        {
            printerStatus = PRINTER_ERROR_UNKNOWN;
            return printerStatus;
        }
        if((socket == null) || (!socket.isConnected()))
        {
            try {
                socket = BluetoothUtil.getSocket(mBluetoothPrinterDevice);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return printerStatus;
            }
        }
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            byte[] data = ESCUtil.getPrinterStatus();
            out.write(data,0,data.length);
            int readsize = in.read(statusData);
            Log.d(TAG,"~~~ readsize:"+readsize+" statusData:"+statusData[0]+" "+statusData[1]+" "+statusData[2]);
            if((readsize > 0) &&(statusData[0] == ESCUtil.ACK && statusData[1] == 0x11)) {
                printerStatus = statusData[2];
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return printerStatus;
    }

    private void printerInit()
    {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if((socket == null) || (!socket.isConnected()))
                    {
                        socket = BluetoothUtil.getSocket(mBluetoothPrinterDevice);
                    }
                    //Log.d(TAG,"=====printerInit======");
                    OutputStream out = socket.getOutputStream();
                    byte[] data = ESCUtil.init_printer();
                    out.write(data,0,data.length);
                    out.close();
                    socket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void bluetoothPrinterTest(String ProdId, String PinNumber, String SerialNumber)
    {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try{
                    byte[] printer_init = ESCUtil.init_printer();
                    byte[] fontSize0 = ESCUtil.fontSizeSet((byte) 0x00);
                    byte[] fontSize1 = ESCUtil.fontSizeSet((byte) 0x01);
                    byte[] fontSize2 = ESCUtil.fontSizeSet((byte) 0x10);
                    byte[] fontSize3 = ESCUtil.fontSizeSet((byte) 0x11);
                    byte[] lineH0 = ESCUtil.setLineHeight((byte)26);
                    byte[] lineH1 = ESCUtil.setLineHeight((byte)50);

                    byte[] align0 = ESCUtil.alignMode((byte)0);
                    byte[] align1 = ESCUtil.alignMode((byte)1);
                    byte[] align2 = ESCUtil.alignMode((byte)2);
                    byte[] title1 = "PRIX\n".getBytes("GBK");
                    byte[] title2 = "To recharge: *136*7467*PIN#\n".getBytes("GBK");
                    byte[] fontTest1 = "PIN".getBytes("GBK");
                    byte[] fontTest2 = PinNumber.getBytes("GBK");
                    byte[] fontTest3 = "OR Scan the barcode below".getBytes("GBK");
                    byte[] orderSerinum = "Date: 10/11/21 Wednesday 09:14".getBytes("GBK");
                    byte[] testSign = "S/N: ".getBytes("GBK");
                    byte[] testInfo = SerialNumber.getBytes("GBK");
                    byte[] nextLine = ESCUtil.nextLines(1);
                    byte[] performPrint = ESCUtil.performPrintAndFeedPaper((byte)200);

                    byte[][] cmdBytes = {printer_init,lineH0,fontSize3,align1,title1,fontSize1,title2,nextLine,align0,
                            fontSize0,fontSize0,lineH1,fontSize1,fontTest1,lineH0,fontSize2,fontTest2,
                            lineH1,fontSize3,fontTest3,align2,lineH0,fontSize0,orderSerinum,testSign,
                            align1,fontSize1,lineH1,testInfo,nextLine,performPrint};
                    try {
                        if((socket == null) || (!socket.isConnected()))
                        {
                            socket = BluetoothUtil.getSocket(mBluetoothPrinterDevice);
                        }
                        byte[] data = ESCUtil.byteMerger(cmdBytes);
                        OutputStream out = socket.getOutputStream();
                        out.write(data,0,data.length);
                        out.close();
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }


}