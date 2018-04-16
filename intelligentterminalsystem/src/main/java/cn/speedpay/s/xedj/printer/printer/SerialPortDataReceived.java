package cn.speedpay.s.xedj.printer.printer;

public interface SerialPortDataReceived {
	public void onDataReceivedListener(final byte[] buffer, final int size); 

}
