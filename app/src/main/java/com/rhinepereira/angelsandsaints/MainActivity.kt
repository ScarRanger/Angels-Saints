package com.rhinepereira.angelsandsaints

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.rhinepereira.angelsandsaints.data.remote.RetrofitClient
import com.rhinepereira.angelsandsaints.data.repository.RemoteContentRepository
import com.rhinepereira.angelsandsaints.navigation.NavGraph
import com.rhinepereira.angelsandsaints.ui.theme.AngelsAndSaintsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = RetrofitClient.getApi(applicationContext)
        val repository = RemoteContentRepository(api)

        setContent {
            AngelsAndSaintsTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    repository = repository
                )
            }
        }
    }
}
