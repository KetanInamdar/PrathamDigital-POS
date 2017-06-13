package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.example.pef.prathamopenschool.splashScreenVideo.fpath;

public class MultiPhotoSelectActivity extends AppCompatActivity {

	private ImageAdapter imageAdapter;
	JSONArray studentList[]={}, configJsonArray;
	private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
	static String programID;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_multi_photo_select);
		displayStudents();
	}

	public void displayStudents(){
		String assignedGroupIDs[]={},assignedGroupNames[]={};

		try {
			programID = getProgramId();

			if(programID.equals("0")){
				Toast.makeText(this,"Invalid Program Id",Toast.LENGTH_SHORT);
			}
			else {
				StatusDBHelper statusDBHelper = new StatusDBHelper(this);
				StudentDBHelper studentDBHelper = new StudentDBHelper(this);
				GroupDBHelper groupDBHelper = new GroupDBHelper(this);
				assignedGroupIDs = statusDBHelper.getGroupIDs();
				for (int i=0;i<assignedGroupIDs.length;i++) {
					studentList[i] = studentDBHelper.getStudentsList(assignedGroupIDs[i]);
					assignedGroupNames[i] = groupDBHelper.getGroupById(assignedGroupIDs[i]);
				}
				if(programID.equals("2")){   //H-Learning
//					Intent selectGroup = new Intent(this,groupSelect.class);
//					startActivity(selectGroup);

				}
				else if(programID.equals("1")){   //RI
					populateImagesFromGallery();
				}
				else{
					Toast.makeText(this,"Invalid Program Id",Toast.LENGTH_SHORT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Reading configuration Json From SDCard
	public String getProgramId() {
		String crlJsonStr = null;

		try {
//            File myJsonFile= new File(MainActivity.fpath+"Json/funNavigate.json");
				File myJsonFile = new File(splashScreenVideo.fpath + "Json/NewKetanLearn.json");
				FileInputStream stream = new FileInputStream(myJsonFile);
				String jsonStr = null;
				try {
					FileChannel fc = stream.getChannel();
					MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

					jsonStr = Charset.defaultCharset().decode(bb).toString();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					stream.close();
				}
				JSONObject jsonObj = new JSONObject(jsonStr);
			crlJsonStr = jsonObj.getString("programId");

		}catch (Exception e){
			e.printStackTrace();
		}
		return crlJsonStr;
	}
	public void btnChoosePhotosClick(View v){

		Intent i = new Intent(MultiPhotoSelectActivity.this,MainActivity.class);
		startActivity(i);

		/*ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

		if (selectedItems!= null && selectedItems.size() > 0) {
			Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
			Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
		}
	*/}

	private void populateImagesFromGallery() {
		if (!mayRequestGalleryImages()) {
			return;
		}

		ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
		initializeRecyclerView(imageUrls);
	}

	private boolean mayRequestGalleryImages() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}

		if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
			//promptStoragePermission();
			showPermissionRationaleSnackBar();
		} else {
			requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
		}

		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
										   int[] grantResults) {

		switch (requestCode) {

			case REQUEST_FOR_STORAGE_PERMISSION: {

				if (grantResults.length > 0) {
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						populateImagesFromGallery();
					} else {
						if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
							showPermissionRationaleSnackBar();
						} else {
							Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
						}
					}
				}

				break;
			}
		}
	}

	private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        ArrayList<String> imageUrls = new ArrayList<String>();

		/*Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");*/
        Uri INTERNAL = MediaStore.Files.getContentUri("internal");
        Uri EXTERNAL = MediaStore.Files.getContentUri("external");

        File folder = new File(Environment.getExternalStorageDirectory() + "/StudentProfiles");
        if (folder.exists()) {
            Cursor imagecursor = getContentResolver().query(EXTERNAL,
                    null,
                    MediaStore.Images.Media.DATA + " like ? ",
                    new String[]{"%StudentProfiles%"},
                    null);


			String fileName="";//default fileName
			Uri filePathUri;
            for (int i = 1; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
				filePathUri = Uri.parse(imagecursor.getString(dataColumnIndex));
				fileName = filePathUri.getLastPathSegment().toString();
				for (int j=0;j<studentList.length;j++){
					for (int k=0;k<studentList[j].length();k++)
					{
						try {
							if (fileName.equals(studentList[j].getJSONObject(k).getString("studentId")))
                            {
                                imageUrls.add(imagecursor.getString(dataColumnIndex));
                            }
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}


                System.out.println("=====> Array path => " + imageUrls.get(i - 1));
            }

            return imageUrls;
        }
        else
        {
            return null;
        }
    }


	private void initializeRecyclerView(ArrayList<String> imageUrls) {
		imageAdapter = new ImageAdapter(this, imageUrls);

		RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset2));
		recyclerView.setAdapter(imageAdapter);
	}

	private void showPermissionRationaleSnackBar() {
		Snackbar.make(findViewById(R.id.btn_continue), getString(R.string.permission_rationale),
				Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Request the permission
				ActivityCompat.requestPermissions(MultiPhotoSelectActivity.this,
						new String[]{READ_EXTERNAL_STORAGE},
						REQUEST_FOR_STORAGE_PERMISSION);
			}
		}).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_admin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent goToAdminLogin = new Intent(MultiPhotoSelectActivity.this,AdminActivity.class);
			startActivity(goToAdminLogin);
		}

		return super.onOptionsItemSelected(item);
	}
}