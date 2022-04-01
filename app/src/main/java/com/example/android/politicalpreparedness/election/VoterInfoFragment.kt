package com.example.android.politicalpreparedness.election

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.VoterInfoDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.election.repository.ElectionsRepository
import com.example.android.politicalpreparedness.election.repository.VoterInfoRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.google.android.material.snackbar.Snackbar


class VoterInfoFragment : Fragment() {

    lateinit var binding: FragmentVoterInfoBinding

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val electionRepository =
            ElectionsRepository(ElectionDatabase.getInstance(requireContext()), CivicsApi)
        val repository =
            VoterInfoRepository(VoterInfoDatabase.getInstance(requireContext()), CivicsApi)
        val voterInfoViewModelFactory =
            VoterInfoViewModelFactory(repository, electionRepository, requireActivity().application)
        viewModel =
            ViewModelProvider(this, voterInfoViewModelFactory)[VoterInfoViewModel::class.java]
        val arguments = VoterInfoFragmentArgs.fromBundle(requireArguments())
        viewModel.refresh(arguments.election)

        binding.viewModel = viewModel
        binding.stateLocations.setOnClickListener {
            val urlStr = viewModel.voterInfo.value?.votingLocationUrl
            urlStr?.run {
                startWebViewActivity(this)
            }
        }

        binding.stateBallot.setOnClickListener {
            val urlStr = viewModel.voterInfo.value?.ballotInformationUrl
            urlStr?.run {
                startWebViewActivity(this)
            }
        }

        return binding.root
    }

    private fun startWebViewActivity(urlStr: String) {
        val uri: Uri = Uri.parse(urlStr)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {

            val snack = Snackbar.make(
                requireView(),
                getString(R.string.msg_no_web_browser_found),
                Snackbar.LENGTH_LONG
            )
            snack.show()
        }
    }

}