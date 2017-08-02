package ave.ladhc.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;

/**
 * Created by Lenovo on 01/08/2017.
 */

public class LlamadEntrante extends BroadcastReceiver{
       String numero;
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String numeroentrante = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            numero=numeroentrante;
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("MI_ESPECIFICA_ACTION");
            broadcastIntent.putExtra("numero", numero);
            context.sendBroadcast(broadcastIntent);






        }
        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE) || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            //Toast.makeText(context, "LLamada detenida",Toast.LENGTH_LONG).show();

        }


    }




   /* private void sendBroadCast(Context context) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("android.intent.action.PHONE_STATE");
        broadcastIntent.putExtra("numero", numero);
        context.sendBroadcast(broadcastIntent);
    }*/



}
