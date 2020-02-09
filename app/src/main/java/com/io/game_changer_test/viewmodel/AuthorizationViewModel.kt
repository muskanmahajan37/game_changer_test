package com.io.game_changer_test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.io.game_changer_test.model.issueModel.GithubIssueModel
import com.io.game_changer_test.repository.AuthorizationRepository


class AuthorizationViewModel : ViewModel(){

    private var authorizationRepository: AuthorizationRepository? = null
    var userModelData : MutableLiveData<ArrayList<GithubIssueModel>>? = null


    fun getGithubIssueData(): LiveData<ArrayList<GithubIssueModel>>? {
        return userModelData
    }

    fun callApiToGetIssue(
    ) {

        authorizationRepository = AuthorizationRepository().getInstance()
        userModelData = authorizationRepository!!.callApiToGetIssue()

    }



}