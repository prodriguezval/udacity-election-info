package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.ui.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : BaseFragment() {

    private val navController by lazy { findNavController() }
    lateinit var binding: FragmentElectionBinding
    override val viewModel: ElectionsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val upcomingElectionAdapter = ElectionListAdapter(ElectionListener { election ->
            navController.navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election
                )
            )
        })

        binding.rvUpcomingElections.adapter = upcomingElectionAdapter
        viewModel.upcomingElections.observe(viewLifecycleOwner) { elections ->
            Log.i("ViewModel", elections.size.toString())
            upcomingElectionAdapter.submitList(elections)
        }


        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}