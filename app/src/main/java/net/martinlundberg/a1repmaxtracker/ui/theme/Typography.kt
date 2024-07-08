package net.martinlundberg.a1repmaxtracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import net.martinlundberg.a1repmaxtracker.R

val antonFamily = FontFamily(Font(R.font.anton_regular))
val notoSansFamily = FontFamily(
    Font(R.font.noto_sans, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.noto_sans_italic, FontWeight.Normal, FontStyle.Italic)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = antonFamily,
        fontSize = 48.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = antonFamily,
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = notoSansFamily,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = notoSansFamily,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = notoSansFamily,
        fontSize = 14.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = notoSansFamily,
        fontSize = 12.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = antonFamily,
        fontSize = 16.sp,
    )
)

