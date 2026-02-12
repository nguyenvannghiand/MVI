package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.User
import com.example.myapplication.model.UserIntent
import com.example.myapplication.model.UserState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()
        setupOnClickListeners()

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

                        is UserEffect.TrackEvent -> {
                            android.util.Log.d("MVI_DEBUG", "Tracking: ${effect.eventName}")
                        }
                    }
                }
        }
    }

    private fun setupOnClickListeners() {
        binding.infoUser.setOnClickListener {
            val user= viewModel.state.value.listUsers.firstOrNull()
            user?.let {
                lifecycleScope.launch {
                    viewModel.userIntent.send(UserIntent.ClickUser(user = it))
                }
            }

        }

        onBackPressedDispatcher.addCallback(this){
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                lifecycleScope.launch {
                    viewModel.userIntent.send(UserIntent.CloseDetail)
                }
            } else {
                finish()
            }
        }
    }


    private fun render(state: UserState){
        binding.fragmentContainer.isVisible = state.isDetailVisible
        binding.infoUser.isVisible = !state.isDetailVisible
        if (state.isDetailVisible && state.selectedUser != null) {
            // Chỉ thực hiện transaction nếu Fragment chưa tồn tại hoặc khác User
            showFragmentDetail(state.selectedUser)
        }
        state.error?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        android.util.Log.d("MVI_DEBUG", "Users: ${state.listUsers.size}")
    }

    private fun showFragmentDetail(user: User) {
        // Kiểm tra để tránh add đè nhiều Fragment giống nhau khi render chạy lại
        val currentFrag = supportFragmentManager.findFragmentByTag("UserDetail")
        if (currentFrag == null) {
            val fragment = UserDetailFragment.newInstance(user)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment, "UserDetail")
                .addToBackStack(null)
                .commit()
        }
    }
}