package com.example.administrator.lianshou.Support.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/11.
 */
public class TableSimpleAdapter extends SimpleAdapter {
    private Context mContext;
    private  List<Map<String, Object>> mlist;
    public TableSimpleAdapter(Context context, List<? extends Map<String, ?>> data,

                           int resource, String[] from, int[] to) {

        super(context, data, resource, from, to);
        this.mContext = context;
        this.mlist = (List<Map<String, Object>>)data;

    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        TextView TmpPeopleTextView = (TextView)(v.findViewById(R.id.PeopleNumber));
        TmpPeopleTextView.setText("0/" + mlist.get(position).get("PeopleNumber"));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mlist.get(position).get("IsService").equals("已开台")) {
                    View tmpView = (View) parent.getParent();
                    View parentView = (View) tmpView.getParent();

                    TextView TmpTableNameShow = (TextView) parentView.findViewById(R.id.TableNameShow);
                    TmpTableNameShow.setText(mlist.get(position).get("TableName").toString());

                    TextView TmpTableTypeShow = (TextView) parentView.findViewById(R.id.TableTypeShow);
                    TmpTableTypeShow.setText(mlist.get(position).get("TableId").toString());

                    TextView TmpTableMinSafeShow = (TextView) parentView.findViewById(R.id.TableMinSafeShow);
                    TmpTableMinSafeShow.setText("最低消费" + mlist.get(position).get("Tablelowerpay").toString());

                    TextView TmpTableMaxPeopleShow = (TextView) parentView.findViewById(R.id.TableMaxPeopleShow);
                    TmpTableMaxPeopleShow.setText("桌台编号" + mlist.get(position).get("Tablenum").toString());

                    TextView TmpTableServicePeopleShow = (TextView) parentView.findViewById(R.id.TableServicePeopleShow);
                    TmpTableServicePeopleShow.setText("容纳人数" + mlist.get(position).get("PeopleNumber").toString());

                    Data.getInstance().SetTableid((int) mlist.get(position).get("TableId"));
                } else {
                    Toast.makeText(mContext, "你选择的桌台还没有开台", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }
}
