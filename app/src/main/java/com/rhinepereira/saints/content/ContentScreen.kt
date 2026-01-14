package com.rhinepereira.saints.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.rhinepereira.saints.data.model.ContentBlock
import com.rhinepereira.saints.ui.components.ContentBlockRenderer
import com.rhinepereira.saints.ui.components.ErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    viewModel: ContentViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.content?.title ?: "Angels and Saints") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                        // 1. Header Image with Scrim
                        if (imageUrl != null && viewModel.categoryId != "prayers") {
                            var showImage by remember(imageUrl) { mutableStateOf(true) }

                            if (showImage) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 300.dp)
                                ) {
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth(),
                                        contentScale = ContentScale.Crop,
                                        onState = { state ->
                                            if (state is AsyncImagePainter.State.Error) {
                                                showImage = false
                                            }
                                        }
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Black.copy(alpha = 0.5f),
                                                        Color.Transparent
                                                    ),
                                                    startY = 0f,
                                                    endY = 500f
                                                )
                                            )
                                    )
                                }
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
                        modifier = Modifier.tabIndicatorOffset(tabPositions[safeIndex]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            versions.forEachIndexed { index, version ->
                Tab(
                    selected = safeIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(version.label ?: "Version ${index + 1}") },
                    modifier = Modifier.padding(vertical = 16.dp)
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
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
