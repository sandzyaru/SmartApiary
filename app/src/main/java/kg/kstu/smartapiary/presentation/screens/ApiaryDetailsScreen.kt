@file:OptIn(ExperimentalMaterial3Api::class)

package kg.kstu.smartapiary.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kg.kstu.smartapiary.presentation.screens.viewmodel.ApiaryDetailsScreenViewModel

@Composable
fun ApiaryDetailsScreen(hiveId: String, navController: NavHostController) {
    val viewModel: ApiaryDetailsScreenViewModel = hiltViewModel()
    val graphicsData by viewModel.graphicsData.collectAsState()

    val titles = mapOf(
        "humidity" to "Влажность (%)",
        "strength_signal" to "Уровень сигнала (dB)",
        "temp" to "Температура (°C)",
        "weight" to "Вес (кг)"
    )

    val units = mapOf(
        "humidity" to "%",
        "strength_signal" to "dB",
        "temp" to "°C",
        "weight" to "кг"
    )

    LaunchedEffect(hiveId) { viewModel.loadGraphicsData(hiveId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Детали пасеки №$hiveId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Графики параметров", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            graphicsData.forEach { (param, values) ->
                val title = titles[param] ?: param
                val unit = units[param] ?: ""
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                LineChartCanvas(values.values.map { it.toFloat() }, Color.Yellow, unit)
            }
        }
    }
}


@Composable
fun LineChartCanvas(
    data: List<Float>,
    lineColor: Color,
    yLabel: String // Единица измерения (например, "°C", "кг", "%")
) {
    val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс") // 📌 Дни недели

    val paddingLeft = 90f // 📌 Отступ слева для оси Y (чтобы сам график не залезал)
    val paddingBottom = 50f // 📌 Отступ снизу для оси X
    val gridColor = Color.LightGray // 📌 Цвет сетки

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // 📌 Высота графика
            .padding(8.dp)
    ) {
        if (data.size != 7) return@Canvas // ✅ Проверка: данных должно быть ровно 7

        var maxY = (data.maxOrNull() ?: 1f) + 1f // 📌 Добавляем отступ вверх
        var minY = (data.minOrNull() ?: 0f) - 1f // 📌 Добавляем отступ вниз

        // 📌 Округляем Y до целых значений
        maxY = kotlin.math.ceil(maxY)
        minY = kotlin.math.floor(minY)

        val rangeY = maxY - minY
        val chartWidth = size.width - paddingLeft
        val chartHeight = size.height - paddingBottom

        // 📌 Рисуем сетку (горизонтальные линии)
        val stepY = chartHeight / 4
        for (i in 0..4) {
            val yValue = minY + (rangeY / 4) * i
            val yPosition = chartHeight - stepY * i
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(paddingLeft, yPosition),
                end = androidx.compose.ui.geometry.Offset(size.width, yPosition),
                strokeWidth = 1.dp.toPx()
            )

            // 📌 Подписи оси Y (Сдвигаем левее!)
            drawContext.canvas.nativeCanvas.drawText(
                "${yValue.toInt()} $yLabel",
                40f, // 📌 Сдвинул подписи Y левее
                yPosition + 10f, // 📌 Выравниваем текст по центру
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                    textAlign = android.graphics.Paint.Align.RIGHT // 📌 Выравниваем по правому краю
                }
            )
        }

        // 📌 Рисуем сетку (вертикальные линии)
        val stepX = chartWidth / (daysOfWeek.size - 1)
        for (i in daysOfWeek.indices) {
            val xPosition = paddingLeft + stepX * i
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(xPosition, 0f),
                end = androidx.compose.ui.geometry.Offset(xPosition, chartHeight),
                strokeWidth = 1.dp.toPx()
            )

            // 📌 Подписи оси X (дни недели)
            drawContext.canvas.nativeCanvas.drawText(
                daysOfWeek[i],
                xPosition,
                size.height, // Подписи теперь корректно ниже графика
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }

        // 📌 Рисуем линию графика
        val path = Path().apply {
            val startX = paddingLeft
            val startY = chartHeight - (data[0] - minY) / rangeY * chartHeight
            moveTo(startX, startY)

            for (i in data.indices) {
                val x = paddingLeft + i / (data.size - 1).toFloat() * chartWidth
                val y = chartHeight - (data[i] - minY) / rangeY * chartHeight
                lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 4.dp.toPx())
        )

        // 📌 Рисуем точки на графике
        for (i in data.indices) {
            val x = paddingLeft + i / (data.size - 1).toFloat() * chartWidth
            val y = chartHeight - (data[i] - minY) / rangeY * chartHeight
            drawCircle(
                color = lineColor,
                center = androidx.compose.ui.geometry.Offset(x, y),
                radius = 6.dp.toPx()
            )
        }
    }
}




