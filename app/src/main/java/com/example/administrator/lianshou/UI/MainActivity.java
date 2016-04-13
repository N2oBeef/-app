package com.example.administrator.lianshou.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.lianshou.R;
import com.example.administrator.lianshou.Support.CONSTANT;
import com.example.administrator.lianshou.Support.Settings;
import com.example.administrator.lianshou.UI.Dish.DishFragment;
import com.example.administrator.lianshou.UI.Order.OrderFragment;
import com.example.administrator.lianshou.UI.ShopCart.ShopCartFragment;
import com.example.administrator.lianshou.UI.Table.TableFragment;
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

/**
 * Created by Administrator on 2016/4/8.
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Drawer drawer ;
    private AccountHeader header;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment ;
    private Menu menu;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent TmpIntent = getIntent();
        Bundle bundle = TmpIntent.getExtras();
        String StrUserName = bundle.getString("username");
        initdate(StrUserName);

        currentFragment = new TableFragment(mhandler);
        switchFragment();
    }

    private void switchFragment(){
        if(currentFragment instanceof TableFragment){
            switchFragment(currentFragment, "桌台", R.menu.menu_table);
        }
        else if (currentFragment instanceof DishFragment){
            switchFragment(currentFragment, "菜品", R.menu.menu_dish);
        }
        else if (currentFragment instanceof ShopCartFragment){
            switchFragment(currentFragment, "购物车", R.menu.menu_shopcart);
        }
        else if (currentFragment instanceof OrderFragment){
            switchFragment(currentFragment, "订单", R.menu.menu_order);
        }

    }

    private void switchFragment(Fragment fragment,String title,int resourceMenu){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);
        if(menu != null) {
            menu.clear();
            getMenuInflater().inflate(resourceMenu, menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void initdate(String UserName) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        header = new AccountHeaderBuilder().withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem().withIcon(R.drawable.logo)
                        .withName(UserName))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {


                        return false;
                    }
                })
                .build();

        drawer = new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(header)
                .withSliderBackgroundColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.night_primary) : ContextCompat.getColor(this, R.color.white))
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.Table).withIcon(R.mipmap.ic_home).withIdentifier(R.mipmap.ic_home)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.Surcharge).withIcon(R.mipmap.ic_science).withIdentifier(R.mipmap.ic_science)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.Dish).withIcon(R.mipmap.ic_news).withIdentifier(R.mipmap.ic_news)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.payForBill_changeTable).withIcon(R.mipmap.ic_collect_grey).withIdentifier(R.mipmap.ic_collect_grey)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.ShopCat).withIcon(R.mipmap.ic_collect_grey).withIdentifier(R.mipmap.ic_collect_grey)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new SectionDrawerItem().withName(R.string.app_name).withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new SecondaryDrawerItem().withName(Settings.isNightMode == true ? "一" : "一111")
                                .withIcon(Settings.isNightMode == true ? R.mipmap.ic_day_white : R.mipmap.ic_night).withIdentifier(R.mipmap.ic_night)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_light)),
                        new SecondaryDrawerItem().withName("一").withIcon(R.mipmap.ic_setting).withIdentifier(R.mipmap.ic_setting)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_light)),
                        new SecondaryDrawerItem().withName("一").withIcon(R.mipmap.ic_about).withIdentifier(R.mipmap.ic_about)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_light))
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case R.mipmap.ic_home:
                                currentFragment = new TableFragment(mhandler);
                                break;
                            case R.mipmap.ic_reading:

                                break;
                            case R.mipmap.ic_news:

                                break;
                            case R.mipmap.ic_science:
                                currentFragment = new OrderFragment();
                                break;
                            case R.mipmap.ic_collect_grey:
                                currentFragment = new ShopCartFragment();
                                break;
                            case R.mipmap.ic_night:

                                return false;
                            case R.mipmap.ic_setting:

                                return false;
                            case R.mipmap.ic_about:

                                return false;
                        }
                        switchFragment();
                        return false;
                    }
                })
                .build();

    }
    private android.os.Handler mhandler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what)
            {
                case CONSTANT.ID_TABLE_TO_DISH: {
                    currentFragment = new DishFragment();
                }
                break;
            }
            switchFragment();
            return true;
        }
    });


}
