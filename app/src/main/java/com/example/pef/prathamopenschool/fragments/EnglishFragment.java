package com.example.pef.prathamopenschool.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pef.prathamopenschool.R;
import com.example.pef.prathamopenschool.MainActivity;
import com.example.pef.prathamopenschool.splashScreenVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.R.id.parent;
import static com.example.pef.prathamopenschool.R.id.container;
import static java.lang.Integer.parseInt;


public class EnglishFragment extends Fragment {

    String newNodeList="", fragName="EnglishFragment";
    JSONArray contentNavigate;
    String subFolderNodeList;
    int position=0;
    Context mContext;

    CardView cardView;

    public EnglishFragment() {

    }

    @SuppressLint("ValidFragment")
    public EnglishFragment(String newNodeList) {
        this.newNodeList = newNodeList;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_english, container, false);
        final GridView gridview = (GridView)view.findViewById(R.id.gridview);
        cardView = (CardView) view.findViewById(R.id.card_view);


        List<ItemObject> allItems = getAllItemObject(newNodeList);

        //passing list frm the function
//        FragmentManager fragment = this.getFragmentManager();
//        fragment.

        CustomAdapter customAdapter = new CustomAdapter(getActivity(), allItems, fragName);
        gridview.setAdapter(customAdapter);


/*        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // find your fragment
                List<ItemObject> allItems = getAllItemObject(subFolderNodeList);
                CustomAdapter customAdapter = new CustomAdapter(getActivity(), allItems, "tempName");
                gridview.setAdapter(customAdapter);

                Toast.makeText(getActivity().getApplicationContext(), "Position English: " + position, Toast.LENGTH_SHORT).show();
            }
        });*/
        return view;
    }


    private List<ItemObject> getAllItemObject(String newNodeList){

        List<ItemObject> items = new ArrayList<>();


        try {
            JSONArray jsnarray = null;
            jsnarray = new JSONArray(newNodeList);
            contentNavigate = jsnarray;
            for (int i = 0; i < jsnarray.length(); i++) {

                String notetitle;
                JSONObject c = contentNavigate.getJSONObject(i);
                String nodeTitle = c.optString("nodeTitle").toString();
                String nodeId=c.optString("nodeId");
/*                nodeType=c.optString("nodeType");
*/
                String nodeImage = splashScreenVideo.fpath+"Media/"+c.optString("nodeImage").toString();
                String nodePhase= c.optString("nodePhase").toString();
/*
                nodeAge= c.optString("nodeAge").toString();
                nodeDesc= c.optString("nodeDesc").toString();
                nodeKeywords= c.optString("nodeKeywords").toString();
                sameCode= c.optString("sameCode").toString();
                resourceId= c.optString("resourceId").toString();
                resourceType= c.optString("resourceType").toString();
                resourcePath= c.optString("resourcePath").toString();
*/
                String nodeList = c.optString("nodelist").toString();
                subFolderNodeList=nodeList;

                items.add(new ItemObject(nodeImage , nodeTitle , nodePhase, nodeList));

                /****************************************/

                final CustomAdapter.ViewHolder listViewHolder;

                    listViewHolder = new CustomAdapter.ViewHolder();
//                    View view = (mContext  , Integer.parseInt(nodeId), cardView);
                    /*fragment_english=(mContext , Integer.parseInt(nodeId), cardView);*/
                    listViewHolder.tabThumbnail = (ImageView)cardView.findViewById(R.id.tabThumbnail);
                    listViewHolder.tabTitle = (TextView)cardView.findViewById(R.id.tabTitle);
                    listViewHolder.tabPhase = (TextView)cardView.findViewById(R.id.tabPhase);
                    listViewHolder.rlcard= (RelativeLayout) cardView.findViewById(R.id.rlCard);

                    cardView.setTag(listViewHolder);

                listViewHolder.rlcard.setTag(i);
                final String nodelist=nodeList;
                Bitmap bitmap = BitmapFactory.decodeFile(nodeImage);
                listViewHolder.tabThumbnail.setImageBitmap(bitmap);
                listViewHolder.tabTitle.setText(nodeTitle);
                listViewHolder.tabPhase.setText(nodePhase);

                listViewHolder.rlcard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<ItemObject> allItems = getAllItemObject(nodelist);


/*                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new EnglishFragment(subFolderNodeList);

                    }
                });*/
                        Toast.makeText(mContext,"Nodelist:"+allItems, Toast.LENGTH_SHORT).show();

                    }});

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }


    static class ViewHolder{
        ImageView tabThumbnail;
        TextView tabTitle;
        TextView tabPhase;

        RelativeLayout rlcard;

    }


}
