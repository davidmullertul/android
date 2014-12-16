package pma.muller.pexeso.v01;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

public class MenicPozadi extends Service{

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		Bundle b = intent.getExtras();
		
	    return Service.START_NOT_STICKY;
	  }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
