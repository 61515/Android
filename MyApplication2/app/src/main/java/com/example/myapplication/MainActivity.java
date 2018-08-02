package com.example.myapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private List<Fruit> fruitList=new ArrayList<>();
    public static final int CHOOSE_PHOTO=1;
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///加载布局文件
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if(savedInstanceState!=null)
        {
            String temp=savedInstanceState.getString("imagepath");
            displayImage(temp);
        }*/
        ///////找到圆形框
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header);
        circleImageView=headerLayout.findViewById(R.id.icon_image);
        /////////////////////////////////////////////////
        /////////////////////////////////////////////////////
        /////////////////////////////
        ////////数据库建表
        LitePal.getDatabase();
        Book book=DataSupport.findFirst(Book.class);
        if(book!=null)
        {
            String sx=book.getimagepath();
            displayImage(sx);
        }


        //////////////////////////////////


        //找到工具栏ID和滑动式布局
        ////加载工具栏ID
        //////////////////////////////////////////////////
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        //////////////////////////
        ////设置工具栏返回键
        ////setHomeAsIndicator设置返回键的图标
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);
        }

        ////////////////////////////
        ////导航布局设置事件
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.nav_call:
                        Intent intent= new Intent(MainActivity.this,DatabaseActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        }
        );

        /////////////////////////
        ////圆形图片设置点击事件
        circleImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    /////申请权限
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]
                            {
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },1);
                }
                else
                {
                    openAlbum();
                }
            }
        });

        //////////////////////////////
        ////可放图标的圆形按钮
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        Log.d("MainActivity","able");
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //////////Snackbar 活动
                Snackbar.make(v,"Data deleted",
                        Snackbar.LENGTH_SHORT)
                        .setAction("Undo",
                                new View.OnClickListener()
                                {
                                    public void onClick(View v)
                                    {
                                        Toast.makeText(MainActivity.this,
                                                "Data restored",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });

        //////////////////////////////////
        /////初始化循环布局列表
        ///设置线性管理
        ///设置列表适配器
        initFruits();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter=new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
    }

    ////////////////////////////////////////
    private void openAlbum()
    {

        /////////开启活动
        ////活动返回回调函数
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }
    ///////////////////
    ///////////权限请求函数
    /////////////请求成功后调用开启获取内容的活动
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
            {
                openAlbum();
            }
            else
                {
                    Toast.makeText(this,"You denied the permisssion",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    ////////////////
    /////活动回调函数
    ///处理上一个活动返回的data数据
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch(requestCode)
        {
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    handleImageOnKitKat(data);
                }
        }
    }
    //////处理内容返回的数据函数
    ////设置图片
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data)
    {
        String imagePath=null;
        ///////获取url定位文件管理的位置
        Uri uri=data.getData();

        ///文档url
        if(DocumentsContract.isDocumentUri(this,uri))
        {
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID + "="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentUri= ContentUris.withAppendedId(Uri.parse(
                        "content://downloads//public_downloads"),
                        Long.valueOf(docId)
                );
                imagePath=getImagePath(contentUri,null);
            }
        }
        //内容url
        else if("content".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath=getImagePath(uri,null);
        }
        //文件url
        else if("file".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath=uri.getPath();
        }
        ////建表存储url，存储imagepath
        Book book= DataSupport.findFirst(Book.class);
        if(book==null)
        {
            book =new Book();
            book.setImagepath(imagePath);
            //新添加数据
            book.save();
        }
        else
        {
            DataSupport.deleteAll(Book.class);
            book=new Book();
            book.setImagepath(imagePath);
            book.save();
            ///更新数据
        }
        ////圆形image设置image path地址的图片
        displayImage(imagePath);

        ////////////////////////////////////////////////
    }
    ////根据uri地址和selection获取imagepath
    private String getImagePath(Uri uri,String selection)
    {
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images
                .Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    ////展示图像
    private void displayImage(String imagePath)
    {
        if(imagePath!=null)
        {
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            circleImageView.setImageBitmap(bitmap);


            //////////////////////////////////////////////
        }
        else
        {
            Toast.makeText(this,"failed to get image",
                    Toast.LENGTH_SHORT).show();
        }
    }
    //////////////////////////////////////////////////
    ///初始化；循环列表

    private void initFruits() {
        for(int i=0;i<1;i++)
        {
            Fruit apple=new Fruit("Apple",R.drawable.ic_launcher_background);
            fruitList.add(apple);
            Fruit banana=new Fruit("Banana",R.drawable.ic_launcher_background);
            fruitList.add(banana);
        }
    }
    /////////////加载工具栏菜单按钮
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    ////工具栏菜单子项单机函数
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.backup:
                Toast.makeText(this,"You clicked Backup",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"You clicked Delete",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"You clicked Settings",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    /////////////////////////////////////////
    ///存储bundle类型的数据方便下一次数据的恢复onCreate()
    /*@Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("imagepath",imagePath);
    }*/
}
