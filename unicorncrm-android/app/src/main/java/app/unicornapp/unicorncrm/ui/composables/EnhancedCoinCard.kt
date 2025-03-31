package app.unicornapp.unicorncrm.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
    // Compute all values outside of the composition phase
    // These are stable calculations that shouldn't happen during layout/draw
    val firstLetter = remember(coin.symbol) { coin.symbol.take(1) }
    val rankText = remember(coin.rank) { "Rank #${coin.rank}" }
    val isPriceUp = remember(priceChange24h) { (priceChange24h ?: 0.0) >= 0 }
    val priceAvailable = price != null
    
    // Pre-format strings to avoid doing this during the draw phase
    val priceText = remember(price, priceAvailable) {
        if (priceAvailable) "$${String.format("%,.2f", price)}" else "Price N/A"
    }
    
    val changeText = remember(priceChange24h, isPriceUp) {
        if (priceAvailable && priceChange24h != null) {
            val prefix = if (isPriceUp) "+" else ""
            "$prefix${String.format("%.2f", priceChange24h)}%"
        } else ""
    }

    // Use remember for colors to avoid recreation
    val changeColor = remember(isPriceUp) {
        if (isPriceUp) Color(0xFF4CAF50) else Color(0xFFE53935)
    }
    
    // Memoize the click handler to prevent recreations during recomposition
    val onClickHandler = remember(coin.id, coin.name, coin.symbol, coin.rank, price, priceChange24h) {
        {
            navigator.navigate(
                RatesDetailScreenDestination(
                    coinId = coin.id,
                    coinName = coin.name,
                    coinSymbol = coin.symbol,
                    coinRank = coin.rank,
                    price = price,
                    priceChange24h = priceChange24h
                )
            )
        }
    }

    // Create a simplified card with less nesting
    Card(
        elevation = CardDefaults.cardElevation(2.dp), // Reduce elevation for faster rendering
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClickHandler),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        // Single row layout to reduce nesting
        Row(
            modifier = Modifier.padding(12.dp), // Reduced padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coin symbol circle
            Box(
                modifier = Modifier
                    .size(40.dp) // Reduced size
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = firstLetter,
                    fontSize = 16.sp, // Reduced font size
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Coin details (middle column)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Coin name
                Text(
                    text = coin.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp, // Reduced font size
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Symbol and rank
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = coin.symbol,
                        fontSize = 12.sp, // Reduced font size
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Rank badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 4.dp, vertical = 2.dp) // Reduced padding
                    ) {
                        Text(
                            text = rankText,
                            fontSize = 10.sp, // Reduced font size
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Price details (right column)
            Column(horizontalAlignment = Alignment.End) {
                // Price
                Text(
                    text = priceText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp, // Reduced font size
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Price change
                if (priceAvailable && priceChange24h != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isPriceUp) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "Price change direction",
                            tint = changeColor,
                            modifier = Modifier.size(14.dp) // Reduced icon size
                        )

                        Text(
                            text = changeText,
                            fontSize = 12.sp, // Reduced font size
                            color = changeColor
                        )
                    }
                } else if (!priceAvailable) {
                    Text(
                        text = "Change N/A",
                        fontSize = 12.sp, // Reduced font size
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
