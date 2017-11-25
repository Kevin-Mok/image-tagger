package fx.imgur;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Adapted from Johnny850807's GitHub repository
 * https://github.com/Johnny850807/Imgur-Picture-Uploading-Example-Using-Retrofit-On-Native-Java
 * on Nov 24th, 2017
 */
public interface ImgurAPI {
    String SERVER = "https://api.imgur.com";
    String AUTH = "997046bdff74b8b";

    @Headers("Authorization: Client-ID " + AUTH)
    @POST("/3/upload")
    Call<ImageResponse> postImage(
            @Body RequestBody image
    );

}