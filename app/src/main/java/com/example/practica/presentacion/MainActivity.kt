package com.example.practica.presentacion


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.practica.R
import com.example.practica.controladores.adapters.ActivityController
import com.example.practica.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Binding
    lateinit var binding : ActivityMainBinding
    //ViewModel
    private val activityViewModel : ActivityController by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activityViewModel.currentFragment.observe(this, Observer {
            cambiarFragmento(it)
        })

        activityViewModel.currentTab.observe(this, Observer {
            binding.bottomNavigation.selectedItemId = it
        })

        binding.bottomNavigation.setOnItemSelectedListener { it ->
            activityViewModel.changeFragment(it.itemId)
        }

    }




    override fun onBackPressed() {
        if (activityViewModel.currentTab.value == R.id.navInicio) {
            super.onBackPressed()
        } else {
            activityViewModel.changeFragment(R.id.navInicio)
        }
    }

    fun cambiarFragmento(fragmento : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction
            .replace(binding.contenedorFragments.id, fragmento)
            .commit()
    }



}