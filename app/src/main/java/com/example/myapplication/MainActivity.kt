package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.model.UserIntent
import com.example.myapplication.model.UserState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()

        // Gửi Intent ban đầu
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Toast.makeText(this@MainActivity, "Fetch data", Toast.LENGTH_SHORT).show()
                viewModel.userIntent.send(UserIntent.FetchDataUsers)
            }

        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            // Sử dụng flowWithLifecycle để tránh memory leak
            viewModel.state.flowWithLifecycle(lifecycle = lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    render(state)
                }
        }
        lifecycleScope.launch {
            viewModel.effect.flowWithLifecycle(lifecycle = lifecycle, Lifecycle.State.STARTED)
                .collect { effect ->
                    when (effect) {
                        is UserEffect.ShowToast -> {
                            Toast.makeText(this@MainActivity, effect.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is UserEffect.NavigateToDetail -> {
                            // Logic chuyển màn hình
                        }
                    }

                }
        }
    }

    private fun render(state: UserState){
        state.error?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        android.util.Log.d("MVI_DEBUG", "Users: ${state.listUsers.size}")
    }
}