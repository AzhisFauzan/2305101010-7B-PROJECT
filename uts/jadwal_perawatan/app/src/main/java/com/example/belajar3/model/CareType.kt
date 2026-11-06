package com.example.belajar3.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class CareType(
    val displayName: String,
    val description: String
) {
    FERTILIZING("Pemupukan", "Nutrisi makro/mikro, cor, atau tabur"),
    WATERING("Penyiraman", "Kebutuhan air dan kelembapan media tanam"),
    PESTICIDE("Pengendalian Hama & Penyakit", "Insektisida, fungisida, atau pestisida nabati"),
    PRUNING("Pemangkasan & Wiwil", "Pemotongan tunas air, cabang liar, daun tua"),
    WEEDING("Penyiangan Gulma", "Pembersihan gulma & penggemburan tanah"),
    HARVESTING("Panen", "Pemetikan hasil panen pertama atau berkala"),
    OTHER("Perawatan Lainnya", "Pemasangan ajir, pengikatan batang, dll.");

    fun getIcon(): ImageVector {
        return when (this) {
            FERTILIZING -> Icons.Default.Eco
            WATERING -> Icons.Default.WaterDrop
            PESTICIDE -> Icons.Default.BugReport
            PRUNING -> Icons.Default.ContentCut
            WEEDING -> Icons.Default.Grass
            HARVESTING -> Icons.Default.ShoppingBasket
            OTHER -> Icons.Default.CheckCircle
        }
    }

    fun getColor(): Color {
        return when (this) {
            FERTILIZING -> Color(0xFF2E7D32) // Forest Green
            WATERING -> Color(0xFF0288D1)    // Light Blue
            PESTICIDE -> Color(0xFFD84315)   // Deep Orange
            PRUNING -> Color(0xFF7B1FA2)     // Purple
            WEEDING -> Color(0xFFF57F17)     // Warm Amber / Mustard
            HARVESTING -> Color(0xFF00796B)  // Teal
            OTHER -> Color(0xFF546E7A)       // Blue Grey
        }
    }

    fun getContainerColor(): Color {
        return when (this) {
            FERTILIZING -> Color(0xFFE8F5E9)
            WATERING -> Color(0xFFE1F5FE)
            PESTICIDE -> Color(0xFFFBE9E7)
            PRUNING -> Color(0xFFF3E5F5)
            WEEDING -> Color(0xFFFFF8E1)
            HARVESTING -> Color(0xFFE0F2F1)
            OTHER -> Color(0xFFECEFF1)
        }
    }
}
