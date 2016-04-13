package com.example.administrator.lianshou.UI.Order;


import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.lianshou.API.UrlApi;
import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.CONSTANT;
import com.example.administrator.lianshou.Support.Data;
import com.example.administrator.lianshou.Support.HttpUtil;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/14.
 */
public class OrderFragment extends Fragment {
    protected View parentView;
    List<Map<String, Object>> mdata;
    private ListView mListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_order, container, false);
        mListView = (ListView)parentView.findViewById(R.id.listview);

        if (Data.getInstance().GetTableid() != 0) {
            OrderThread TmpOrderThread = new OrderThread();
            TmpOrderThread.start();
        }
        else
        {
            mhandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
        }

        return parentView;
    }

    public class OrderThread extends Thread
    {
        public void run()
        {
            RequestBody TmpFormBody = new FormEncodingBuilder()
                    .add("robot", "123")
                    .add("shopid", "22")
                    .add("tableid", String.valueOf(Data.getInstance().GetTableid()))
                    .build();

            Request.Builder builder = new Request.Builder();
            builder.url(UrlApi.Order_Url).post(TmpFormBody);
            Request request = builder.build();
            HttpUtil.enqueue(request, new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful() == false) {
                        return;
                    }
                    try {
                        String TmpRes = response.body().string();
                        JSONObject object = new JSONObject(TmpRes);

                        List<Map<String, Object>> TmpList = new ArrayList<Map<String, Object>>();
                        try {
                            Iterator it = object.keys();
                            while (it.hasNext()) {

                                String key = (String) it.next();

                                String TmpValue = object.getString(key);
                                JSONObject Tmpobject = new JSONObject(TmpValue);

                                Map<String, Object> map = new HashMap<>();
                                map.put("number", "数量" + Tmpobject.getString("ordernow_num"));

                                if ( Tmpobject.getString("ordernow_confirm").equals("0"))
                                    map.put("confirm", "未划菜");
                                else
                                    map.put("confirm", "已划菜");

                                map.put("totalprice", "价格:" + Tmpobject.getString("ordernow_nowprice"));
                                map.put("dishname", Tmpobject.getString("dish_dishname"));
                                TmpList.add(map);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mdata = TmpList;
                       // JSONArray array = new JSONArray(TmpRes);
                       // List<Map<String, Object>> TmpList = new ArrayList<Map<String, Object>>();

                        //for (int i = 0; i < array.length(); i++) {
                           // Map<String, Object> map = new HashMap<>();
                           // JSONObject obj = array.getJSONObject(i);

                           // map.put("number", obj.getString("ordernow_num"));
                           // map.put("confirm", obj.getString("ordernow_confirm"));
                            //map.put("totalprice", obj.getString("ordernow_nowprice"));
                        //map.put("dishname", obj.getString("dish_dishname"));
//                            TmpList.add(map);
                      //  }


                        mhandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                        SimpleAdapter TmpAdapter = new SimpleAdapter(getContext(), mdata, R.layout.framegment_order_item, new String[]{"number", "confirm", "totalprice", "dishname"}, new int[]{R.id.number, R.id.confirm, R.id.totalprice, R.id.dishname});
                        mListView.setAdapter(TmpAdapter);
                    }
                    break;
                }
                case CONSTANT.ID_FAILURE: {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "你不能获取数据", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
            return true;
        }
    });
}
