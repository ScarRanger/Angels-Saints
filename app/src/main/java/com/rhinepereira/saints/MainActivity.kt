package com.rhinepereira.saints

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.rhinepereira.saints.data.remote.RetrofitClient
import com.rhinepereira.saints.data.repository.RemoteContentRepository
import com.rhinepereira.saints.navigation.NavGraph
import com.rhinepereira.saints.ui.theme.AngelsAndSaintsTheme

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
