package com.io.game_changer_test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.io.game_changer_test.R
import com.io.game_changer_test.adapter.GithubIssueAdapter
import com.io.game_changer_test.clickInterface.OnClickAdapter
import com.io.game_changer_test.localstorage.LocalStorage
import com.io.game_changer_test.model.issueModel.GithubIssueModel
import com.io.game_changer_test.utility.Common
import com.io.game_changer_test.viewmodel.AuthorizationViewModel
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class GithubIssueActivity : AppCompatActivity(), OnClickAdapter {
    private var date: String? = null
    private var newArray: ArrayList<GithubIssueModel>? = null
    private var issueRecyclerView: RecyclerView? = null
    private var githubIssueAdapter: GithubIssueAdapter? = null
    private lateinit var authorizationViewModel: AuthorizationViewModel
    private var localStorage: LocalStorage? = null
    var gson: Gson? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        //startInternetServices()
        getCurrentDate()
        checkForDateForApiCall()
        callApiToGetAllIssue()
    }

/*    private fun startInternetServices() {
        if( PermissionChecker.isPermissionGranted!!){
            Common.showShortToast("Permission Granted",this)
            checkFoRInterNetServices()
        }else{
            Common.showShortToast("Permission Rejected",this)
            PermissionChecker.showSettingsDialog(this,"Need Permissions",
                "This app needs permission to use this feature. You can grant them in app settings.",
                "GOTO SETTINGS","Cancel",1)
        }
    }*/

    private fun checkForDateForApiCall() {
        try {
            var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            var diffInMillisec =
                sdf.parse(date).time - sdf.parse(localStorage!!.getString(LocalStorage.lastApiCallDate)).time
            print(
                diffInMillisec / 60 * 60
            )
            var hours = TimeUnit.MILLISECONDS.toHours(diffInMillisec)
            if (hours >= 24) {
                callApiToGetAllIssue()
                Common.showLongToast("Data Updated", this)
            } else {
                prepareRecyclerViewData(getData())
                Common.showLongToast("Data Will Updated After 24 hour", this)
            }
        } catch (e: java.lang.Exception) {

        }
    }

    private fun getCurrentDate() {
        var timeStamp =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Calendar.getInstance().time)
        date = timeStamp
    }

    private fun initView() {
        setContentView(R.layout.activity_splash)
        gson = Gson()
        localStorage = LocalStorage(this)
        issueRecyclerView = findViewById(R.id.issueRecyclerView)

    }

  /*  private fun checkFoRInterNetServices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJob()
        } else {
            val startServiceIntent = Intent(this, InternetServices::class.java)
            startService(startServiceIntent)
        }
    }*/

   /* override fun onStart() {
        super.onStart()
        // Start service and provide it a way to communicate with this class.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJob()
        } else {
            val startServiceIntent = Intent(this, InternetServices::class.java)
            startService(startServiceIntent)
        }
    }*/


    /*override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJob()
        } else {
            val startServiceIntent = Intent(this, InternetServices::class.java)
            startService(startServiceIntent)
        }
    }*/


    /*@TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJob() {
        val myJob = JobInfo.Builder(0, ComponentName(this, InternetServices::class.java))
            .setRequiresCharging(true)
            .setMinimumLatency(1000)
            .setOverrideDeadline(2000)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .build()
        val jobScheduler =
            getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(myJob)


    }
*/
 /*   override fun onDestroy() {
        super.onDestroy()
        val startServiceIntent = Intent(this, InternetServices::class.java)
        stopService(startServiceIntent)
    }*/

    // Sample Test Api TO Call

    private fun callApiToGetAllIssue() {
        authorizationViewModel = ViewModelProviders.of(this).get(AuthorizationViewModel::class.java)
        authorizationViewModel.callApiToGetIssue(
        )
        authorizationViewModel.getGithubIssueData()?.observe(this@GithubIssueActivity, Observer {

            if (it != null) {
                saveDataToLocalStorage(it)
                prepareRecyclerViewData(it)
            } else {
                try {
                    Common.showShortToast(
                        "Something Went Wrong",
                        this@GithubIssueActivity
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })

    }

    private fun saveDataToLocalStorage(data: ArrayList<GithubIssueModel>?) {
        localStorage!!.putString(LocalStorage.lastApiCallDate, date.toString())
        localStorage!!.putString(LocalStorage.GITHUB_ISSUE_DATA, gson!!.toJson(sortData(data)))
    }

    private fun getData(): ArrayList<GithubIssueModel> {
        val type: Type = object : TypeToken<ArrayList<GithubIssueModel>?>() {}.type
        val gsonString: String = localStorage!!.getString(LocalStorage.GITHUB_ISSUE_DATA)!!
        var data: ArrayList<GithubIssueModel> = gson!!.fromJson(gsonString, type)
        return data
    }

    private fun prepareRecyclerViewData(data: ArrayList<GithubIssueModel>) {
        issueRecyclerView?.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        githubIssueAdapter =
            GithubIssueAdapter(sortData(data) as ArrayList<GithubIssueModel>?, this, this)
        issueRecyclerView!!.isNestedScrollingEnabled = false
        issueRecyclerView!!.adapter = githubIssueAdapter
    }

    private fun sortData(data: ArrayList<GithubIssueModel>?): List<GithubIssueModel>? {
        newArray = ArrayList()
        var dummyStraing = ArrayList<String>()
        for (i in data?.indices!!) {
            dummyStraing.add(data.get(i).updated_at)
        }
        dummyStraing.sort()
        Collections.sort(
            dummyStraing,
            Collections.reverseOrder()
        )
        for (i in dummyStraing.indices) {
            for (j in data.indices) {
                if (dummyStraing.get(i).equals(data.get(j).updated_at)) {
                    newArray?.add(data.get(j))
                }
            }
        }
        return newArray
    }

    override fun onPositionClick(variable: Int, isChecked: Int) {
        Common.showLongToast("Id Of This issue is$variable", this)
    }
}
