//package com.example.petshield
//
//import android.content.ContentValues.TAG
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import com.kakao.sdk.auth.model.OAuthToken
//import com.kakao.sdk.common.model.ClientError
//import com.kakao.sdk.common.model.ClientErrorCause
//import com.kakao.sdk.user.UserApiClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class AuthCodeHandlerActivity : AppCompatActivity() {
//
//    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//        if (error != null) {
//            Log.e(TAG, "로그인 실패 $error")
//        } else if (token != null) {
//            Log.e(TAG, "로그인 성공 ${token.accessToken}")
//
//            // Ensure accessToken is non-null before making API call
//            val accessToken = token.accessToken
//            if (accessToken != null) {
//                // Replace with your API endpoint and correct parameters
//                RetrofitClientApi.instance.getKakaoToken(accessToken)
//                    .enqueue(object : Callback<KakaoTokenDto> {
//                        override fun onResponse(call: Call<KakaoTokenDto>, response: Response<KakaoTokenDto>) {
//                            if (response.isSuccessful) {
//                                val tokenDto = response.body()
//                                if (tokenDto != null) {
//                                    // Replace with your API endpoint and correct parameters
//                                    RetrofitClientApi.instance.loginWithKakao(tokenDto.accessToken)
//                                        .enqueue(object : Callback<LoginResponseDto> {
//                                            override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
//                                                if (response.isSuccessful) {
//                                                    val loginResponse = response.body()
//                                                    Log.d(TAG, "로그인 성공: ${loginResponse?.loginSuccess}")
//                                                    val intent = Intent(this@AuthCodeHandlerActivity, ProfileActivity::class.java)
//                                                    startActivity(intent)
//                                                    finish()
//                                                } else {
//                                                    Log.e(TAG, "로그인 실패: ${response.message()}")
//                                                }
//                                            }
//
//                                            override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
//                                                Log.e(TAG, "로그인 실패: ${t.message}")
//                                            }
//                                        })
//                                }
//                            } else {
//                                Log.e(TAG, "토큰 요청 실패: ${response.message()}")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<KakaoTokenDto>, t: Throwable) {
//                            Log.e(TAG, "토큰 요청 실패: ${t.message}")
//                        }
//                    })
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Check if KakaoTalk is available
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
//                if (error != null) {
//                    Log.e(TAG, "로그인 실패 $error")
//                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        return@loginWithKakaoTalk
//                    } else {
//                        UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
//                    }
//                } else if (token != null) {
//                    Log.e(TAG, "로그인 성공 ${token.accessToken}")
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
//        }
//    }
//}
