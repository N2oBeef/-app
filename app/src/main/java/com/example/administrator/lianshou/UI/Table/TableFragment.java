package com.example.administrator.lianshou.UI.Table;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lianshou.API.UrlApi;
import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.Adapter.TableSimpleAdapter;
import com.example.administrator.lianshou.Support.CONSTANT;
import com.example.administrator.lianshou.Support.HttpUtil;
import com.example.administrator.lianshou.UI.MainActivity;
import com.example.administrator.lianshou.g_application;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/9.
 */
public class TableFragment extends Fragment {
    private Handler mMainHandler;
    public TableFragment(Handler handler)
    {
        this.mMainHandler = handler;
    }

    protected View parentView;
    private ListView mListView;
    private LinearLayout mTableLinearLayout;
    List<Map<String, Object>> mdata;
    private ProgressBar mProgressBar;
    private Button mGoDish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.framlayout_table, container, false);

        mTableLinearLayout = (LinearLayout) parentView.findViewById(R.id.TableLayout);
        mTableLinearLayout.setVisibility(View.GONE);

        mProgressBar = (ProgressBar)  parentView.findViewById(R.id.progressbar);

        mGoDish = (Button) parentView.findViewById(R.id.GoDish);
        mGoDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mMainHandler.sendEmptyMessage(CONSTANT.ID_TABLE_TO_DISH);
            }
        });
        TableThread TmpTableThread = new TableThread();
        TmpTableThread.start();
        return parentView;
    }

    private class TableThread extends Thread{

        public void run()
        {
            RequestBody TmpFormBody = new FormEncodingBuilder()
                    .add("robot", "123")
                    .add("shopid", "22")
                    .build();

            Request.Builder builder = new Request.Builder();
            builder.url(UrlApi.Table_Url).post(TmpFormBody);
            Request request = builder.build();
            HttpUtil.enqueue(request, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    mhandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful() == false) {
                        return;
                    } else {
                        try {
                            String TmpRes = response.body().string();
                            JSONArray array = new JSONArray(TmpRes);

                            List<Map<String, Object>> TmpList = new ArrayList<Map<String, Object>>();

                            for (int i = 0; i < array.length(); i++) {
                                Map<String, Object> map = new HashMap<>();
                                JSONObject obj = array.getJSONObject(i);

                                map.put("TableName", obj.getString("table_name"));

                                if (obj.getInt("table_istaken") != 0)
                                    map.put("IsService", "未开台");
                                else
                                    map.put("IsService", "已开台");

                                map.put("PeopleNumber", obj.getInt("table_mostpeople"));

                                map.put("TableId", obj.getInt("table_id"));
                                map.put("Tablelowerpay", obj.getInt("table_lowerpay"));
                                map.put("Tablenum", obj.getString("table_num"));
                                map.put("Tabletypeid", obj.getInt("table_typeid"));
                                Log.d("i:" + i, "onResponse: ");
                                TmpList.add(map);
                            }
                            mdata = TmpList;

                            mhandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

        }
    }

    private android.os.Handler mhandler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what)
            {
                case CONSTANT.ID_SUCCESS: {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "chenggong", Toast.LENGTH_SHORT).show();

                        mTableLinearLayout.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);

                        mListView = (ListView) parentView.findViewById(R.id.listview);
                        SimpleAdapter Tmpadapter = new TableSimpleAdapter(getContext(), mdata,
                                R.layout.framelayout_table_listview_item, new String[]{"TableName", "PeopleNumber", "IsService"},
                                new int[]{R.id.TableName, R.id.PeopleNumber, R.id.IsService});

                        mListView.setAdapter(Tmpadapter);
                    }
                    break;
                }
                case CONSTANT.ID_FAILURE: {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
            return true;
        }
    });


}
