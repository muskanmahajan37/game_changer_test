package com.io.game_changer_test.utility

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.io.game_changer_test.model.issueModel.GithubIssueModel
import org.json.JSONObject

class ApiResponseErrorClass {

    companion object errorModel{

        fun getErrorModel(jsonObjectError: JSONObject, str: String): Any? {
            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()

            try {
                when (str) {
                    "githubIssueModel" -> {
                        return gson.fromJson(jsonObjectError.toString(), GithubIssueModel::class.java)
                    }
                    else -> {
                        return null
                    }

                }
            } catch (e: java.lang.Exception) {

                return null
                e.printStackTrace()
            }
        }
    }
}