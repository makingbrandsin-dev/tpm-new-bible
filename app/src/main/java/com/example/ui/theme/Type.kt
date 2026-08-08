package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val playfairFont = GoogleFont("Playfair Display")
val interTightFont = GoogleFont("Inter Tight")

val PlayfairDisplayFontFamily = FontFamily(
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.Bold)
)

val InterTightFontFamily = FontFamily(
    Font(googleFont = interTightFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = interTightFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = interTightFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = interTightFont, fontProvider = provider, weight = FontWeight.Bold)
)

// Typography hierarchy with Playfair Display for Titles and Inter Tight for Body & Other text
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.02).sp,
        color = CelestialGold
    ),
    displayMedium = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        color = OnSurfaceLight
    ),
    displaySmall = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 38.sp,
        color = OnSurfaceLight
    ),
    headlineLarge = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        color = OnSurfaceLight
    ),
    headlineMedium = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        color = OnSurfaceLight
    ),
    headlineSmall = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        color = OnSurfaceLight
    ),
    titleLarge = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = OnSurfaceLight
    ),
    titleMedium = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = OnSurfaceLight
    ),
    titleSmall = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = OnSurfaceLight
    ),
    bodyLarge = TextStyle(
        fontFamily = InterTightFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color = OnSurfaceLight
    ),
    bodyMedium = TextStyle(
        fontFamily = InterTightFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = OnSurfaceVariantMuted
    ),
    bodySmall = TextStyle(
        fontFamily = InterTightFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = OnSurfaceVariantMuted
    ),
    labelLarge = TextStyle(
        fontFamily = InterTightFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterTightFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InterTightFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

val ScriptureStyle = TextStyle(
    fontFamily = PlayfairDisplayFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 32.sp,
    color = OnSurfaceLight
)
