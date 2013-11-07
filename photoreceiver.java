package com.example.workinground2;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;


public class photoReceiver extends BroadcastReceiver {
	private static final String TAG = "photoReceiver";
	 int duration = Toast.LENGTH_LONG;
	 //private static final String appKey = "p78hpiyefxd4p9z";
	    //private static final String appSecret = "hwjzoa1qoxjoqft";
	    //private DbxAccountManager mDbxAcctMgr;
	@Override
	public void onReceive(Context context, Intent intent) {
		 Log.v(TAG, "In photoReceiver");
		// TODO Auto-generated method stub
		 Cursor cursor = context.getContentResolver().query(intent.getData(),      null,null, null, null);
		    cursor.moveToFirst();
		    String image_path = cursor.getString(cursor.getColumnIndex("_data"));
		    Toast.makeText(context, "New Photo is Saved as : -" + image_path, duration).show();
		
	    Log.v(TAG, "Received new photo");
	    //Sending to drop box
	    //mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
	    //DbxPath testPath = new DbxPath(DbxPath.ROOT,image_path);

        // Create DbxFileSystem for synchronized file access.
        //DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());


	   
		
	}
	

}
