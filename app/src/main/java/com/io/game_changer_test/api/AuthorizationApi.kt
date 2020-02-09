package com.io.game_changer_test.api

import com.io.game_changer_test.model.issueModel.GithubIssueModel
import retrofit2.Call
import retrofit2.http.*

interface AuthorizationApi {

    @GET("issues")
    fun callApiToGetIssue(
    ): Call<ArrayList<GithubIssueModel>>

}