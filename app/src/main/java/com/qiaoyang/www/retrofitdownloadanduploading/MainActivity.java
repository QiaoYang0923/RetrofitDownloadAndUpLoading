package com.qiaoyang.www.retrofitdownloadanduploading;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Joe
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 上传按钮
     */
    private Button upLoading;
    /**
     * 下载按钮
     */
    private Button downLoad;

    /**
     * SD卡根目录
     */
    public static final String SD_HOME_DIR = Environment.getExternalStorageDirectory().getPath() + "/Joe/";

    /**
     * 要上传的文件存储位置
     */
    private final String file1Location = SD_HOME_DIR + "file1.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getJurisdiction();
        setViewClick();
    }

    /**
     * 设置view点击事件
     */
    private void setViewClick() {
        upLoading.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "我是上传", Toast.LENGTH_SHORT).show();
            upLoadingMethod();
        });
        downLoad.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "我是下载", Toast.LENGTH_SHORT).show();
            downLoadMethod();
        });
    }

    /**
     * 响应上传点击事件的方法
     */
    private void upLoadingMethod() {

        //创建文件(你需要上传到服务器的文件)
        //file1Location文件的路径 ,我是在手机存储根目录下创建了一个文件夹,里面放着了一张图片;
        File file = new File(file1Location);

        //创建表单map,里面存储服务器本接口所需要的数据;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //在这里添加服务器除了文件之外的其他参数
                .addFormDataPart("参数1", "值1")
                .addFormDataPart("参数2", "值2");


        //设置文件的格式;两个文件上传在这里添加
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // RequestBody imageBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        //添加文件(uploadfile就是你服务器中需要的文件参数)
        builder.addFormDataPart("uploadfile", file.getName(), imageBody);
        //builder.addFormDataPart("uploadfile1", file1.getName(), imageBody1);
        //生成接口需要的list
        List<MultipartBody.Part> parts = builder.build().parts();
        //创建设置OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                //允许失败重试
                .retryOnConnectionFailure(true)
                .build();
        //创建retrofit实例对象
        Retrofit retrofit = new Retrofit.Builder()
                //设置基站地址(基站地址+描述网络请求的接口上面注释的Post地址,就是要上传文件到服务器的地址,
                // 这只是一种设置地址的方法,还有其他方式,不在赘述)
                .baseUrl("你的基站地址")
                //设置委托,使用OKHttp联网,也可以设置其他的;
                .client(okHttpClient)
                //设置数据解析器,如果没有这个类需要添加依赖:
                .addConverterFactory(GsonConverterFactory.create())
                //设置支持rxJava
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //实例化请求接口,把表单传递过去;
        Call<BaseBean> call = retrofit.create(Api.class).upLoading(parts);
        //开始请求
        call.enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                //联网有响应或有返回数据
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                //连接失败,多数是网络不可用导致的
                System.out.println("网络不可用");
            }
        });

    }

    /**
     * 获取权限
     */
    private void getJurisdiction() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //进行权限请求
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }
    }

    /**
     * 响应下载点击事件的方法
     */
    private void downLoadMethod() {

        //创建设置OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                //允许失败重试
                .retryOnConnectionFailure(true)
                .build();
        //创建retrofit实例对象
        Retrofit retrofit = new Retrofit.Builder()
                //设置基站地址(基站地址+描述网络请求的接口上面注释的Post地址,就是要上传文件到服务器的地址,
                // 这只是一种设置地址的方法,还有其他方式,不在赘述)
                .baseUrl("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=e3dc64d05a3d26972ed30f5b6dc0d5c6/")
                //设置委托,使用OKHttp联网,也可以设置其他的;
                .client(okHttpClient)
                //设置数据解析器,如果没有这个类需要添加依赖:
                .addConverterFactory(GsonConverterFactory.create())
                //设置支持rxJava
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //我在百度上找到一张图片,把他的地址拆分了一下,最后一个斜杠之前的url设置为了baseUrl,斜杠之后设置在这里;
        Call<ResponseBody> download = retrofit.create(Api.class).download("241f95cad1c8a7868a2713146c09c93d70cf509e.jpg");
        download.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.isSuccessful()) {
                    boolean toDisk = writeResponseBodyToDisk(response.body());
                    if (toDisk) {
                        System.out.println("下载成功请查看");
                    } else {

                        System.out.println("下载失败,请稍后重试");
                    }
                } else {
                    System.out.println("服务器返回错误");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //连接失败,多数是网络不可用导致的
                System.out.println("网络不可用");
            }
        });
    }


    /**
     * 初始化view
     */
    private void initView() {
        upLoading = findViewById(R.id.uploading);
        downLoad = findViewById(R.id.download);
    }


    /**
     * 下载到本地
     *
     * @param body 内容
     * @return 成功或者失败
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            //判断文件夹是否存在
            File files = new File(SD_HOME_DIR);
            if (!files.exists()) {
                //不存在就创建出来
                files.mkdirs();
            }
            //创建一个文件
            File futureStudioIconFile = new File(SD_HOME_DIR + "download.jpg");
            //初始化输入流
            InputStream inputStream = null;
            //初始化输出流
            OutputStream outputStream = null;

            try {
                //设置每次读写的字节
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                //请求返回的字节流
                inputStream = body.byteStream();
                //创建输出流
                outputStream = new FileOutputStream(futureStudioIconFile);
                //进行读取操作
                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    //进行写入操作
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                }
                //刷新
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    //关闭输入流
                    inputStream.close();
                }

                if (outputStream != null) {
                    //关闭输出流
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
