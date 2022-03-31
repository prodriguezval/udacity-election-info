package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.election.repository.ElectionsRepository
import com.example.android.politicalpreparedness.network.CivicsApi

class ElectionsFragment : Fragment() {

    private val navController by lazy { findNavController() }
    lateinit var binding: FragmentElectionBinding
    lateinit var viewModel: ElectionsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = ElectionsRepository(
            ElectionDatabase.getInstance(requireContext()),
            CivicsApi
        )
        val electionViewModelFactory =
            ElectionsViewModelFactory(requireActivity().application, repository)
        viewModel =
            ViewModelProvider(this, electionViewModelFactory)[ElectionsViewModel::class.java]

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
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
            upcomingElectionAdapter.submitList(elections)
        }


        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}