package com.alexteodorovici.loadbalancer.data.repository

import com.alexteodorovici.loadbalancer.data.model.ServerInstance
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

class LoadBalancerRepository {

    private val servers = CopyOnWriteArrayList<ServerInstance>()
    private val index = AtomicInteger(0)

    fun registerServer(server: ServerInstance) {
        servers.add(server)
    }

    fun deregisterServer(server: ServerInstance){
        servers.remove(server)
    }

    fun getNextServer(): ServerInstance? {
        if(servers.isEmpty()) {
            return null
        }
        val server = servers[index.getAndIncrement() % servers.size]
        return server
    }

    fun getAllServers(): List<ServerInstance> {
        return servers.toList()
    }
}