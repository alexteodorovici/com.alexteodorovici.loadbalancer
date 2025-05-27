package com.alexteodorovici.loadbalancer.ui.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.alexteodorovici.loadbalancer.databinding.ActivityMainBinding
import com.alexteodorovici.loadbalancer.ui.theme.LoadBalancerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Register server
        binding.buttonRegister.setOnClickListener {
            val id = binding.inputId.text.toString()
            val url = binding.inputUrl.text.toString()
            viewModel.registerServer(id, url)
        }

        // Deregister server
        binding.buttonDeregister.setOnClickListener {
            val id = binding.inputId.text.toString()
            val url = binding.inputUrl.text.toString()
            viewModel.deregisterServer(id, url)
        }

        // Select next server
        binding.buttonSelect.setOnClickListener {
            viewModel.selectNextServer()
        }

        // Observe servers list
        lifecycleScope.launchWhenStarted {
            viewModel.servers.collect { servers ->
                binding.serversContainer.removeAllViews()
                servers.forEach { server ->
                    val textView = TextView(this@MainActivity).apply {
//                        id = View.generateViewId()
                        id = View.generateViewId()
                        text = "${server.id}: ${server.url}"
                    }
                    binding.serversContainer.addView(textView)
                }
            }
        }

        // Observe selected server
        lifecycleScope.launchWhenStarted {
            viewModel.selectedServer.collect { server ->
                binding.selectedServerText.text = server?.let {
                    "Selected Server: ${it.id} - ${it.url}"
                } ?: "Selected Server: None"
            }
        }

        // Observe errors
        lifecycleScope.launchWhenStarted {
            viewModel.error.collect { error ->
                binding.errorText.text = error ?: ""
            }
        }
    }
}