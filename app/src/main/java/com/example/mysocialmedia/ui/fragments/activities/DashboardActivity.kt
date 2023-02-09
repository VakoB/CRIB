package com.example.mysocialmedia.ui.fragments.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.mysocialmedia.R
import com.example.mysocialmedia.ui.fragments.DashboardFragment
import com.example.mysocialmedia.ui.fragments.HomeFragment
import com.example.mysocialmedia.ui.fragments.NotificationsFragment
import com.example.mysocialmedia.ui.fragments.SettingsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        replaceFragment(HomeFragment())
        val navView: BottomNavigationView = findViewById(R.id.bottomNavView)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        navView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_dashboard -> replaceFragment(DashboardFragment())
                R.id.navigation_notifications -> replaceFragment(NotificationsFragment())
                R.id.navigation_settings -> replaceFragment(SettingsFragment())

                else ->{

                }
            }
            true
        }


        fab.setOnClickListener{
            startActivity(Intent(this,PostActivity::class.java))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}