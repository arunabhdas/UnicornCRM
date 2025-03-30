package app.unicornapp.unicorncrm.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp

@Composable
fun <T> PullToRefreshLazyVerticalGrid(
    items: List<T>,
    content: @Composable (T) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 4.dp
            )
        ) {
            items(items) { item ->
                content(item)
            }
        }
    }
}