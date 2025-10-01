package com.example.kouki.fujisue.androidlab.ui.text

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text Examples") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Allow scrolling if content overflows
        ) {

            Text("基本的なテキスト")
            Spacer(Modifier.height(8.dp))

            Text("色付きのテキスト", color = Color.Blue)
            Spacer(Modifier.height(8.dp))

            Text("大きなサイズのテキスト", fontSize = 24.sp)
            Spacer(Modifier.height(8.dp))

            Text("小さなサイズのテキスト", fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))

            Text("太字のテキスト", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            Text("斜体のテキスト", fontStyle = FontStyle.Italic)
            Spacer(Modifier.height(8.dp))

            Text("セリフ体のテキスト", fontFamily = FontFamily.Serif)
            Spacer(Modifier.height(8.dp))

            Text(
                text = "複数行のテキストです。\nこのテキストは改行を含んでいます。\nJetpack Composeでは簡単に複数行のテキストを扱うことができます。",
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(8.dp))

            Text(
                "中央揃えのテキスト",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            val offset = Offset(5.0f, 10.0f)
            Text(
                text = "シャドウのテキスト",
                style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Blue, offset = offset, blurRadius = 3f
                    )
                )
            )
            Spacer(Modifier.height(8.dp))

            val gradientColors = listOf(Cyan, Color.Blue, Color.Yellow)
            Text(
                text = "テキストのスタイル設定にブラシを使用する",
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                )
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = "このテキストは非常に長いため、表示領域を超えた場合に省略されます。末尾に...が表示されるはずです。",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))

            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("赤い")
                }
                append("テキストと")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Green)) {
                    append("緑で太字")
                }
                append("のテキスト")
            })
            Spacer(Modifier.height(8.dp))

            val uriHandler = LocalUriHandler.current
            Text(buildAnnotatedString {
                append("テキストの中に")
                val link = LinkAnnotation.Url(
                    "https://developer.android.com/jetpack/compose",
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                ) {
                    val url = (it as LinkAnnotation.Url).url
                    uriHandler.openUri(url)
                }
                withLink(link) { append("リンク") }
                append("を組み込めるよ！！")
            })
            Spacer(Modifier.height(8.dp))

            Text(
                "Jetpack Composeを使うのが素晴らしい理由について学びましょう",
                modifier = Modifier.basicMarquee(),
                fontSize = 24.sp
            )
            Spacer(Modifier.height(8.dp))

            SelectionContainer {
                Text(
                    text = "このテキストはコピー出来るよ！！\n試してみよう!!",
                    color = Color.Red
                )
            }
            Spacer(Modifier.height(8.dp))

            Text("文字間隔の広いテキスト", letterSpacing = 4.sp)
            Spacer(Modifier.height(8.dp))

            Text("取り消し線のあるテキスト", textDecoration = TextDecoration.LineThrough)
            Spacer(Modifier.height(8.dp))

            Text("右揃えのテキスト", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))

            Text(buildAnnotatedString {
                append("H")
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift.Subscript,
                        fontSize = 12.sp
                    )
                ) {
                    append("2")
                }
                append("O と X")
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 12.sp
                    )
                ) {
                    append("2")
                }
            })
            Spacer(Modifier.height(16.dp))

            Text("MaterialTheme のタイポグラフィを使用:")
            Spacer(Modifier.height(4.dp))
            Text("Display Large", style = MaterialTheme.typography.displayLarge)
            Text("Display Medium", style = MaterialTheme.typography.displayMedium)
            Text("Display Small", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(4.dp))
            Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
            Text("Headline Medium", style = MaterialTheme.typography.headlineMedium)
            Text("Headline Small", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text("Title Large", style = MaterialTheme.typography.titleLarge)
            Text("Title Medium", style = MaterialTheme.typography.titleMedium)
            Text("Title Small", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text("Body Large", style = MaterialTheme.typography.bodyLarge)
            Text("Body Medium", style = MaterialTheme.typography.bodyMedium)
            Text("Body Small", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Text("Label Large", style = MaterialTheme.typography.labelLarge)
            Text("Label Medium", style = MaterialTheme.typography.labelMedium)
            Text("Label Small", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextScreenPreview() {
    AndroidLabTheme {
        TextScreen()
    }
}
