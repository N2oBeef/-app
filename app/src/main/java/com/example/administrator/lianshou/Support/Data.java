package com.example.administrator.lianshou.Support;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Data {
    private Data() {}
    private static Data single=null;
    //静态工厂方法
    public static Data getInstance() {
        if (single == null) {
            single = new Data();
        }
        return single;
    }

    private String mUserName;
    private int mTableid;

    public void SetUserName(String username)
    {
        this.mUserName = username;
    }
    public String GetUserName()
    {
        return this.mUserName;
    }

    public void SetTableid(int Tableid)
    {
        this.mTableid = Tableid;
    }
    public int GetTableid()
    {
        return this.mTableid;
    }
}
