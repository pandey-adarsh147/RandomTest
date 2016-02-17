package com.brahmand;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import okio.ByteString;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;

import java.io.IOException;

/**
 * Created by adarshpandey on 12/15/15.
 */
public class SslPinTest {

    public static void main(String... args) throws IOException {

        ByteString byteString = ByteString.decodeBase64("h3SuRfBIzHBG28j7YL/R1VaQBbQ=");
        System.out.println(byteString.sha256());
        API api = getBaseRetrofit().create(API.class);

        retrofit.Call<ResponseBody> ping = api.ping();

        System.out.println(ping.execute().body().string());
    }

    public interface API {
        @GET("/mobileapp/v4/pingconfig.json")
        retrofit.Call<ResponseBody> ping();
    }

    public static Retrofit getBaseRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.helpchat.in/")
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
    private static OkHttpClient okHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient();
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                .add("api.helpchat.in", "sha1/h3SuRfBIzHBG28j7YL/R1VaQBbQ=")
//                .add("api.helpchat.in", "sha1/tFVQFINFH+6MoKEM9a/eOkxeEVk=")
                .add("api.helpchat.in", "sha1/IQ8siffEzV0bgl441sZZO6aTde4=")
//                .add("api.helpchat.in", "sha1/7uWfHiqlRMPLJUOmmlvUaiW8u44=")
                .build();
        okHttpClient.setCertificatePinner(certificatePinner);

        // TODO :Handle for production env. use this in debug only
        okHttpClient.interceptors().add(logging);

        return okHttpClient;
    }

    public static class Data {
        public Track uninstall;
    }

    public static class Track {
        public String notification_title;
        public String notification_description;
        public String notification_pic_url;
    }
}
