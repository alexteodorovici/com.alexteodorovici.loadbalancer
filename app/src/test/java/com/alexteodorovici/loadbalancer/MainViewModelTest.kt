package com.alexteodorovici.loadbalancer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alexteodorovici.loadbalancer.data.repository.LoadBalancerRepository
import com.alexteodorovici.loadbalancer.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: LoadBalancerRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = LoadBalancerRepository()
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registerServer adds server to list`() = runTest {
        viewModel.registerServer("1", "http://server1.com")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, viewModel.servers.first().size)
        assertEquals("1", viewModel.servers.first()[0].id)
    }

    @Test
    fun `deregisterServer removes server from list`() = runTest {
        viewModel.registerServer("1", "http://server1.com")
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.deregisterServer("1", "http://server1.com")
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.servers.first().isEmpty())
    }

    @Test
    fun `selectNextServer returns correct server`() = runTest {
        viewModel.registerServer("1", "http://server1.com")
        viewModel.registerServer("2", "http://server2.com")
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.selectNextServer()
        testDispatcher.scheduler.advanceUntilIdle()
        val selected = viewModel.selectedServer.first()
        assertNotNull(selected)
        assertTrue(selected?.id in listOf("1", "2"))
    }
}