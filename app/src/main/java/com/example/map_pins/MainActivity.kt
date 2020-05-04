package com.example.map_pins

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(ArFragment())

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.bottom_navigation) as BottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener(
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when (item.getItemId()) {
                        R.id.action_add -> {
                            loadFragment(AddPinFragment())
                        }
                        R.id.action_ar -> {
                            loadFragment(ArFragment())
                        }
                        R.id.action_profile -> {
                            loadFragment(ProfileFragment())
                        }
                    }
                    return true
                }
            })
    }

    private fun loadFragment(fragment : Fragment) : Boolean {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }

}
