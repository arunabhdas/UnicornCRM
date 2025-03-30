package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import app.unicornapp.unicorncrm.presentation.CoinWithPrice
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.random.Random
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlin.math.absoluteValue

@Destination
@Composable
fun RatesDetailScreen(
    coinId: String,
    coinName: String,
    coinSymbol: String,
    coinRank: Int,
    price: Double?,
    priceChange24h: Double?,
    navController: NavController,
    navigator: DestinationsNavigator,
) {
    // Format price and change values
    val formattedPrice = remember(price) {
        if (price != null) "$${String.format("%,.2f", price)}" else "Price N/A"
    }
    
    val isPriceUp = remember(priceChange24h) {
        priceChange24h != null && priceChange24h >= 0
    }
    
    val changeText = remember(priceChange24h, isPriceUp) {
        if (priceChange24h != null) {
            val prefix = if (isPriceUp) "+" else ""
            "$prefix${String.format("%.2f", priceChange24h)}%"
        } else "N/A"
    }
    
    val changeColor = remember(isPriceUp) {
        if (isPriceUp) Color(0xFF4CAF50) else Color(0xFFE53935)
    }
    
    // Generate mock chart data
    val chartPoints = remember {
        List(24) { 
            val random = if (isPriceUp) Random.nextDouble(0.8, 1.2) else Random.nextDouble(0.8, 1.0)
            price?.times(random) ?: Random.nextDouble(100.0, 200.0)
        }
    }
    
    // Calculate min/max for chart scaling
    val minValue = remember(chartPoints) { chartPoints.minOrNull() ?: 0.0 }
    val maxValue = remember(chartPoints) { chartPoints.maxOrNull() ?: 100.0 }
    
    // Handle system back button
    BackHandler {
        navController.popBackStack()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            )
            // Add edge swipe detection for back navigation
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    // Get the absolute position of the touch
                    val touchX = change.position.x
                    
                    // Only respond to right swipes starting from the left edge (within 40dp from edge)
                    if (touchX < 40.dp.toPx() && dragAmount > 0) {
                        if (dragAmount > 10) { // Require minimum swipe distance
                            navController.popBackStack()
                        }
                    }
                }
            },
    ) {
        // Top app bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { 
                    // Try both navigation methods to ensure one works
                    try {
                        // TODO-FIXME-CLEANUP navigator.navigateUp()
                        navController.popBackStack()
                    } catch (e: Exception) {
                        navController.popBackStack()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            Text(
                text = "$coinSymbol Details",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp) // Leave space for the app bar
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Coin icon and name header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = coinSymbol.take(1),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = coinName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = coinSymbol,
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Rank #$coinRank",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            
            // Price card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current Price",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = formattedPrice,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isPriceUp) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "Price change direction",
                            tint = changeColor,
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Text(
                            text = changeText,
                            fontSize = 18.sp,
                            color = changeColor
                        )
                        
                        Text(
                            text = " (24h)",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Chart card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(250.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Price Chart (24h)",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp, bottom = 24.dp, start = 8.dp, end = 8.dp)
                    ) {
                        // Chart implementation
                        PriceChart(
                            points = chartPoints,
                            minValue = minValue,
                            maxValue = maxValue,
                            color = if (isPriceUp) Color(0xFF4CAF50) else Color(0xFFE53935),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            
            // Market stats card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Market Stats",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    StatRow("Market Cap", "$${formatLargeNumber(price?.times(80000000) ?: 0.0)}")
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))
                    
                    StatRow("Volume (24h)", "$${formatLargeNumber(price?.times(5000000) ?: 0.0)}")
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))
                    
                    StatRow("Circulating Supply", "${formatLargeNumber(80000000.0)} $coinSymbol")
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))
                    
                    StatRow("All-Time High", "$${String.format("%.2f", price?.times(1.5) ?: 0.0)}")
                }
            }
            
            // Leave some space at the bottom for better scrolling experience
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PriceChart(
    points: List<Double>,
    minValue: Double,
    maxValue: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    val range = maxValue - minValue
    
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val strokePath = Path()
        val fillPath = Path()

        if (points.size >= 2) {
            // Calculate x step based on available width
            val xStep = width / (points.size - 1)
            
            // Set the initial point
            val initialX = 0f
            val initialY = height - (((points[0] - minValue) / range) * height).toFloat()
            strokePath.moveTo(initialX, initialY)
            fillPath.moveTo(initialX, initialY)
            
            // Create the path through all points
            for (i in 1 until points.size) {
                val x = i * xStep
                val y = height - (((points[i] - minValue) / range) * height).toFloat()
                strokePath.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            
            // Complete the fill path by drawing to the bottom corners
            fillPath.lineTo(width, height)
            fillPath.lineTo(0f, height)
            fillPath.close()
            
            // Draw fill with gradient
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(color.copy(alpha = 0.3f), Transparent),
                    startY = 0f,
                    endY = height
                )
            )
            
            // Draw line
            drawPath(
                path = strokePath,
                color = color,
                style = Stroke(width = 2.5f)
            )
            
            // Draw points
            for (i in points.indices) {
                val x = i * xStep
                val y = height - (((points[i] - minValue) / range) * height).toFloat()
                
                drawCircle(
                    color = color,
                    radius = 3f,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// Helper function to format large numbers with K, M, B suffixes
fun formatLargeNumber(number: Double): String {
    return when {
        number >= 1_000_000_000 -> String.format("%.2fB", number / 1_000_000_000)
        number >= 1_000_000 -> String.format("%.2fM", number / 1_000_000)
        number >= 1_000 -> String.format("%.2fK", number / 1_000)
        else -> String.format("%.2f", number)
    }
}