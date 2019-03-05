package com.entor.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XieTiansheng on 2018/3/7.
 */

public class HttpUtil {
    private static OkHttpClient okHttpClient;
    private static int num = 0;
    
    static{
    	okHttpClient = new OkHttpClient.Builder()
    			.readTimeout(1, TimeUnit.SECONDS)
    			.connectTimeout(1, TimeUnit.SECONDS)
    			.build();
    }
    
    
    public static String get(String path){
    	//�������ӿͻ���
        Request request = new Request.Builder()
                .url(path)
                .build();
        //����"����" ����
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();//ִ��
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
        	System.out.println("���Ӹ�ʽ����:"+path);
        }
        return null;
    }
    
}