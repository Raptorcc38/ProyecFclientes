// MainActivity.kt
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

        // Recupera el token guardado y actualiza el TokenManager
        val token = Preferencias.obtenerToken(this)
        TokenManager.token = token

        // Accede al NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Si hay token, navega a categorías, sino a login
        if (!token.isNullOrEmpty()) {
            // Quita toda la backstack para que no se pueda volver atrás al login
            navController.setGraph(R.navigation.nav_graph)
            navController.navigate(R.id.categoriasFragment)
        }
        // Si no hay token, se queda en loginFragment (startDestination)
    }
}
