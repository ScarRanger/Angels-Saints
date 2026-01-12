package com.rhinepereira.saints.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.rhinepereira.saints.data.model.ContentBlock
import com.rhinepereira.saints.ui.components.ContentBlockRenderer
import com.rhinepereira.saints.ui.components.ErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    viewModel: ContentViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.content?.title ?: "Angels and Saints") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    ErrorState(
                        error = uiState.error!!,
                        onRetry = { viewModel.retry() }
                    )
                }
                uiState.content != null -> {
                    val content = uiState.content!!
                    val versions = content.versions ?: emptyList()
                    val rootBlocks = content.blocks ?: emptyList()
                    val imageUrl = content.imageUrl?.trim()?.takeIf { it.isNotBlank() }

                    Column(modifier = Modifier.fillMaxSize()) {
                        // 1. Header Image (Completely skipped for prayers category)
                        if (imageUrl != null && viewModel.categoryId != "prayers") {
                            var showImage by remember(imageUrl) { mutableStateOf(true) }
                            
                            if (showImage) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 400.dp),
                                    contentScale = ContentScale.FillWidth,
                                    onState = { state ->
                                        if (state is AsyncImagePainter.State.Error) {
                                            showImage = false
                                        }
                                    }
                                )
                            }
                        }

                        // 2. Main Content Area
                        Box(modifier = Modifier.weight(1f)) {
                            if (versions.isNotEmpty()) {
                                TabbedContent(versions = versions)
                            } else if (rootBlocks.isNotEmpty()) {
                                ContentBlockList(blocks = rootBlocks)
                            } else if (imageUrl == null || viewModel.categoryId == "prayers") {
                                EmptyContentState()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabbedContent(versions: List<com.rhinepereira.saints.data.remote.ContentVersion>) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val safeIndex = if (selectedTabIndex < versions.size) selectedTabIndex else 0

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = safeIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (safeIndex < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[safeIndex])
                    )
                }
            }
        ) {
            versions.forEachIndexed { index, version ->
                Tab(
                    selected = safeIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(version.label ?: "Version ${index + 1}") }
                )
            }
        }
        
        ContentBlockList(
            blocks = versions[safeIndex].blocks ?: emptyList(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ContentBlockList(
    blocks: List<ContentBlock>,
    modifier: Modifier = Modifier
) {
    if (blocks.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No content available.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(blocks) { block ->
                ContentBlockRenderer(block = block)
            }
        }
    }
}

@Composable
private fun EmptyContentState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Content coming soon!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Text(
            text = "We are currently updating our library. Please check back later.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
