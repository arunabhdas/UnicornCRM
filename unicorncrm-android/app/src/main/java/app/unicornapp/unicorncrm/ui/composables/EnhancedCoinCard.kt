package app.unicornapp.unicorncrm.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import app.unicornapp.unicorncrm.ui.screens.destinations.RatesDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

// Optimized EnhancedCoinCard with better memory performance
@Composable
fun EnhancedCoinCard(
    coin: CoinPaprikaCoin,
    price: Double? = null,
    priceChange24h: Double? = null,
    navController: NavController,
    navigator: DestinationsNavigator
) {
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

            Column(
                modifier = Modifier
                    .weight(1f)

                .clickable {
                    navigator.navigate(
                        RatesDetailScreenDestination(
                            coinId = coin.id,
                            coinName = coin.name,
                            coinSymbol = coin.symbol,
                            coinRank = coin.rank,
                            price = price,
                            priceChange24h = priceChange24h,
                            // TODO-FIXME-CLEANUP navController = navController,
                            // TODO-FIXME-CLEANUP navigator = navigator
                        )
                    )
                }
            )
            {
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

