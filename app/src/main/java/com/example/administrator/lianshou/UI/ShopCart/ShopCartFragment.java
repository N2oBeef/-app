package com.example.administrator.lianshou.UI.ShopCart;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.lianshou.API.UrlApi;
import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.Adapter.DishSimpleAdapter;
import com.example.administrator.lianshou.Support.CONSTANT;
import com.example.administrator.lianshou.Support.Data;
import com.example.administrator.lianshou.Support.HttpUtil;
import com.example.administrator.lianshou.Support.Utils;
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
 * Created by Administrator on 2016/4/13.
 */
public class ShopCartFragment extends Fragment {
    protected View parentView;
    List<Map<String, Object>> mdata;
    private ListView mListView;
    private Button mProcedureOrder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.framegment_shopcart, container, false);
        mListView = (ListView)parentView.findViewById(R.id.listview);

        if (Data.getInstance().GetTableid() != 0) {
            ShopCartThread TmpShopCartThread = new ShopCartThread();
            TmpShopCartThread.start();
        }
        else
        {
            mhandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
        }

        mProcedureOrder = (Button)parentView.findViewById(R.id.ProcedureOrder);
        mProcedureOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPay TmpSendPay = new SendPay();
                TmpSendPay.start();
            }
        });

        return parentView;
    }

    public class SendPay extends Thread
    {
        public void run()
        {
            RequestBody TmpFormBody = new FormEncodingBuilder()
                    .add("robot", "123")
                    .add("shopid", "22")
                    .add("tableid", String.valueOf(Data.getInstance().GetTableid()))
                    .add("orderremark","2")
                    .add("operatorid","boss")
                    .build();
           String id = String.valueOf(Data.getInstance().GetTableid());
            Request.Builder builder = new Request.Builder();
            builder.url(UrlApi.ProduceOrder_Url).post(TmpFormBody);
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
                        JSONObject obj = new JSONObject(TmpRes);

                        if (obj.get("result").toString().equals("1"))
                            mhandler.sendEmptyMessage(CONSTANT.ID_PROCEDUREORDER);

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    public class ShopCartThread extends Thread
    {
        public void run()
        {
            RequestBody TmpFormBody = new FormEncodingBuilder()
                    .add("robot", "123")
                    .add("shopid", "22")
                    .add("tableid", String.valueOf(Data.getInstance().GetTableid()))
                    .build();

            Request.Builder builder = new Request.Builder();
            builder.url(UrlApi.ShopCart_Url).post(TmpFormBody);
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
                        JSONArray array = new JSONArray(TmpRes);
                        List<Map<String, Object>> TmpList = new ArrayList<Map<String, Object>>();

                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            JSONObject obj = array.getJSONObject(i);

                            map.put("number", obj.getString("shopcart_num"));
                            map.put("dishid", obj.getString("dish_id"));
                            map.put("totalprice", obj.getString("totalprice"));
                            map.put("dishname", obj.getString("dish_dishname"));
                            TmpList.add(map);
                        }

                        mdata = TmpList;
                        if (mdata != null)
                            mhandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                        else
                            mhandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
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
                        SimpleAdapter TmpAdapter = new SimpleAdapter(getContext(), mdata, R.layout.framegment_shopcart_item, new String[]{"number", "dishid", "totalprice", "dishname"}, new int[]{R.id.number, R.id.dishid, R.id.totalprice, R.id.dishname});
                        mListView.setAdapter(TmpAdapter);
                    }
                    break;
                }
                case CONSTANT.ID_FAILURE: {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "你不能获取数据", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), String.valueOf(msg.what), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case CONSTANT.ID_PROCEDUREORDER: {
                    if (getContext() != null) {

                        Toast.makeText(getContext(), "下订单成功", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), String.valueOf(msg.what), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }


            }
            return true;
        }
    });


}
