package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidRadarAdapter
import com.udacity.asteroidradar.AsteroidRadarClickListener
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container,false) as FragmentMainBinding
      val manager= LinearLayoutManager(activity)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel



        val  adapter= AsteroidRadarAdapter  ( AsteroidRadarClickListener{
            viewModel.onAsteroidClicked(asteroid = it)
        })
        binding.asteroidRecycler.layoutManager= manager
        binding.asteroidRecycler.adapter= adapter


viewModel.image.observe(viewLifecycleOwner, Observer {
    Picasso.with(this.context)
        .load(it.url)
        .into(binding.activityMainImageOfTheDay)
})

       viewModel.newAsteroids.observe(viewLifecycleOwner, Observer {

           it?.let {
               adapter.submitList(it)
           }
       })

        viewModel.getAsteroid.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
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
            R.id.view_week_asteroids_menu -> viewModel.onViewWeekAsteroidsClicked()
            R.id.view_today_asteroids_menu -> viewModel.onTodayAsteroidsClicked()
            R.id.view_saved_asteroids_menu -> viewModel.onSavedAsteroidsClicked()
        }
        return true
    }
}
