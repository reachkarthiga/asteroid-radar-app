package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.DatabaseAsteroid
import com.udacity.asteroidradar.model.asDomainModel
import com.udacity.asteroidradar.roomDataBase.Dao
import com.udacity.asteroidradar.roomDataBase.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    lateinit var dao :Dao
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val database  = Database.getInstance(requireContext())
        dao = database.dao
        val viewModelFactory: MainViewModelFactory = MainViewModelFactory(dao)
        val viewModel: MainViewModel by viewModels{
            viewModelFactory
        }
        binding.viewModel = viewModel


        val adapter = AsteroidsAdapter(ClickListener {
            viewModel.onAsteroidItemClick(it)
        })

        binding.asteroidRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneDetailNavigation()
            }
        })

        viewModel.pictureOfTheDay.observe(viewLifecycleOwner, Observer {
            if (it.url != null && it.mediaType == Constants.MEDIA_TYPE_IMAGE) {
                Picasso.with(context).load(it.url).fit().into(binding.activityMainImageOfTheDay)

            }
        })

        viewModel.asteroidsList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
//            R.id.show_all -> viewModel.weekFilter()
//            R.id.show_today -> viewModel.todayFilter()
//            R.id.show_week -> viewModel.weekFilter()
//            else -> viewModel.getAsteroidListFromDB()
        }
        return true
    }
}
