package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.asDomainModel
import com.udacity.asteroidradar.repository.Repository
import com.udacity.asteroidradar.roomDataBase.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        val database = Database.getInstance(requireContext())
        val dao = database.dao
        val pictureDao = database.pictureDao

        val repository = Repository(dao, pictureDao )

        binding.viewModel = viewModel

        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val dateAfter1Week = Calendar.getInstance()
        dateAfter1Week.add(Calendar.DATE, 7)

        val adapter = AsteroidsAdapter(ClickListener {
            viewModel.onAsteroidItemClick(it)
        })

        binding.asteroidRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneDetailNavigation()
            }
        })

        viewModel.filterType.observe(viewLifecycleOwner, Observer {
            when (it) {
                mainPageFilter.SHOW_ALL.filterType -> {
                    repository.asteroids.observe(viewLifecycleOwner, Observer { completeList ->
                        if (completeList != null) {
                            adapter.submitList(completeList)
                        }
                    })
                }

                mainPageFilter.WEEK.filterType -> {
                    val weekAsteroids = Transformations.map(
                        dao.getWeekAsteroids
                            (sdf.format(today.time), sdf.format(dateAfter1Week.time))
                    ) { weekList ->
                        weekList.asDomainModel()
                    }

                    weekAsteroids.observe(viewLifecycleOwner, Observer {
                        adapter.submitList(it)
                    })

                }

                mainPageFilter.TODAY.filterType -> {
                    val todayAsteroids = Transformations.map(
                        dao.getTodayAsteroids
                            (sdf.format(today.time))
                    ) { todayList ->
                        todayList.asDomainModel()
                    }

                    todayAsteroids.observe(viewLifecycleOwner, Observer {
                        adapter.submitList(todayAsteroids.value)
                    })

                }

            }
        })

        repository.pictureOfTheDay.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.mediaType == Constants.MEDIA_TYPE_IMAGE) {
                    Picasso.with(context)
                        .load(it.url)
                        .fit()
                        .into(binding.activityMainImageOfTheDay)
                }

                binding.imageTitle.text = it.title
                binding.imageTitle.contentDescription = it.title
                binding.activityMainImageOfTheDay.contentDescription = it.title
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
        when (item.itemId) {
            R.id.show_all -> viewModel.showAllAsteroids()
            R.id.show_today -> viewModel.todayFilter()
            R.id.show_week -> viewModel.weekFilter()
            else -> viewModel.showAllAsteroids()
        }
        return true
    }
}
