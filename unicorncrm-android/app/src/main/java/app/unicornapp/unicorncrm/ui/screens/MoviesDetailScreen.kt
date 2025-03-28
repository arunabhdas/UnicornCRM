package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MoviesDetailScreen(
    movieId: Int,
    navController: NavController,
    navigator: DestinationsNavigator

) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp), // Adjust top padding as needed
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Movies Detail Screen",
                color = Color.White
            )
        }
    }
}



@Preview
@Composable
fun MoviesDetailScreenPreview() {
    MoviesDetailScreen(
        movieId = 0,
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}