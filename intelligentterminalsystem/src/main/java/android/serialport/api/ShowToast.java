package android.serialport.api;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {
	public static Toast toast = null;
	
	public static void showToast(Context context,String string){
		if(toast == null){
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		toast.setText(string);
		toast.show();
	}

}
