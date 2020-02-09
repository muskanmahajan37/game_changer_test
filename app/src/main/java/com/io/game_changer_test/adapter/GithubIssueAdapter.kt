package com.io.game_changer_test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.io.game_changer_test.R
import com.io.game_changer_test.activity.GithubIssueActivity
import com.io.game_changer_test.clickInterface.OnClickAdapter
import com.io.game_changer_test.model.issueModel.GithubIssueModel
import com.io.game_changer_test.utility.DateUtility

class GithubIssueAdapter(
    var data: ArrayList<GithubIssueModel>?,
    var activity: GithubIssueActivity,
    onPositionClick: OnClickAdapter
) :RecyclerView.Adapter<GithubIssueAdapter.ViewHolder>()
{
    private val positionClick: OnClickAdapter = onPositionClick
    private var body:String? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GithubIssueAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_item_issue,p0,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.tvIssueTitle.text = "Title-: "+data?.get(p1)?.title
        if(data?.get(p1)?.body?.length!! >= 40){
            body = data?.get(p1)?.body?.substring(0,40) +"..."
        }else{
            if (data?.get(p1)?.body.equals("")){
                body = "Body - Not Found"
            }else{
                body = data?.get(p1)?.body
            }

        }

        holder.tvIsssuedesc.text = body
        holder.tvDate.setText(DateUtility.timeStampaTimeago(data?.get(p1)?.updated_at!!))
        holder.itemView.setOnClickListener {
            positionClick.onPositionClick(data?.get(p1)?.number!!, 2)
        }
    }
    override fun getItemCount(): Int {
        return data?.size!!
    }




    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

        val tvDate : TextView = itemView.findViewById(R.id.tvDate)
        val tvIssueTitle : TextView = itemView.findViewById(R.id.tvIssueTitle)
        val tvIsssuedesc : TextView = itemView.findViewById(R.id.tvIsssuedesc)

    }
}

