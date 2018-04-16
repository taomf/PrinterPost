package cn.speedpay.s.xedj.printer.printer;

import android.util.Log;

import android.serialport.api.SerialPort;

public class SerialPortHelper {

	protected static final String TAG = "SerialPortHelper";
	public SerialPort mSerialPort;
	

	public SerialPortHelper() {
		mSerialPort = new SerialPort();
	}

	

	public Boolean OpenSerialPort(String device, int baudrate) {

		try {
			mSerialPort.open(device, baudrate);
		} catch (SecurityException e) {
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public Boolean Write(String str) {
		byte[] buffer = str.getBytes();
		return Write(buffer);
	}

	public Boolean Write(byte[] buffer) {

		int sendSize = 500;
		if (buffer.length <= sendSize) {
			mSerialPort.Write(buffer);
			return true;
		}
		for (int j = 0; j < buffer.length; j += sendSize) {

			byte[] btPackage = new byte[sendSize];
			if (buffer.length - j < sendSize) {
				btPackage = new byte[buffer.length - j];
			}
			System.arraycopy(buffer, j, btPackage, 0, btPackage.length);
			mSerialPort.Write(btPackage);
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}

	public Boolean CloseSerialPort() {
		if (mSerialPort == null) {
			return true;
		}
		return mSerialPort.closePort();
	}

	public static String byte2HexStr(byte[] b, int lenth) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < lenth; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			sb.append(" ");
		}
		return sb.toString().toUpperCase().trim();
	}

}
