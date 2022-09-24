package com.example.projectta.API;

import com.example.projectta.Response.ResponseAdmin;
import com.example.projectta.Response.ResponseAdmins;
import com.example.projectta.Response.ResponseDonatur;
import com.example.projectta.Response.ResponseDonaturDetail;
import com.example.projectta.Response.ResponseDonaturFilter;
import com.example.projectta.Response.ResponseFIle;
import com.example.projectta.Response.ResponseKey;
import com.example.projectta.Response.ResponseUbahAdmin;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIRequestData {

    @FormUrlEncoded
    @POST("loginapi/login")
    Call<ResponseAdmin> postLogin(
            @Field("username") String username,
            @Field("password") String password,
            @Field("key") String key
    );

    @GET("donaturapi/donaturs")
    Call<ResponseDonatur> getDataDonatur(
            @Header("token") String token,
            @Header("key") String key
    );

    @GET("donaturapi/donatur_detail/{id}/{key}")
    Call<ResponseDonaturDetail> getDataDonaturDetail(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Path("key") String key,
            @Path("id") int id
    );

    @FormUrlEncoded
    @PATCH("donaturapi/donatur_action/{id}")
    Call<ResponseDonaturDetail> ActionDataDonasi(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Field("action") String action,
            @Path("id") int id
    );

    @GET("donaturapi/checkKey/{id}/{key}")
    Call<ResponseKey> CheckKey(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Path("key") String key,
            @Path("id") int id
    );

    @FormUrlEncoded
    @PATCH("donaturapi/changeKey/{id}")
    Call<ResponseDonaturDetail> ChangeKey(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Field("key_lama") String key_lama,
            @Field("key_baru") String key_baru,
            @Path("id") int id
    );

    @GET("donaturapi/get_data_by_filter/{key}/{from}/{to}/{opsi}")
    Call<ResponseDonaturFilter> GetDataFilter(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Path("key") String key,
            @Path("from") String from,
            @Path("to") String to,
            @Path("opsi") String opsi
    );

    @Multipart
    @POST("donaturapi/donatur_post")
    Call<ResponseDonaturDetail> InsertDataDonatur(
            @Part MultipartBody.Part bukti_transfer,
            @Part("token") RequestBody token,
            @Part("key_token") RequestBody key_token,
            @Part("no_rekening") RequestBody no_rekening,
            @Part("pemilik_rekening") RequestBody pemilik_rekening,
            @Part("organisasi") RequestBody organisasi,
            @Part("jumlah") RequestBody jumlah,
            @Part("gmail") RequestBody gmail,
            @Part("pesan") RequestBody pesan,
            @Part("tanggal_donate") RequestBody tanggal_donate,
            @Part("opsi") RequestBody opsi,
            @Part("key") RequestBody key
    );

    @Multipart
    @POST("fileapi/insert_file")
    Call<ResponseFIle> InsertDataFile(
            @Part MultipartBody.Part file,
            @Part("token") RequestBody token,
            @Part("key_token") RequestBody key_token,
            @Part("keterangan") RequestBody keterangan,
            @Part("key") RequestBody key
    );

    @GET("fileapi/files")
    Call<ResponseFIle> getDataFiles(
            @Header("token") String token,
            @Header("key") String key
    );

    @GET("fileapi/getFileById/{id}")
    Call<ResponseFIle> GetFilebyId(
            @Header("token") String token,
            @Header("key") String key,
            @Path("id") int id
    );

    @FormUrlEncoded
    @PATCH("fileapi/enkrip_file/{id}")
    Call<ResponseFIle> enkripData(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Field("key") String key,
            @Path("id") int id
    );

    @FormUrlEncoded
    @PATCH("fileapi/dekrip_file/{id}")
    Call<ResponseFIle> dekripData(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Field("key") String key,
            @Path("id") int id
    );

    @GET("adminapi/admins")
    Call<ResponseAdmins> getAllAdmin(
            @Header("token") String token,
            @Header("key") String key
    );

    @FormUrlEncoded
    @POST("adminapi/insert_data")
    Call<ResponseAdmins> insertDataAdmin(
            @Field("token") String token,
            @Field("key_token") String key_token,
            @Field("key") String key,
            @Field("nama") String nama,
            @Field("username") String username,
            @Field("password") String password,
            @Field("level") String level,
            @Field("access") String access
    );

    @GET("adminapi/getDataById/{id}")
    Call<ResponseAdmins> getDetailAdmin(
            @Header("token") String token,
            @Header("key") String key,
            @Path("id") int id
    );

    @DELETE("adminapi/hapusData/{id}")
    Call<ResponseAdmins> hapusData(
            @Header("token") String token,
            @Header("key") String key,
            @Path("id") int id
    );

    @DELETE("adminapi/destroy_access/{id}")
    Call<ResponseAdmins> hapusAccess(
            @Header("token") String token,
            @Header("key") String key,
            @Path("id") int id
    );

    @FormUrlEncoded
    @PATCH("adminapi/give_access/{id}")
    Call<ResponseAdmins> beriAccess(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Field("key") String key,
            @Path("id") int id
    );

    @FormUrlEncoded
    @PATCH("adminapi/update_data/{id}")
    Call<ResponseUbahAdmin> updateData(
            @Header("token") String token,
            @Header("key_token") String key_token,
            @Path("id") int id,
            @Field("nama") String nama,
            @Field("username") String username,
            @Field("password") String password,
            @Field("key") String key
    );
}
