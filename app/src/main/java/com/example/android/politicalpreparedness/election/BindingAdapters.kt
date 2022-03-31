package com.example.android.politicalpreparedness.election

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