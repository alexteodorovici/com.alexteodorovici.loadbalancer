package com.alexteodorovici.loadbalancer.ui.main

import androidx.lifecycle.ViewModel
import com.alexteodorovici.loadbalancer.data.model.ServerInstance
import com.alexteodorovici.loadbalancer.data.repository.LoadBalancerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LoadBalancerRepository
): ViewModel() {

    private val _servers = MutableStateFlow<List<ServerInstance>>(emptyList())
    val servers: StateFlow<List<ServerInstance>> = _servers

    private val _selectedServer = MutableStateFlow<ServerInstance?>(null)
    val selectedServer: StateFlow<ServerInstance?> = _selectedServer

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun registerServer(id: String, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(id.isEmpty() || url.isEmpty()) {
                _error.value = "ID and URL cannot be empty"
                return@launch
            }

            repository.registerServer(ServerInstance(id, url))
            _servers.value = repository.getAllServers()
            _error.value = null
        }
    }

    fun deregisterServer(id: String, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deregisterServer(ServerInstance(id, url))
            _servers.value = repository.getAllServers()
        }
    }

    fun selectNextServer() {
        viewModelScope.launch {
            val server = repository.getNextServer()
            if(server != null) {
                _selectedServer.value = server
            } else {
                _error.value = "No servers available"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}