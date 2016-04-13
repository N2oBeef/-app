package com.example.administrator.lianshou.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.lianshou.API.UrlApi;
import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.CONSTANT;
import com.example.administrator.lianshou.Support.HttpUtil;
import com.example.administrator.lianshou.Support.Settings;
import com.example.administrator.lianshou.Support.Utils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2016/4/8.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText musername,mpassword;
    private Button mLoginButton,mBackButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        musername = (EditText) findViewById(R.id.username);
        mpassword = (EditText) findViewById(R.id.password);

        mLoginButton = (Button) findViewById(R.id.Login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginThread tmpLoginThread = new LoginThread(musername.getText().toString(), mpassword.getText().toString(), mhandler);
                tmpLoginThread.start();
            }
        });

        mBackButton = (Button) findViewById(R.id.back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    class LoginThread extends Thread
    {
        private String musername,mpassword;
        private android.os.Handler mhandler;

        public LoginThread(String username,String password,android.os.Handler Handler)
        {
            this.musername = username;
            this.mpassword = password;
            this.mhandler  = Handler;
        }

        public void run()
        {

            RequestBody TmpFormBody = new FormEncodingBuilder()
                    .add("robot", "123")
                    .add("username", musername)
                    .add("password", mpassword)
                    .build();

            Request.Builder builder = new Request.Builder();
            builder.url(UrlApi.Login_Url).post(TmpFormBody);
            Request request = builder.build();
            HttpUtil.enqueue(request, new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                   mhandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if(response.isSuccessful() == false) {
                        mhandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                        return;
                    }

                    String TmpRes = response.body().string();

                    if (Utils.Login(TmpRes)) {
                        mhandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
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
                    Intent TmpIntent = new Intent(LoginActivity.this, MainActivity.class);
                    TmpIntent.putExtra("username", musername.getText().toString());
                    startActivity(TmpIntent);
                }
                case CONSTANT.ID_FAILURE: {
                    musername.setText("");
                    mpassword.setText("");
                }
            }
            return true;
        }
    });
}

