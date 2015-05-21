package com.example.androidsync;

import java.io.IOException;
import java.util.List;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String appKey = "7w6frrlar9nakh8";
	private static final String appSecret = "u59q61ac3z2sljj";

	private static final int REQUEST_LINK_TO_DBX = 0;

	    private TextView mTestOutput;
	    private Button mLinkButton;
	    private DbxAccountManager mDbxAcctMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
        setContentView(R.layout.activity_main);
        mLinkButton = (Button) findViewById(R.id.link_button);
        mTestOutput = (TextView) findViewById(R.id.test_output);
        mLinkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLinkToDropbox();
                Log.v("DropboxActivity","Inside onClickMain");
            
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                this.finish();
                return true;
            case R.id.unlink:
            	mDbxAcctMgr.unlink();
            	mLinkButton.setVisibility(View.VISIBLE);
                mTestOutput.setVisibility(View.GONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
	protected void onResume() {
		super.onResume();
		if (mDbxAcctMgr.hasLinkedAccount()) {
		    showLinkedView();
		    doDropboxTest();
		} else {
			showUnlinkedView();
		}
	}

    private void showLinkedView() {
        mLinkButton.setVisibility(View.GONE);
        mTestOutput.setVisibility(View.VISIBLE);
    }

    private void showUnlinkedView() {
        mLinkButton.setVisibility(View.VISIBLE);
        mTestOutput.setVisibility(View.GONE);
    }

    private void onClickLinkToDropbox() {
    	Log.v("DropboxActivity","Inside onClickView");
        mDbxAcctMgr.startLink((Activity)this, REQUEST_LINK_TO_DBX);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                doDropboxTest();
                Log.v("DropboxActivity","Inside onClickView 1");
            } else {
                mTestOutput.setText("Link to Dropbox failed or was cancelled.");
                Log.v("DropboxActivity","Inside onClickView 2");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Log.v("DropboxActivity","Inside onClickView 3");
        }
    }

    private void doDropboxTest() {
        mTestOutput.setText("Dropbox Sync API Version "+DbxAccountManager.SDK_VERSION_NAME+"\n");
        try {
            final String TEST_DATA = "Hello Dropbox";
            final String TEST_FILE_NAME = "hello_dropbox.txt";
            DbxPath testPath = new DbxPath(DbxPath.ROOT, TEST_FILE_NAME);

            // Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());

            // Print the contents of the root folder.  This will block until we can
            // sync metadata the first time.
            List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);
            mTestOutput.append("Linked to Dropbox");
            //mTestOutput.append("\nContents of app folder:\n");
            //for (DbxFileInfo info : infos) {
               // mTestOutput.append("    " + info.path + ", " + info.modifiedTime + '\n');
            //}

            // Create a test file only if it doesn't already exist.
            if (!dbxFs.exists(testPath)) {
                DbxFile testFile = dbxFs.create(testPath);
                try {
                    testFile.writeString(TEST_DATA);
                } finally {
                    testFile.close();
                }
                //mTestOutput.append("\nCreated new file '" + testPath + "'.\n");
            }

            // Read and print the contents of test file.  Since we're not making
            // any attempt to wait for the latest version, this may print an
            // older cached version.  Use getSyncStatus() and/or a listener to
            // check for a new version.
            if (dbxFs.isFile(testPath)) {
                String resultData;
                DbxFile testFile = dbxFs.open(testPath);
                try {
                    resultData = testFile.readString();
                } finally {
                    testFile.close();
                }
               // mTestOutput.append("\nRead file '" + testPath + "' and got data:\n    " + resultData);
            } else if (dbxFs.isFolder(testPath)) {
                //mTestOutput.append("'" + testPath.toString() + "' is a folder.\n");
            }
        } catch (IOException e) {
            //mTestOutput.setText("Dropbox test failed: " + e);
        }
    }



    
}
