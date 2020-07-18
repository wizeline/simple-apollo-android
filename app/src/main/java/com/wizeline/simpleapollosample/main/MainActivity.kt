package com.wizeline.simpleapollosample.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wizeline.simpleapollosample.R
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
