package com.example.pef.prathamopenschool;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView, horizontalrecyclerView ;
    public static String appname="";
    private CardAdapter adapter;
    private List<Card> cardList;
    public static String fpath,fpathMedia;
    String newNodeList;
    JSONArray contentNavigate;
    int i;
    private HashMap<String, String> navigateJson;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        String files[] = new String [] {"/storage/sdcard1/.PrathamOpenSchool/studentprofiles/img1.png","/storage/sdcard1/.PrathamOpenSchool/studentprofiles/new2.png","/storage/sdcard1/.PrathamOpenSchool/Json/learnNavigate.json"};

        Compress zipObj=new Compress(files, Environment.getExternalStorageDirectory()+"/new.zip");

        zipObj.zip();

        List<String> arrayList = new ArrayList<String>();

        arrayList=zipObj.unzip(Environment.getExternalStorageDirectory()+"/new.zip",Environment.getExternalStorageDirectory()+"/new");
*/
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        *///getSdCardPath();
        initCollapsingToolbar();

        newNodeList=getIntent().getStringExtra("nodeList");


        recyclerView = (RecyclerView) findViewById(R.id.verticalGridLayout);
        horizontalrecyclerView = (RecyclerView) findViewById(R.id.horizontalgridlayout);


        GridLayoutManager gridlayoutManager = new GridLayoutManager(this , 1 , GridLayoutManager.HORIZONTAL, false);
        horizontalrecyclerView.setLayoutManager(gridlayoutManager);

        cardList = new ArrayList<>();
        adapter = new CardAdapter(this, cardList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

/*        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
*/

//        PdJsonParser myJsonParser = new PdJsonParser();

        ReadMyFile();

/*
        navigateJson = myJsonParser.ReadMyFile(newNodeList);
        String abc= "tp";
*/

/*
        for(int i=0 ; i<navigateJson.size() ; i++){

            Log.d(TAG, "##################"+navigateJson.get(i));

        }
*/


        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


   /* public void getSdCardPath()
    {
        CharSequence c="";

        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
        try {
            c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
            appname=c.toString();
            Log.w("LABEL", c.toString());
        }
        catch(Exception e) {//Name Not FOund Exception
        }*/

        /*if(new File("/mnt/usb_storage/USB_DISK0/udisk0/prathamMarathi").exists()){
            Toast.makeText(getApplicationContext(),"This path is from pen drive.",Toast.LENGTH_LONG).show();
            fpath="/mnt/usb_storage/USB_DISK0/udisk0/prathamMarathi/";
        }
        else if(new File("/mnt/internal_sd/prathamMarathi").exists()){
            Toast.makeText(getApplicationContext(),"This path is from internal storage.",Toast.LENGTH_LONG).show();
            fpath="/mnt/internal_sd/prathamMarathi";
        }*/

/*
        if(appname.contains("Pratham")) {

            if ( (new File("/storage/extSdCard/.PrathamOpenSchool/Json/").exists()) ) {
                fpath = "/storage/extSdCard/";
            } else if ( (new File("/storage/sdcard1/.PrathamOpenSchool/Json/").exists())  ) {
                fpath = "/storage/sdcard1/";
            } else if ( (new File("/storage/usbcard1/.PrathamOpenSchool/Json/").exists()) ) {
                fpath = "/storage/usbcard1/";

            } else if ( (new File("/storage/sdcard0/.PrathamOpenSchool/Json/").exists())  ) {
                fpath = "/storage/sdcard0/";

            } else if ( (new File("/storage/emulated/0/.PrathamOpenSchool/Json/").exists())  ) {
                fpath ="/storage/emulated/0/";
            }
            fpath = fpath + ".PrathamOpenSchool/";

            //         fpathMedia="/storage/sdcard1/.PrathamOpenSchool/Media/";
            fpathMedia=fpath+"Media/";
        }
*/
 //   }
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    public void ReadMyFile (){
        try {
            if (newNodeList == null) {

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

                // Getting data JSON Array nodes
//            JSONArray contentNavigate  = jsonObj.getJSONArray("funNavigate");
                contentNavigate = jsonObj.getJSONArray("nodelist");
//            JSONObject contentNavigate2 = jsonObj.getJSONObject("nodelist");

            } else {

                JSONArray jsnarray = new JSONArray(newNodeList);
                contentNavigate = jsnarray;
                // contentNavigate = findElementsChildren(contentNavigate.getJSONObject(""),newNodeId);
            }

            // looping through All nodes
            for (int i = 0; i < contentNavigate.length(); i++) {

                String notetitle;
                JSONObject c = contentNavigate.getJSONObject(i);

                Card card = new Card();
                card.nodeId=c.optString("nodeId");
                card.nodeType=c.optString("nodeType");
                card.nodeTitle = c.optString("nodeTitle").toString();
                notetitle = c.optString("nodeTitle").toString();
                card.nodeImage = splashScreenVideo.fpath+"Media/"+c.optString("nodeImage").toString();
                card.nodePhase= c.optString("nodePhase").toString();
                card.nodeAge= c.optString("nodeAge").toString();
                card.nodeDesc= c.optString("nodeDesc").toString();
                card.nodeKeywords= c.optString("nodeKeywords").toString();
                card.sameCode= c.optString("sameCode").toString();
                card.resourceId= c.optString("resourceId").toString();
                card.resourceType= c.optString("resourceType").toString();
                card.resourcePath= c.optString("resourcePath").toString();
                card.nodeList = c.optString("nodelist").toString();

                cardList.add(card);


/*                // tmp hashmap for single node
                HashMap<String, String> parsedData = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                parsedData.put("resourceId",resourceId);
                parsedData.put("resourceName",resourceName);
                parsedData.put("resourceType",resourceType);
                parsedData.put("source",source);
                parsedData.put("resourceImage",resourceImage);
                parsedData.put("sameCode",sameCode);
                parsedData.put("demo",demo);
                parsedData.put("resourcePhase",resourcePhase);
*/

                // do what do you want on your interface
            }
            adapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Adding few albums for testing
     */
    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
