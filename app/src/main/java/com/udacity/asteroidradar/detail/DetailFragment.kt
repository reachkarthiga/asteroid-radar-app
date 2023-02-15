package com.udacity.asteroidradar.detail


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.main.MainViewModel
import kotlinx.android.synthetic.main.au_helper.*

class DetailFragment : Fragment() {

    val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner


        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        binding.asteroid = asteroid

        binding.helpButton.setOnClickListener {
            viewModel.onHelperButtonClick()
        }

        viewModel.helperClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                displayAstronomicalUnitExplanationDialog()
            }
        })

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {

        val builder = Dialog(requireActivity())
        builder.setContentView(R.layout.au_helper)
        builder.ok.setOnClickListener {
            builder.cancel()
            viewModel.onDoneShowingHelper()
        }
        builder.create()
        builder.show()

    }
}
