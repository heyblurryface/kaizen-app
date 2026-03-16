package br.com.fiap.kaizen.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.fiap.kaizen.R

// FONTE SORA
val soraFamily = FontFamily(
    Font(R.font.sora_light, FontWeight.Light),
    Font(R.font.sora_regular, FontWeight.Normal),
    Font(R.font.sora_semi_bold, FontWeight.SemiBold),
    Font(R.font.sora_bold, FontWeight.Bold)
)


val Typography = Typography(
    // Nome do usuario na TopBar
    bodyLarge = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    // Descricoes e textos secundarios
    bodySmall = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    // Descricoes dentro dos cards
    bodyMedium = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    // Titulo principal da Login ("Bem-vindo ao Kaizen")
    titleLarge = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    // Titulos de pagina ("Perfil Da Empresa")
    titleMedium = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    // Subtitulo da Login
    titleSmall = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    // Labels dos campos de formulario
    labelSmall = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Texto dos botoes ("Entrar", "Salvar e continuar")
    labelMedium = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    // Titulos dos cards ("Maturidade atual", "Nova pre-avaliacao")
    labelLarge = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Reservado
    displayLarge = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 68.sp,
        letterSpacing = 0.sp
    ),
    // Reservado
    displayMedium = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 68.sp,
        letterSpacing = 0.sp
    ),
    // Empresa/cargo na TopBar
    displaySmall = TextStyle(
        fontFamily = soraFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
)