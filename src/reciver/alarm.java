package reciver;


import com.exbadr.mosquetracker.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class alarm extends BroadcastReceiver{
	MediaPlayer mp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		switch (intent.getIntExtra("code", 0)) {
		case 0:
			Toast.makeText(context, "Subuh...!!!", Toast.LENGTH_LONG).show();
			break;
		case 1:
			Toast.makeText(context, "Dzuhur...!!!", Toast.LENGTH_LONG).show();
			break;
		case 2:
			Toast.makeText(context, "Ashr...!!!", Toast.LENGTH_LONG).show();
			break;
		case 3:
			Toast.makeText(context, "Maghrib...!!!", Toast.LENGTH_LONG).show();
			break;
		case 4:
			Toast.makeText(context, "Isya...!!!", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
		if(intent.getIntExtra("code", 0)==0){
			mp = new MediaPlayer();
			mp = MediaPlayer.create(context, R.raw.rashidmishary_fajr);
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					alarm.this.mp.release();
				}
			});
			mp.start();
		}else{
			mp = new MediaPlayer();
			mp = MediaPlayer.create(context, R.raw.makkah);
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					alarm.this.mp.release();
				}
			});
			mp.start();
		}
	}

}
