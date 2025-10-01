package com.example.kouki.fujisue.androidlab.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animation Examples") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("animateColorAsState")
            ColorAnimationExample()
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            SectionTitle("animateDpAsState")
            SizeAnimationExample()
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            SectionTitle("AnimatedVisibility")
            VisibilityAnimationExample()
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            SectionTitle("animateContentSize")
            ContentSizeAnimationExample()
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            SectionTitle("Crossfade")
            CrossfadeAnimationExample()
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            SectionTitle("updateTransition")
            TransitionAnimationExample()
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            SectionTitle("rememberInfiniteTransition")
            InfiniteAnimationExample()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ColorAnimationExample() {
    var usePrimaryColor by remember { mutableStateOf(true) }
    val animatedColor by animateColorAsState(
        targetValue = if (usePrimaryColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(1000), // 1秒かけて色を変化
        label = "Color Animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(animatedColor)
        )
        Button(onClick = { usePrimaryColor = !usePrimaryColor }) {
            Text("色を変更")
        }
    }
}

@Composable
fun SizeAnimationExample() {
    var isExpanded by remember { mutableStateOf(false) }
    val animatedSize by animateDpAsState(
        targetValue = if (isExpanded) 150.dp else 100.dp,
        label = "Size Animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(animatedSize)
                .background(MaterialTheme.colorScheme.tertiary)
        )
        Button(onClick = { isExpanded = !isExpanded }) {
            Text(if (isExpanded) "縮小" else "拡大")
        }
    }
}

@Composable
fun VisibilityAnimationExample() {
    var isVisible by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text("表示/非表示", modifier = Modifier.align(Alignment.Center))
            }
        }
        Button(onClick = { isVisible = !isVisible }) {
            Text(if (isVisible) "非表示にする" else "表示する")
        }
    }
}

@Composable
fun ContentSizeAnimationExample() {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .animateContentSize(animationSpec = tween(500)) // このModifierを追加
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Text(
            text = if (isExpanded) {
                "これは長いテキストです。animateContentSizeは、コンテンツのサイズが変更されたときに、そのサイズ変更を自動的にアニメーション化します。Modifierをクリックするとテキストの長さが変わります。"
            } else {
                "タップして詳細表示..."
            },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CrossfadeAnimationExample() {
    var screenState by remember { mutableStateOf(Screen.Play) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Crossfade(
            targetState = screenState,
            animationSpec = tween(500),
            label = "Crossfade"
        ) { state ->
            when (state) {
                Screen.Play -> {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(100.dp)
                    )
                }
                Screen.Stop -> {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Stop",
                        modifier = Modifier.size(100.dp),
                        tint = Color.Red
                    )
                }
            }
        }
        Button(onClick = {
            screenState = if (screenState == Screen.Play) Screen.Stop else Screen.Play
        }) {
            Text("切り替え")
        }
    }
}

private enum class BoxState { Collapsed, Expanded }

@Composable
fun TransitionAnimationExample() {
    var boxState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(targetState = boxState, label = "Box Transition")

    val color by transition.animateColor(label = "Color") { state ->
        when (state) {
            BoxState.Collapsed -> MaterialTheme.colorScheme.primary
            BoxState.Expanded -> MaterialTheme.colorScheme.secondary
        }
    }
    val size by transition.animateDp(label = "Size") { state ->
        when (state) {
            BoxState.Collapsed -> 100.dp
            BoxState.Expanded -> 150.dp
        }
    }
    val cornerRadius by transition.animateDp(label = "Corner Radius") { state ->
        when (state) {
            BoxState.Collapsed -> 8.dp
            BoxState.Expanded -> 32.dp
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(cornerRadius))
                .background(color)
        )
        Button(onClick = {
            boxState = if (boxState == BoxState.Collapsed) BoxState.Expanded else BoxState.Collapsed
        }) {
            Text("トランジション実行")
        }
    }
}

@Composable
fun InfiniteAnimationExample() {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinite Transition")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Infinite Color"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text("無限アニメーション")
    }
}

enum class Screen { Play, Stop }

@Preview(showBackground = true)
@Composable
fun AnimationScreenPreview() {
    AndroidLabTheme {
        AnimationScreen()
    }
}
