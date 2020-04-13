package com.kyrgyzcoder.moibiznesv04

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kyrgyzcoder.moibiznesv04.categories.*
import com.kyrgyzcoder.moibiznesv04.sign_in.SignInActivity
import com.kyrgyzcoder.moibiznesv04.utils.EXTRA_USERNAME
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, BothVseFragment()).commit()

        drawer = findViewById(R.id.drawer_layout_id)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerView = navigationView.getHeaderView(0)
        val textUserName = headerView.findViewById<TextView>(R.id.textViewUserName)
        val userEmail = getIntent().getStringExtra(EXTRA_USERNAME)
        if (userEmail != null)
            textUserName.text = userEmail
        val toggle =
            ActionBarDrawerToggle(this, drawer, toolbar_main, R.string.open_nav, R.string.close_nav)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_vse_tovary -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BothVseFragment()).commit()
                title = "Мой бизнес"
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_statistics -> {

            }
            R.id.nav_vesna -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, VesnaFragment()).commit()
                title = resources.getString(R.string.vesna)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_leto -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LetoFragment()).commit()
                title = resources.getString(R.string.leto)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_osen -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, OsenFragment()).commit()
                title = resources.getString(R.string.osen)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_shkolnyi -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ShkolnyiFragment()).commit()
                title = resources.getString(R.string.shkolnyi)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_zima -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ZimaFragment()).commit()
                title = resources.getString(R.string.zima)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_drugie -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DrugieFragment()).commit()
                title = resources.getString(R.string.drugie)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.action_sign_out -> {
                signOut()
            }
        }
        return true
    }

    private fun signOut() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogStyle))
        builder.setTitle("Внимание!").setMessage(getString(R.string.signout_message))
            .setPositiveButton("Выйти") { _, _ ->
                val intent = Intent(this, SignInActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }
        builder.setNegativeButton("Отменить") { _, _ ->
            Toast.makeText(this, "Отменено!", Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }
}
