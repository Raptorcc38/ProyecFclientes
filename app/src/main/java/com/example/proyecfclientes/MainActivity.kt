package com.example.proyecfclientes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.proyecfclientes.network.TokenManager
import com.example.proyecfclientes.utils.Preferencias

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val token = Preferencias.getToken(this)
        TokenManager.token = token


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        if (!token.isNullOrEmpty()) {

            navController.setGraph(R.navigation.nav_graph)
            navController.navigate(R.id.categoriasFragment)
        }
    }
}
