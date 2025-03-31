package app.unicornapp.unicorncrm.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun <T> PullToRefreshLazyVerticalGridIndexed(
    items: List<T>,
    content: @Composable (index: Int, item: T) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            state = gridState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 4.dp
            )
        ) {
            itemsIndexed(
                items = items,
                key = { index, item -> 
                    // Use the index as a fallback if the item doesn't have a unique ID
                    // This helps maintain scroll position when items are added or removed
                    index
                }
            ) { index, item ->
                content(index, item)
            }
        }
    }
}