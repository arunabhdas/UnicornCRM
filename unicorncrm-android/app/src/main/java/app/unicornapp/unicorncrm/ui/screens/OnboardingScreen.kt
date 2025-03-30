package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.unicornapp.unicorncrm.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.launch

/**
 * Onboarding screen for Unicorn CRM app featuring a horizontal pager
 * with 6 pages of marketing content and illustrations.
 */
@Destination
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit
) {
    // Define onboarding pages content
    val pages = listOf(
        OnboardingPage(
            title = "Predict the Future, React in Real-Time",
            subtitle = "Welcome to Unicorn CRM",
            description = "Unicorn CRM leverages cutting-edge AI to transform market data into your competitive advantage. Welcome to intelligence that doesn't just inform—it empowers.",
            imageRes = R.drawable.img_onboarding_welcome // You'll need to create these image resources
        ),
        OnboardingPage(
            title = "Beyond Data, Beyond Insights",
            subtitle = "AI-Powered Intelligence",
            description = "Our proprietary AI engine continuously analyzes thousands of market signals, detecting patterns human analysts would miss. Unicorn doesn't just collect data—it connects the dots that matter to your business.",
            imageRes = R.drawable.img_onboarding_welcome
        ),
        OnboardingPage(
            title = "Minutes Matter, Seconds Win",
            subtitle = "Real-Time Market Signals",
            description = "While competitors rely on yesterday's news, Unicorn delivers intelligence as it happens. Track market movements, competitor actions, and emerging opportunities the moment they appear—because in today's markets, timing isn't just important, it's everything.",
            imageRes = R.drawable.img_onboarding_welcome
        ),
        OnboardingPage(
            title = "The World's Intelligence at Your Fingertips",
            subtitle = "OSINT Integration",
            description = "Unicorn seamlessly integrates Open Source Intelligence from across the digital landscape. From social sentiment to regulatory changes, from press releases to patent filings—if it's public, it's in your arsenal.",
            imageRes = R.drawable.img_onboarding_welcome
        ),
        OnboardingPage(
            title = "Insights That Demand Action",
            subtitle = "Actionable Intelligence",
            description = "We don't believe in information overload. Unicorn translates complex market signals into clear, prioritized recommendations tailored to your business objectives. Each alert includes context, potential impact, and suggested next steps.",
            imageRes = R.drawable.img_onboarding_welcome
        ),
        OnboardingPage(
            title = "Your Competitive Edge Awaits",
            subtitle = "Get Started",
            description = "Join the elite businesses already leveraging Unicorn's intelligence platform. Set up your custom dashboard in minutes, connect your data sources, and watch as your market visibility transforms from reactive to predictive.",
            imageRes = R.drawable.img_onboarding_welcome
        )
    )

    // Set up pager state and scope for animations
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    // Main container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Horizontal pager for swiping between screens
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { position ->
            OnboardingPageContent(
                page = pages[position],
                isLastPage = position == pages.size - 1,
                onGetStartedClick = onGetStartedClick
            )
        }

        // Page indicators at bottom of screen
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        // Skip button - only visible if not on last page
        AnimatedVisibility(
            visible = pagerState.currentPage < pages.size - 1,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pages.size - 1)
                    }
                }
            ) {
                Text(
                    text = "Skip",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Next button - only visible if not on last page
        AnimatedVisibility(
            visible = pagerState.currentPage < pages.size - 1,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            ) {
                Text(
                    text = "→",
                    fontSize = 20.sp
                )
            }
        }
    }
}

/**
 * Data class representing the content of each onboarding page
 */
data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val description: String,
    val imageRes: Int
)

/**
 * Content for a single onboarding page
 */
@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    isLastPage: Boolean,
    onGetStartedClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Image card
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = page.subtitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Subtitle text
        Text(
            text = page.subtitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title text
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description text
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Get Started button - only visible on the last page
        AnimatedVisibility(
            visible = isLastPage,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}