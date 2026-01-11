package com.rhinepereira.saints.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PapalGold,         // Gold stands out beautifully on dark
    secondary = Color(0xFFE5C100), // Brighter gold for interaction
    tertiary = Color(0xFFD3D3D3), // Silver/Grey for subtle metadata
    background = DeepCharcoal,   // True dark mode
    surface = Color(0xFF1E1E1E), // Slightly lighter surface for cards
    onPrimary = Color.Black,     // Black text on gold buttons
    onBackground = Color(0xFFE1E1E1), // Off-white for comfortable reading
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = DeepBurgundy2,      // Buttons, top bars, primary icons
    secondary = PapalGold,       // Floating action buttons, active states
    tertiary = GoldVariant,      // Accents or secondary icons
    background = CreamBackground, // Warm parchment feel
    surface = MarbleWhite,       // Card backgrounds
    onPrimary = Color.White,     // Text on burgundy
    onSecondary = Color.Black,   // Text on gold
    onBackground = Color(0xFF2C2C2C), // Dark grey for softer reading than pure black
    onSurface = Color(0xFF2C2C2C)
)

@Composable
fun AngelsAndSaintsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled for consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
