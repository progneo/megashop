package me.progneo.megashop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.progneo.megashop.ui.component.navigation.NavGraph
import me.progneo.megashop.ui.theme.MegaShopTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MegaShopTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
