package com.example.belajar3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belajar3.model.PlantTemplate

@Composable
fun GuideScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            GuideHeroCard()
        }

        item {
            Text(
                text = "Panduan & Pengetahuan Dasar HST",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            GuideArticleCard(
                icon = Icons.Default.Timer,
                iconColor = Color(0xFF2E7D32),
                title = "Apa itu HST (Hari Setelah Tanam)?",
                description = "HST adalah standar perhitungan umur tanaman dalam dunia pertanian modern. Perhitungan dimulai sejak hari pertama bibit dipindahkan dari persemaian ke lahan terbuka (tanah/polybag).\n\n" +
                        "• Hari Tanam dihitung sebagai HST 0.\n" +
                        "• Keesokan harinya adalah HST 1, HST 2, dan seterusnya.\n" +
                        "• Beda dengan HSS (Hari Setelah Semai) yang dihitung sejak benih mulai disemai di tray."
            )
        }

        item {
            GuideArticleCard(
                icon = Icons.Default.Lightbulb,
                iconColor = Color(0xFFF57F17),
                title = "Mengapa Jadwal Berdasarkan HST Sangat Efektif?",
                description = "Kebutuhan nutrisi dan kerentanan hama tanaman berubah drastis sesuai fase fisiologisnya, bukan hari kalender acak:\n\n" +
                        "1. HST 0 - 25 (Fase Vegetatif Awal): Tanaman membutuhkan Nitrogen (N) tinggi untuk pembentukan akar, batang, dan klorofil daun.\n" +
                        "2. HST 30 - 50 (Fase Peralihan / Flowering): Tanaman butuh Fosfat (P) dan Kalsium tinggi untuk pembentukan kuncup bunga anti rontok.\n" +
                        "3. HST 50+ (Fase Generatif / Buah): Tanaman butuh Kalium (K) dan Boron tinggi untuk pembesaran, bobot, dan rasa manis buah."
            )
        }

        item {
            GuideArticleCard(
                icon = Icons.Default.AutoAwesome,
                iconColor = Color(0xFF00796B),
                title = "Tips Waktu Terbaik Perawatan Tanaman",
                description = "• Penyiraman: Pagi hari (06:00 - 08:00) agar tanaman terhidrasi sebelum matahari terik. Hindari menyiram daun di malam hari karena memicu jamur.\n" +
                        "• Pemupukan Kocor: Pagi hari setelah tanah disiram lembap agar akar tidak terbakar pupuk pekat.\n" +
                        "• Penyemprotan Pestisida: Pagi (06:30 - 08:30) saat stomata daun terbuka atau sore (16:00 - 17:30) saat serangga hama aktif keluar dari persembunyian."
            )
        }

        item {
            Text(
                text = "Preset Template Tanaman Bawaan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        val templates = PlantTemplate.getAllTemplates().filter { it.id != "custom" }
        items(templates.size) { index ->
            val tmpl = templates[index]
            TemplateSummaryCard(template = tmpl)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun GuideHeroCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Aplikasi Offline 100%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Semua jadwal & notifikasi pengingat tersimpan di perangkat lokal tanpa perlu koneksi internet.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun GuideArticleCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TemplateSummaryCard(template: PlantTemplate) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Panen ~${template.estimatedHarvestHst} HST",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Varietas Umum: ${template.defaultVariety}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${template.tasks.size} Jadwal Perawatan Otomatis (HST ${template.tasks.joinToString(", ") { it.targetHst.toString() }})",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
