package com.qiaoyang.www.retrofitdownloadanduploading;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * 创建用于描述网络请求的接口
 *
 * @author joe
 */
public interface Api {
    /**
     * 上传
     * Multipart 表示多表单上传,
     *
     * @param partList 表单信息
     * @return .
     */
    @Multipart
    @POST("你的地址")
    Call<BaseBean> upLoading(@Part List<MultipartBody.Part> partList);

    /**
     * 下载文件
     * 如果下载大文件的一定要加上   @Streaming  注解
     *
     * @param fileUrl 文件的路径
     * @return 请求call
     */
    @GET
    Call<ResponseBody> download(@Url String fileUrl);
}
