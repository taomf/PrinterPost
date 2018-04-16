package cn.speedpay.s.xedj.printer.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;


import com.google.zxing.WriterException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.activitys.CaptureActivity;
import cn.speedpay.s.xedj.bean.OrderInfoDetailsBean;
import cn.speedpay.s.xedj.printer.printerhelper.Device;
import cn.speedpay.s.xedj.printer.printerhelper.PrintService;
import cn.speedpay.s.xedj.printer.printerhelper.PrinterClass;
import cn.speedpay.s.xedj.utils.CodeUtil;

public class PrinterClassSerialPort implements PrinterClass {
    // 小票虚线
    public static final String SingleLine = "--------------------------------";
    public static final String title = "名称	  数量	   单价	    金额";

    private Context mContext;

//    String[] strs = {"草莓酸奶A布的", "酸奶水果补丁陶看见康师傅", "苹果"};

    private static final String TAG = "PrinterClassSerialPort";
    SerialPortHelper serialPort = null;
    PrintService printservice = new PrintService();
    private Handler mHandler;
    String device = "/dev/ttyMT0";
    public int baudrate = 115200;// 38400

    boolean iswrite = false;
    boolean canWrite = false;

    private Thread thread1;

    public enum States {
        BufferFull, NullBuffer, Printing, NoPaper, HightEmperature, LowPower
    }

    public States state = States.NullBuffer;
    private byte[] dataprint = {0x13};

    boolean isFull = false;

    boolean isNull = false;

    public PrinterClassSerialPort(Handler _mHandler, Context context) {
        this.mContext = context;
        mHandler = _mHandler;
        MsgHandler msgHandler = new MsgHandler(Looper.myLooper());
        serialPort = new SerialPortHelper();
        serialPort.mSerialPort
                .setOnserialportDataReceived(new SerialPortDataReceived() { // 监听事件
                    @Override
                    public void onDataReceivedListener(byte[] buffer, int size) {
                        dataprint = buffer;
                        isFull = buffer[0] == 0x13;
                        isNull = buffer[0] == 0x11;

                        getStates(buffer);

						/*
                         * int size = msg.arg1; String dataStr =
						 * msg.obj.toString(); byte[] buffer =
						 * dataStr.getBytes();
						 */
                        Log.i(TAG,
                                "get printer recive data:"
                                        + byteToString(buffer, size));
                        if (size > 0) {
                            if (iswrite) {
                                if (buffer[0] == 0) {
                                    canWrite = true;
                                } else {
                                    canWrite = false;
                                    serialPort.Write(new byte[]{0x0a});
                                }
                                iswrite = false;
                            }
                            if (buffer[0] == 0x13) { // 缓存满了
                                PrintService.isFUll = true;
                                Log.i(TAG, "0x13:");
                            } else if (buffer[0] == 0x11) { // 缓存为空
                                PrintService.isFUll = false;
                                Log.i(TAG, "0x11:");
                            } else if (PrintService.getState) { // 正在打印 或者空闲状态
                                PrintService.getState = false;
                                PrintService.printState = buffer[0];
                            }
                            mHandler.obtainMessage(PrinterClass.MESSAGE_READ,
                                    size, -1, buffer).sendToTarget();
                        }
                    }
                });
        serialPort.OpenSerialPort(device, baudrate);
    }

    private class MsgHandler extends Handler {
        public MsgHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }

    ;

    /**
     * 判断打印机在一定时间内是否空闲
     *
     * @param sleepTime
     * @return
     */
    private boolean getBufferState(final int sleepTime) {
        // TODO Auto-generated method stub
        PrintService.getState = true;
        for (int i = 0; i < sleepTime / 10; i++) {
            serialPort.Write(new byte[]{0x1b, 0x76});
            if (PrintService.printState == 0) {
                PrintService.getState = false;
                return true;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PrintService.getState = false;
        return false;
    }

    public boolean setSerialPortBaudrate(int _baudrate) {
        if (serialPort.CloseSerialPort()) {
            if (serialPort.OpenSerialPort(device, baudrate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean open(Context context) {
        if (serialPort.OpenSerialPort(device, baudrate)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean close(Context context) {

        return serialPort.CloseSerialPort();
    }

    @Override
    public void scan() {

    }

    @Override
    public List<Device> getDeviceList() {
        return null;
    }

    @Override
    public void stopScan() {

    }

    @Override
    public boolean connect(String device) {
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public boolean IsOpen() {
        return false;
    }

    @Override
    public boolean write(byte[] buffer) {
        if (getBufferState(10000)) {
            return serialPort.Write(buffer);
        }
        return false;
        // //return serialPort.Write(buffer);
    }

    @Override
    public boolean printText(String textStr) {

        // 打印当前存储内容,并进几行
        write(CMD_PRINT_LOCATION);
        write(CMD_INIT_PRINTER);
        byte[] buffer = printservice.getText(textStr);
        return write(buffer);
    }

    @Override
    public boolean printImage(Bitmap bitmap) {
        return write(printservice.getImage(bitmap));
    }

    @Override
    public boolean printUnicode(String textStr) {
        return write(printservice.getTextUnicode(textStr));
    }

    public static String byteToString(byte[] b, int size) {
        byte high, low;
        byte maskHigh = (byte) 0xf0;
        byte maskLow = 0x0f;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < size; i++) {
            high = (byte) ((b[i] & maskHigh) >> 4);
            low = (byte) (b[i] & maskLow);
            buf.append(findHex(high));
            buf.append(findHex(low));
            buf.append(" ");
        }
        return buf.toString();
    }

    private static char findHex(byte b) {
        int t = new Byte(b).intValue();
        t = t < 0 ? t + 16 : t;
        if ((0 <= t) && (t <= 9)) {
            return (char) (t + '0');
        }
        return (char) (t - 10 + 'A');
    }

    CaptureActivity mActivity;

    public boolean printTextDemo(final OrderInfoDetailsBean bean, final Context context, String barcode) {
        try {
            bitmap = CodeUtil.createQrBitmap(barcode, 1000, 800);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (context instanceof CaptureActivity) {
            mActivity = (CaptureActivity) context;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 3; i++) {
                        serialPort.Write(CMD_PRINT_CURRENT_CONTEXT);
                        serialPort.Write(CMD_INIT_PRINTER);
                        Log.i("wjf", "init =" + i);
                        // 初始化打印机
                        serialPort.Write(CMD_NEWLINE);
                        write(printservice.getImage(compressImage(getI(i, context))));
                        serialPort.Write(CMD_NEWLINE);

                        if(i != 0){
                            Thread.sleep(100);
                            write(printservice.getImage(compressImage(bitmap)));
                            serialPort.Write(CMD_NEWLINE);
                        }

                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));

                        serialPort.Write(PrinterClass.CMD_ALIGN_LEFT);
                        write(printservice.getText("单号: " + bean.getOrderid()
                                + "\n"));
                        Thread.sleep(50);
                        write(printservice.getText("日期: " + bean.getOrdertime() + "\n"));
                        Thread.sleep(50);
                        if (i == 0) {
                            write(printservice.getText("姓名: " + bean.getName() + "\n"));
                            Thread.sleep(50);
                            write(printservice.getText("地址: " + bean.getAddress() + "\n"));
                            Thread.sleep(50);
                        }
                        write(printservice.getText("电话: " + bean.getPhone() + "\n"));
                        Thread.sleep(50);
                        write(printservice.getText("支付方式: " + bean.getOrderpaychannel() + "\n"));
                        Thread.sleep(50);

                        // 打印当前存储内容,并进几行
                        serialPort.Write(CMD_PRINT_LOCATION);
                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));

                        Thread.sleep(50);
                        printZhiBiaoText(bean.getGoodsinfo(), context);

                        serialPort.Write(CMD_PRINT_LOCATION);

                        serialPort.Write(CMD_NEWLINE);

                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));

                        // write(CMD_ALIGN_RIGHT);
                        String str = "                                    ";
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 1) + " 商品金额:" + bean.getOrdergoodsallprice() + "\n"));  //1
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 6) + "运费:" + bean.getYfallprice() + "\n"));  //6
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText("优惠券减免:" + bean.getYhprice() + "\n"));
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 2) + "营销减免:" + bean.getYxjmprice() + "\n"));  //2
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 2) + "营收总额:" + bean.getOrderpayprice() + "\n"));  //2
                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));

                        // 打印当前存储内容,并进几行
                        serialPort.Write(CMD_PRINT_LOCATION);

                        if (i == 0) {
                            // 左对齐
                            serialPort.Write(CMD_ALIGN_LEFT);
                            write(printservice
                                    .getText("请当面点清所购商品和找零并保存好此清单,可作为退还凭证"
                                            + "\n"));

                            Thread.sleep(50);
                            write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                    R.drawable.xuxian)));

                            // 打印当前存储内容,并进几行
                            serialPort.Write(CMD_PRINT_LOCATION);

                            // 打印当前存储内容
                            serialPort.Write(CMD_PRINT_CURRENT_CONTEXT);
                            Thread.sleep(1500);
                            write(printservice.getImage(compressImage(BitmapFactory
                                    .decodeResource(context.getResources(),
                                            R.drawable.print_code))));
                        }

                        if (i == 1) {
                            Thread.sleep(50);
                            write(printservice.getText("客户评价" + "\n\n"));
                            Thread.sleep(50);
                            write(printservice.getText("  □满意    □一般   □不满意"
                                    + "\n"));
                            Thread.sleep(50);
                            write(printservice.getText("客户签字" + "\n\n\n"));
                            Thread.sleep(50);

                        }
                        Log.i("wjf", "init =" + i);
                        serialPort.Write(CMD_PRINT_CURRENT_CONTEXT);
                        if (i == 0) {
                            Thread.sleep(1000);
                        } else {
                            Thread.sleep(2000);
                        }
                    }
                    // 打印当前存储内容
//						getTimeStates();
                } catch (Exception e) {
                    Log.i("wjf",e.toString());
                    e.printStackTrace();
                }

                serialPort.Write(CMD_PRINT_LOCATION);
                if (mActivity != null) {
                    mActivity.printing = true;
                }

            }
        }).start();

        return true;
    }

    // 制表打印
    public boolean printZhiBiaoText(List<OrderInfoDetailsBean.GoodsinfoBean> list, Context context) {

        // TODO Auto-generated method stub
        // 初始化打印机
        // 左对齐
        serialPort.Write(CMD_ALIGN_LEFT);
        write(printservice.getText("名称	  数量	   单价	    金额" + "\n"));

        try {
            Thread.sleep(50);
            write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.xuxian)));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).getGoodsname();

            int length = str.length();
            int count = (length - 1) / 5;

            switch (count) {
                case 0: // 长度小于5
                    int len = str.length();
                    String kongge = "     ";
                    // (len==5?"":kongge.substring(0, 5-len))
                    write(printservice.getText(str));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText((len == 5 ? "" : kongge.substring(0,
                            6 - len)) + "4"));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText("12.00"));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText("48.00" + "\n"));
                    break;
                case 1: // 6-10
                    String s1 = str.substring(0, 5);
                    String s2 = str.substring(5, str.length());

                    write(printservice.getText(s1));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText(list.get(i).getGoodscnt() + ""));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText(list.get(i).getZkgoodsprice() + ""));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText(list.get(i).getGoodscnt() * list.get(i).getZkgoodsprice() + "\n"));

                    write(printservice.getText(s2 + "\n"));

                    break;

                default: // 11以上
                    String s3 = str.substring(0, 5);
                    String s4 = str.substring(5, 10);
                    String s5 = "";
                    if (length > 15) {
                        s5 = str.substring(10, 14) + "..";
                    } else {
                        s5 = str.substring(10, str.length());
                    }

                    write(printservice.getText(s3));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText(list.get(i).getGoodscnt() + ""));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText(list.get(i).getZkgoodsprice() + ""));
                    serialPort.Write(CMD_HORIZONTAL_TAB);
                    write(printservice.getText(list.get(i).getGoodscnt() * list.get(i).getZkgoodsprice() + "\n"));
                    write(printservice.getText(s4 + "\n"));
                    write(printservice.getText(s5 + "\n"));

                    break;
            }
        }
        return true;
    }

    // 获取图片
    private Bitmap getI(int i, Context context) {
        Bitmap bitmap = null;
        switch (i) {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.print_customer_logo);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.print_express_logo);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.print_retention_logo);
                break;
        }
        return bitmap;
    }

    /**
     * 文字转图片 单行
     *
     * @param str
     * @return
     */
    public Bitmap text2pic(String str) {
        Bitmap btMap = Bitmap.createBitmap(384, 25, Config.ARGB_8888);
        Canvas canvas = new Canvas(btMap);
        canvas.drawColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25.0F);
        StaticLayout layout = new StaticLayout(str, textPaint,
                btMap.getWidth(), Alignment.ALIGN_NORMAL, (float) 1.0,
                (float) 0.0, true);

        layout.draw(canvas);
        return btMap;
    }

    // 图片压缩
    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public void getStates(byte[] by) {
        try {

            if (by[0] == 0x13) { // 缓存满了
                state = States.BufferFull;
//				   synchronized("b") {
//					   "b".wait();
//	                }
            } else if (by[0] == 0x11) { // 缓存为空
                state = States.NullBuffer;
//				synchronized("a") {
//					"a".notify();
//				}
            } else if (by[0] == 0x08) { // 没纸了
                state = States.NoPaper;

            } else if (by[0] == 0x01) { // 正在打印
                state = States.Printing;

            } else if (by[0] == 0x04) { // 高温
                state = States.HightEmperature;

            } else if (by[0] == 0x02) { // 低压
                state = States.LowPower;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 获取临时状态
     */
    public void getTimeStates() {
        // TODO Auto-generated method stub
        if (state != States.NullBuffer) {
            try {
                Thread.sleep(50);
                serialPort.Write(CMD_POST_STATE); // 获取打印机状态
                getTimeStates();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

     Bitmap bitmap = null;
    public boolean printTextFlash(final OrderInfoDetailsBean bean, final Context context, String barcode) {

        try {
            bitmap = CodeUtil.createQrBitmap(barcode, 1000, 800);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (context instanceof CaptureActivity) {
            mActivity = (CaptureActivity) context;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 2; i++) {
                        serialPort.Write(CMD_PRINT_CURRENT_CONTEXT);
                        serialPort.Write(CMD_INIT_PRINTER);
                        Log.i("wjf", "init =" + i);
                        // 初始化打印机
                        serialPort.Write(CMD_NEWLINE);
                        if (i == 0) {
                            write(printservice.getImage(compressImage(getI(0, context))));
                        } else {
                            write(printservice.getImage(compressImage(getI(2, context))));
                        }

                        Thread.sleep(100);
                        write(printservice.getImage(compressImage(bitmap)));

                        serialPort.Write(CMD_NEWLINE);

                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));

                        serialPort.Write(PrinterClass.CMD_ALIGN_LEFT);
                        write(printservice.getText("单号: " + bean.getOrderid()
                                + "\n"));
                        Thread.sleep(50);
                        write(printservice.getText("日期: " + bean.getOrdertime() + "\n"));

                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));
                        Thread.sleep(50);

                        write(printservice.getText("姓名: " + bean.getName() + "\n"));
                        Thread.sleep(50);
                        write(printservice.getText("电话: " + bean.getPhone() + "\n"));
                        Thread.sleep(50);
                        write(printservice.getText("支付方式: " + bean.getOrderpaychannel() + "\n"));

                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));
                        Thread.sleep(50);


                        // 打印当前存储内容,并进几行
                        serialPort.Write(CMD_PRINT_LOCATION);
                        Thread.sleep(50);
                        printZhiBiaoText(bean.getGoodsinfo(), context);

                        serialPort.Write(CMD_PRINT_LOCATION);

                        serialPort.Write(CMD_NEWLINE);
                        // write(CMD_ALIGN_RIGHT);
                        String str = "                                    ";
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 1) + " 商品金额:" + bean.getOrdergoodsallprice() + "\n"));  //1
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 6) + "运费:" + bean.getYfallprice() + "\n"));  //6
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText("优惠券减免:" + bean.getYhprice() + "\n"));
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 2) + "营销减免:" + bean.getYxjmprice() + "\n"));  //2
                        Thread.sleep(50);
                        serialPort.Write("               ");
                        write(printservice.getText(str.substring(0, 2) + "营收总额:" + bean.getOrderpayprice() + "\n"));  //2

                        Thread.sleep(50);
                        write(printservice.getImage(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.xuxian)));

                        Thread.sleep(50);

                        // 打印当前存储内容,并进几行
                        serialPort.Write(CMD_PRINT_LOCATION);

                        Thread.sleep(1800);
                    }
                    // 打印当前存储内容
//						getTimeStates();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                serialPort.Write(CMD_PRINT_LOCATION);
                if (mActivity != null) {
                    mActivity.printing = true;
                }
            }
        }).start();

        return true;
    }
}
