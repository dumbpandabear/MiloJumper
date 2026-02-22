package com.milolabs.milojumper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import com.milolabs.milojumper.ui.theme.MiloJumperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiloJumperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MiloJumperGame(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MiloJumperGame(modifier: Modifier = Modifier) {
    val tapCount = remember { mutableStateOf(0) }
    val jumpHeight = remember { mutableStateOf(0f) }
    val isJumping = remember { mutableStateOf(false) }
    val jumpMultiplier = remember { mutableStateOf(1f) }

    // Animate the jump height with gravity effect
    val offsetY = animateFloatAsState(
        targetValue = if (isJumping.value) -jumpHeight.value else 0f,
        animationSpec = tween(durationMillis = 600)
    ).value

    // Reset jump state after landing
    LaunchedEffect(isJumping.value) {
        if (isJumping.value) {
            delay(600)
            isJumping.value = false
            jumpHeight.value = 0f
            jumpMultiplier.value = 1f
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .clickable {
                if (!isJumping.value) {
                    // First tap - start jump
                    isJumping.value = true
                    jumpHeight.value = 150f
                    tapCount.value++
                    jumpMultiplier.value = 1f
                } else {
                    // Tap while jumping - jump higher
                    jumpMultiplier.value += 0.5f
                    jumpHeight.value = 150f * jumpMultiplier.value
                    tapCount.value++
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Milo image that jumps
        Image(
            painter = painterResource(R.drawable.milo),
            contentDescription = "Milo",
            modifier = Modifier
                .size(150.dp)
                .padding(16.dp)
                .graphicsLayer {
                    translationY = offsetY
                },
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Taps: ${tapCount.value}",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF795548),
            modifier = Modifier.padding(top = 32.dp)
        )

        Text(
            text = if (isJumping.value) "Keep tapping to jump higher!" else "Tap to make Milo jump!",
            fontSize = 14.sp,
            color = Color(0xFFAA7755),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MiloJumperPreview() {
    MiloJumperTheme {
        MiloJumperGame()
    }
}