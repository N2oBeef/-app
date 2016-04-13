package com.example.administrator.lianshou.Support.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.Data;
import com.example.administrator.lianshou.Support.HttpUtil;
import com.example.administrator.lianshou.Support.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/12.
 */
public class DishSimpleAdapter extends SimpleAdapter {
    private Context mContext;
    private List<Map<String, Object>> mlist;
    public DishSimpleAdapter(Context context, List<? extends Map<String, ?>> data,

                              int resource, String[] from, int[] to) {

        super(context, data, resource, from, to);
        this.mContext = context;
        this.mlist = (List<Map<String, Object>>)data;

    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, mlist.get(position).get("text").toString(), Toast.LENGTH_SHORT).show();
                DishThread TmpDishThread = new DishThread(position);
                TmpDishThread.start();
            }
        });
        return v;
    }

    private class DishThread extends Thread
    {
        private int mposition;
        public DishThread(int position)
        {
            this.mposition = position;
        }

        public void run()
        {
            RequestBody TmpFormBody = new FormEncodingBuilder()
                    .add("robot", "123")
                    .add("shopid", "22")
                    .add("tableid", String.valueOf(Data.getInstance().GetTableid()))
                    .add("dishid",mlist.get(mposition).get("dishid").toString())
                    .add("num","1")
                    .add("originalprice",mlist.get(mposition).get("originalprice").toString())
                    .add("nowprice",mlist.get(mposition).get("nowprice").toString())
                    .add("specprice","2")
                    .add("specvalue","2")
                    .add("dishremark", "2")
                    .add("operatorid","boss")
                    .build();

            Request.Builder builder = new Request.Builder();
            builder.url("http://old.chifanbao.com/index.php/Home/Order/ajaxAddCart").post(TmpFormBody);
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

                    String TmpRes = response.body().string();
                    Log.d(TmpRes, "onResponse: ");
                }
            });


        }
    }

}

