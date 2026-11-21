package hu.toliver.vinotes.ui.screen.dashboard

import android.content.Context
import hu.toliver.vinotes.domain.model.DashboardStats
import hu.toliver.vinotes.domain.model.TasteWithWine
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.model.enums.ThemeMode
import hu.toliver.vinotes.domain.usecases.catalog.PerformFullImportUseCase
import hu.toliver.vinotes.domain.usecases.settings.GetAppPreferencesUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveWelcomeShownUseCase
import hu.toliver.vinotes.domain.usecases.stats.GetDashboardStatsUseCase
import hu.toliver.vinotes.domain.usecases.taste.GetRecentTastingsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val recentTastings = MutableStateFlow<List<TasteWithWine>>(emptyList())
    private val dashboardStats = MutableStateFlow(DashboardStats(0, 0, 0.0, ""))
    private val appPrefs = MutableStateFlow(defaultPrefs())

    private val getRecentTastings: GetRecentTastingsUseCase = mockk()
    private val getDashboardStats: GetDashboardStatsUseCase = mockk()
    private val getAppPreferences: GetAppPreferencesUseCase = mockk()
    private val context: Context = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getRecentTastings(any()) } returns recentTastings
        every { getDashboardStats() } returns dashboardStats
        every { getAppPreferences() } returns appPrefs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun clearsLoadingWhenSourcesEmitEmptyData() = runTest {
        val viewModel = createViewModel()

        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.recentTastings.isEmpty())
        assertEquals(0, viewModel.state.value.totalWines)
    }

    @Test
    fun refreshDoesNotResubscribeAndClearsAfterDataWipe() = runTest {
        dashboardStats.value = DashboardStats(2, 3, 4.5, "Tokaj")
        val viewModel = createViewModel()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals(2, viewModel.state.value.totalWines)

        repeat(3) {
            viewModel.onEvent(DashboardEvent.RefreshRequested)
        }

        recentTastings.value = emptyList()
        dashboardStats.value = DashboardStats(0, 0, 0.0, "")

        assertFalse(viewModel.state.value.isLoading)
        assertEquals(0, viewModel.state.value.totalWines)
        verify(exactly = 1) { getRecentTastings(any()) }
        verify(exactly = 1) { getDashboardStats() }
        verify(exactly = 1) { getAppPreferences() }
    }

    private fun createViewModel(): DashboardViewModel =
        DashboardViewModel(
            context = context,
            getRecentTastings = getRecentTastings,
            getDashboardStats = getDashboardStats,
            getAppPreferences = getAppPreferences,
            saveWelcomeShown = mockk(relaxed = true),
            performFullImport = mockk(relaxed = true),
        )

    private fun defaultPrefs() =
        GetAppPreferencesUseCase.Prefs(
            catalogUrl = "https://example.com/catalog",
            username = "tester",
            themeMode = ThemeMode.SYSTEM,
            appLanguage = AppLanguage.ENGLISH,
            welcomeShown = true,
        )
}