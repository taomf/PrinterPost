package cn.speedpay.s.xedj.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.api.ShowToast;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;

import cn.speedpay.s.xedj.FastFrame;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.bean.OrderInfoDetailsBean;
import cn.speedpay.s.xedj.presenter.PrintTicketPresenter;
import cn.speedpay.s.xedj.presenter.PrintTicketPresenterImp;
import cn.speedpay.s.xedj.printer.printer.PrinterClassSerialPort;
import cn.speedpay.s.xedj.utils.CodeUtil;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.view.PrintTicketView;
import cn.speedpay.s.xedj.zbar.CameraPreview;

public class CaptureActivity extends Activity implements PrintTicketView {

    PrinterClassSerialPort printerClass = null;
    public boolean printing = true;

    private static final float BEEP_VOLUME = 0.10f;
    public static final String TAG = "CaptureActivity";
    public static Camera mCamera;
    Parameters parameters;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private MediaPlayer mediaPlayer;
    private boolean playBeep = true;
    ImageScanner scanner;
    int time = 0;
    FrameLayout preview;

    private PrintTicketPresenter presenter;
    private String orderid = "";


    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.zbar_capture);
        preview = (FrameLayout) findViewById(R.id.cameraPreview);

        presenter = new PrintTicketPresenterImp(this);

    }

    // 相机参数的初始化设置
    private void initCamera() {
        parameters = mCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        // parameters.setPictureSize(surfaceView.getWidth(),
        // surfaceView.getHeight()); // 部分定制手机，无法正常识别该方法。
        parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);  //FLASH_MODE_TORCH 闪光灯
        parameters
                .setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
        setDispaly(parameters, mCamera);
        mCamera.setParameters(parameters);
        mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

        mCamera.startPreview();

    }

    // 控制图像的正确显示方向
    private void setDispaly(Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(0);
        }
    }

    // 实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod(
                    "setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            mCamera = getCameraInstance();

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            autoFocusHandler = new Handler();

			/* Instance barcode scanner */
            scanner = new ImageScanner();
            scanner.setConfig(0, Config.X_DENSITY, 3);
            scanner.setConfig(0, Config.Y_DENSITY, 3);
            preview.removeAllViews();

            mPreview = new CameraPreview(this, mCamera, new PreviewCallback());

            preview.addView(mPreview);
            initCamera();
            mCamera.startPreview();

            ////AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);

            initBeepSound();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            Log.i("", e.toString());
        }

    }

    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if(printerClass != null){
            printerClass.close(this);
        }
        super.onDestroy();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        return Camera.open(CameraInfo.CAMERA_FACING_BACK);
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static int[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        int[] d = new int[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (int) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    //扫描结果的回调
    private final class PreviewCallback implements
            Camera.PreviewCallback {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);
            int result = scanner.scanImage(barcode);
            if (result != 0) {
                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    if (getIntent().getData() != null) {

                        String codeid = sym.getData();
                        String orderid = CodeUtil.codeIdTurnOrderID(codeid);
                        Log.i("taomf++++", orderid);
                    } else {
                        /*
                         * Toast.makeText(getApplicationContext(),
						 * sym.getData(), 1000).show();
						 */
                        String resultString = sym.getData();

                        if (resultString.equals("")) {
                            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            if(printing){
                                printing = false;

                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("result", resultString);
                                resultIntent.putExtras(bundle);
                                byte[] recData = barcode.getData();

                                //订单号
                                String orderCode = CodeUtil.codeIdTurnOrderID(resultString);

                                Intent intent = getIntent();
                                if (intent.getFlags() == Constant.MIntent.TOCAPTUREACTIVITY) {
                                    intent.putExtra("code", orderCode);
                                    setResult(Constant.MIntent.CAPTUREACTIVITYRESULTCODE, intent);

                                    ShowToast.showToast(FastFrame.getApplication(), orderCode);
//                                    Log.i("CaptureActivity", orderCode);

                                    CaptureActivity.this.finish();
                                } else if (intent.getFlags() == Constant.MIntent.MAINACTIVITYFLG) {
                                    Log.i("CaptureActivity", orderCode);

                                        presenter.orderSearch(orderCode);

                                 /*   try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }*/
                            }
                            }

//							QrCodeActivity.bitmapRec=BitmapFactory.decodeByteArray(recData, 0, recData.length);
//                            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void orderSearch(String str) {

    }

    /**
     * 打印小票
     *
     * @param jsonObject
     */
    @Override
    public void printTicketDetail(Object jsonObject) {
        ShowToast.showToast(this,"扫描成功,正在打印!!!");
        try {
            JSONObject jsObject = (JSONObject) jsonObject;

            if (Integer.parseInt(jsObject.getString("printcnt")) > 1) { //当前打印次数

            }
            OrderInfoDetailsBean objBean = new Gson().fromJson(jsObject.toString(), OrderInfoDetailsBean.class);

            printerClass = new PrinterClassSerialPort(new Handler(),this);
            printerClass.open(this);

            String orderid = jsObject.getString("orderid");

            String barcode = CodeUtil.orderIdTurnCodeID(orderid);
//            Bitmap bitmap =  CodeUtil.CreateBarCode(barcode);  //条码图片资源


            if(TextUtils.equals(jsObject.getString("ordertype"),"闪购订单")){ //闪购订单
                printerClass.printTextFlash(objBean, CaptureActivity.this, barcode);
            }else {
                printerClass.printTextDemo(objBean, CaptureActivity.this, barcode);
            }

        } catch (Exception e) {
            Log.i("printTicketDetail",e.toString());
            e.printStackTrace();
        }
    }



}
