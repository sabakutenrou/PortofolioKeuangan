package com.example.portfoliokeuangan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.portfoliokeuangan.ui.screens.PortfolioScreen
import com.example.portfoliokeuangan.ui.theme.PortfolioKeuanganTheme
import com.example.portfoliokeuangan.ui.viewmodel.PortfolioViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<PortfolioViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PortfolioViewModel(this@MainActivity) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PortfolioKeuanganTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PortfolioScreen(viewModel)

                }
            }
        }
    }
}