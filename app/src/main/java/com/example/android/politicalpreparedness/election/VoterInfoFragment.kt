package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.ui.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding


class VoterInfoFragment : BaseFragment() {

    lateinit var binding: FragmentVoterInfoBinding
    override val viewModel: VoterInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
        binding.lifecycleOwner = this

        val arguments = VoterInfoFragmentArgs.fromBundle(requireArguments())
        viewModel.refresh(arguments.election)

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values
        binding.viewModel = viewModel
        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root
    }

    //TODO: Create method to load URL intents

}