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
    bodySmall = TextStyle(
        fontFamily = notoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = notoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = notoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = antonFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = notoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = antonFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    )
)

