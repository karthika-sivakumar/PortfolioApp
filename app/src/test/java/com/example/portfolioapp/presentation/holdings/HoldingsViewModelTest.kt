package com.example.portfolioapp.presentation.holdings

import com.example.portfolioapp.domain.model.Holding
import com.example.portfolioapp.domain.model.Transaction
import com.example.portfolioapp.domain.model.TransactionType
import com.example.portfolioapp.domain.repository.PortfolioRepository
import com.example.portfolioapp.presentation.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HoldingsViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeHolding(symbol: String = "TEST") = Holding(
        symbol = symbol,
        name = "Test Corp",
        quantity = 10.0,
        averageCost = 100.0,
        currentPrice = 120.0,
        currency = "INR"
    )

    @Test
    fun state_becomesSuccess_whenHoldingsExist() = runTest {
        val vm = HoldingsViewModel(FakeRepo(flowOf(listOf(makeHolding("TCS")))))
        advanceUntilIdle()

        assertTrue(vm.state.value.uiState is UiState.Success)
    }

    @Test
    fun state_becomesEmpty_whenNoHoldings() = runTest {
        val vm = HoldingsViewModel(FakeRepo(flowOf(emptyList())))
        advanceUntilIdle()

        assertEquals(UiState.Empty, vm.state.value.uiState)
    }

    @Test
    fun state_becomesError_whenManualRefreshFails() = runTest {
        val vm = HoldingsViewModel(FakeRepo(flowOf(emptyList()), shouldThrow = true))

        vm.refreshData()
        advanceUntilIdle()

        assertTrue(vm.state.value.uiState is UiState.Error)
    }

    @Test
    fun isRefreshing_resetsToFalse_afterRefreshCompletes() = runTest {
        val vm = HoldingsViewModel(FakeRepo(flowOf(listOf(makeHolding()))))

        vm.refreshData()
        advanceUntilIdle()

        assertFalse(vm.state.value.isRefreshing)
    }

    @Test
    fun isRefreshing_resetsToFalse_evenWhenRefreshFails() = runTest {
        val vm = HoldingsViewModel(FakeRepo(flowOf(emptyList()), shouldThrow = true))

        vm.refreshData()
        advanceUntilIdle()

        assertFalse(vm.state.value.isRefreshing)
    }

    @Test
    fun state_updatesReactively_onNewEmissions() = runTest {
        val sharedFlow = MutableSharedFlow<List<Holding>>(replay = 1)
        val vm = HoldingsViewModel(FakeRepo(sharedFlow))

        sharedFlow.emit(emptyList())
        advanceUntilIdle()
        assertEquals(UiState.Empty, vm.state.value.uiState)

        sharedFlow.emit(listOf(makeHolding("INFY")))
        advanceUntilIdle()

        val state = vm.state.value.uiState
        assertTrue(state is UiState.Success)
        assertEquals("INFY", (state as UiState.Success).data[0].symbol)
    }

    // --- Fake Repository ---

    private class FakeRepo(
        private val flow: Flow<List<Holding>>,
        private val shouldThrow: Boolean = false
    ) : PortfolioRepository {

        override fun getHoldings(): Flow<List<Holding>> = flow

        override suspend fun refreshHoldings() {
            if (shouldThrow) throw Exception("Refresh failed")
        }

        override fun getTransactions(
            symbol: String,
            filter: TransactionType?
        ): Flow<List<Transaction>> = flowOf(emptyList())
    }
}