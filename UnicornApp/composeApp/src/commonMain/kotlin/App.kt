import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        AppContent(homeViewModel = HomeViewModel())
    }
}

fun createGradientEffect(
    colors: List<Color>,
    isVertical: Boolean = true
): Brush {

    // Set the end offset for a vertical gradient (top to bottom)
    val endOffset = if (isVertical) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(homeViewModel: HomeViewModel) {
   val products = homeViewModel.products.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        val scope = this
        val maxWidth = scope.maxWidth

        var cols = 2
        var modifier = Modifier.fillMaxWidth()
        if (maxWidth > 840.dp) {
           cols = 3
           modifier = Modifier.widthIn(max = 1080.dp)

        }

        val scrollState = rememberLazyGridState()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(cols),
                state = scrollState,
                contentPadding = PaddingValues(16.dp)
                ) {
                item(span = { GridItemSpan(cols) }) {
                    Column {
                        SearchBar(
                            modifier = Modifier.fillMaxWidth(),
                            query = "",
                            active = false,
                            onActiveChange = {},
                            onQueryChange = {},
                            onSearch = {},
                            leadingIcon = { Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )},
                            placeholder = { Text("Search")}
                        ) {

                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                items(
                    items = products.value,
                    key = {product ->
                        product.id.toString()
                    }) {product ->
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        elevation = 2.dp
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val painter = rememberImagePainter(url = product.image.toString())
                            Image(
                                painter = painter,
                                contentDescription = "image",
                                modifier = Modifier
                                    .height(130.dp)
                                    .padding(8.dp)

                            )
                            Text(
                                product.title,
                                maxLines = 2,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .heightIn(min = 40.dp),
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "${product.price.toString()} USD",
                                maxLines = 2,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .heightIn(min = 40.dp),
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                    }
                }
            }
        }
    }

}