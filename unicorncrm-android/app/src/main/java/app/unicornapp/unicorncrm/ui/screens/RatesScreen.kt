package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import app.unicornapp.unicorncrm.presentation.CoinViewModel
import app.unicornapp.unicorncrm.presentation.CoinWithPrice
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.ui.composables.PullToRefreshLazyColumn
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext
import timber.log.Timber
import kotlin.math.absoluteValue

@Destination
@Composable
fun RatesScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
) {
    KoinContext {
        val viewModel = koinViewModel<CoinViewModel>()
        
        // Collect state flows efficiently with initialValue to avoid stalls
        val coins by viewModel.coinList.collectAsStateWithLifecycle(initialValue = emptyList())
        val coinsWithPrices by viewModel.coinsWithPricesList.collectAsStateWithLifecycle(initialValue = emptyList())
        val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(initialValue = true)
        
        // Create efficient lookups outside of the item rendering
        val coinPriceMap = remember(coinsWithPrices) {
            coinsWithPrices.associateBy { it.id }
        }
        
        // Only trigger refresh on first composition
        LaunchedEffect(Unit) {
            if (coins.isEmpty() && !isLoading) {
                viewModel.refreshCoins()
            }
        }

        // Use derivedStateOf for derived UI states to avoid unnecessary recompositions
        val showFullScreenLoading = remember(coins, isLoading) { 
            isLoading && coins.isEmpty() 
        }

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
            // Add a title at the top of the screen
            Text(
                text = "Rates",
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            if (showFullScreenLoading) {
                // Only show full-screen loading indicator on initial load
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            } else {
                // Optimize rendering with key for proper diffing
                val listItems = remember(coins) { coins.toList() }
                
                PullToRefreshLazyColumn(
                    items = listItems,
                    content = { coin ->
                        // Look up price data using efficient map lookup
                        val coinWithPrice = coinPriceMap[coin.id]
                        
                        EnhancedCoinCard(
                            coin = coin,
                            price = coinWithPrice?.price,
                            priceChange24h = coinWithPrice?.priceChange24h
                        )
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

// Optimized EnhancedCoinCard with better memory performance
@Composable
fun EnhancedCoinCard(coin: CoinPaprikaCoin, price: Double? = null, priceChange24h: Double? = null) {
    val currentPrice = price ?: 0.0
    val priceChangePercent = priceChange24h ?: 0.0
    val isPriceUp = priceChangePercent >= 0
    val priceAvailable = price != null

    // Pre-format strings to avoid doing this during the draw phase
    val priceText = if (priceAvailable) "$${String.format("%,.2f", currentPrice)}" else "Price N/A"
    val changeText = if (priceAvailable && priceChange24h != null) {
        val prefix = if (isPriceUp) "+" else ""
        "$prefix${String.format("%.2f", priceChangePercent)}%"
    } else ""
    
    // Use remember for colors to avoid recreation
    val changeColor = remember(isPriceUp) {
        if (isPriceUp) Color(0xFF4CAF50) else Color(0xFFE53935)
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = coin.symbol.take(1),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = coin.symbol,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Rank #${coin.rank}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = priceText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (priceAvailable && priceChange24h != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isPriceUp) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "Price change direction",
                            tint = changeColor,
                            modifier = Modifier.size(16.dp)
                        )

                        Text(
                            text = changeText,
                            fontSize = 14.sp,
                            color = changeColor
                        )
                    }
                } else if (!priceAvailable) {
                    Text(
                        text = "Change N/A",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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