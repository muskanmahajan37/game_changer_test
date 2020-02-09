package com.io.game_changer_test.repository

import androidx.lifecycle.MutableLiveData
import com.io.game_changer_test.api.AuthorizationApi
import com.io.game_changer_test.model.issueModel.GithubIssueModel
import com.io.game_changer_test.networking.RetrofitCommonClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthorizationRepository {

    private var authorizationRepository: AuthorizationRepository? = null
    private var authorizationApi: AuthorizationApi = RetrofitCommonClass.createService(AuthorizationApi::class.java)

    constructor()


    companion object

    fun getInstance(): AuthorizationRepository {
        if (authorizationRepository == null) {
            authorizationRepository = AuthorizationRepository()
        }
        return authorizationRepository as AuthorizationRepository
    }

    fun callApiToGetIssue(
    ): MutableLiveData<ArrayList<GithubIssueModel>> {
        val loginData = MutableLiveData<ArrayList<GithubIssueModel>>()
        authorizationApi.callApiToGetIssue(
        ).enqueue(object : Callback<ArrayList<GithubIssueModel>> {
            override fun onResponse(
                call: Call<ArrayList<GithubIssueModel>>,
                response: Response<ArrayList<GithubIssueModel>>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<GithubIssueModel>>, t: Throwable) {
                loginData.value = null
            }
        })
        return loginData
    }


}