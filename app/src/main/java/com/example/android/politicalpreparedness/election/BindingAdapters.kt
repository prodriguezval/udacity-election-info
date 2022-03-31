package com.example.android.politicalpreparedness.election

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.model.VoterInfo

@BindingAdapter("electionInfoTitle")
fun bindElectionInfoTitleText(view: TextView, voterInfo: VoterInfo?) {
    voterInfo?.run {
        view.text = view.resources.getString(R.string.text_election_info, stateName)
    }
}

@BindingAdapter("followButtonText")
fun bindFollowButtonText(button: Button, isElectionSaved: Boolean?) {
    if (isElectionSaved != null) {
        if (isElectionSaved) {
            button.text = button.resources.getString(R.string.text_unfollow_election)
        } else {
            button.text = button.resources.getString(R.string.text_follow_election)
        }
    } else {
        button.text = ""
    }
}