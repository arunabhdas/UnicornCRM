package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import app.unicornapp.unicorncrm.presentation.CoinViewModel
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.ui.composables.PullToRefreshLazyColumn
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun RatesScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    viewModel: CoinViewModel = koinViewModel<CoinViewModel>(),

) {
    val coins by viewModel.coinList.collectAsStateWithLifecycle(initialValue = emptyList())
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(initialValue = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            PullToRefreshLazyColumn(
                items = coins,
                content = { coin ->
                    CoinCard(coin)
                    Spacer(modifier = Modifier.height(8.dp))
                },
                isRefreshing = isLoading,
                onRefresh = { viewModel.refreshCoins() },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 56.dp)
            )
        }
    }
}

@Composable
fun CoinCard(coin: CoinPaprikaCoin) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = coin.name)
            Text(text = "Symbol: ${coin.symbol}")
        }
    }
}


@Preview
@Composable
fun RatesScreenPreview() {
   RatesScreen(
       navController = rememberNavController(),
       navigator = MockDestinationsNavigator()
   )
}