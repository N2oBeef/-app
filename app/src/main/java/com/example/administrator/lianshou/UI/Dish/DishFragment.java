package com.example.administrator.lianshou.UI.Dish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.lianshou.API.UrlApi;
import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.Adapter.DishSimpleAdapter;
import com.example.administrator.lianshou.Support.CONSTANT;
import com.example.administrator.lianshou.Support.HttpUtil;
import com.example.administrator.lianshou.Support.Utils;
import com.example.administrator.lianshou.UI.MainActivity;
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
 * Created by Administrator on 2016/4/11.
 */
public class DishFragment extends Fragment {
    protected View parentView;
    private GridView gridView;
    List<Map<String, Object>> mdata;
    private ProgressBar mProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.framelayout_dish, container, false);

        gridView = (GridView)parentView.findViewById(R.id.DishGridView);
        gridView.setVisibility(View.GONE);

        mProgressBar = (ProgressBar)  parentView.findViewById(R.id.progressbar);

        DishThread TmpDishThread = new DishThread();
        TmpDishThread.start();
        return parentView;
    }

    private class DishThread extends Thread {
        public void run()
        {
            RequestBody TmpFormBody = new FormEncodingBuilder()
                    .add("robot", "123")
                    .add("shopid", "22")
                    .build();

            Request.Builder builder = new Request.Builder();
            builder.url(UrlApi.Dish_Url).post(TmpFormBody);
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

                            Log.d(obj.getString("dish_picurl"), "onResponse: ");
                            map.put("image", Utils.returnBitMap(obj.getString("dish_picurl")));
                            map.put("text", obj.getString("dish_dishprice"));
                            map.put("dishid", obj.getString("dish_id"));
                            map.put("originalprice", obj.getString("dish_dishprice"));
                            map.put("nowprice", obj.getString("dish_dishprice"));

                            TmpList.add(map);
                        }
                        mdata = TmpList;
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
                        DishSimpleAdapter b = new DishSimpleAdapter(getContext(), mdata, R.layout.framelayout_dish_gridview_item, new String[]{"image", "text"}, new int[]{R.id.image, R.id.text});
                        gridView.setAdapter(b);
                        b.setViewBinder(new SimpleAdapter.ViewBinder() {
                            public boolean setViewValue(View view, Object data,
                                                        String textRepresentation) {
                                //判断是否为我们要处理的对象
                                if (view instanceof ImageView && data instanceof Bitmap) {
                                    ImageView iv = (ImageView) view;
                                    iv.setImageBitmap((Bitmap) data);
                                    return true;
                                } else
                                    return false;
                            }
                        });

                        gridView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                    break;
                }
                case CONSTANT.ID_FAILURE: {
                    if (getContext() != null)
                        Toast.makeText(getContext(), "xxxxx", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            return true;
        }
    });
}