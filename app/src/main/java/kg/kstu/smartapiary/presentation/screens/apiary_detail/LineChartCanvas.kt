package kg.kstu.smartapiary.presentation.screens.apiary_detail

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun LineChartCanvas(
    data: List<Float>,
    lineColor: Color,
    yLabel: String
) {
    val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    val paddingLeft = 90f
    val paddingBottom = 50f
    val gridColor = Color.LightGray

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
    ) {
        if (data.size != 7) return@Canvas

        var maxY = (data.maxOrNull() ?: 1f) + 1f
        var minY = (data.minOrNull() ?: 0f) - 1f

        maxY = ceil(maxY)
        minY = floor(minY)

        val rangeY = maxY - minY
        val chartWidth = size.width - paddingLeft
        val chartHeight = size.height - paddingBottom

        val stepY = chartHeight / 4
        for (i in 0..4) {
            val yValue = minY + (rangeY / 4) * i
            val yPosition = chartHeight - stepY * i
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, yPosition),
                end = Offset(size.width, yPosition),
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${yValue.toInt()} $yLabel",
                40f,
                yPosition + 10f,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                    textAlign = Paint.Align.RIGHT
                }
            )
        }

        val stepX = chartWidth / (daysOfWeek.size - 1)
        for (i in daysOfWeek.indices) {
            val xPosition = paddingLeft + stepX * i
            drawLine(
                color = gridColor,
                start = Offset(xPosition, 0f),
                end = Offset(xPosition, chartHeight),
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                daysOfWeek[i],
                xPosition,
                size.height,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                    textAlign = Paint.Align.CENTER
                }
            )
        }

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

        for (i in data.indices) {
            val x = paddingLeft + i / (data.size - 1).toFloat() * chartWidth
            val y = chartHeight - (data[i] - minY) / rangeY * chartHeight
            drawCircle(
                color = lineColor,
                center = Offset(x, y),
                radius = 6.dp.toPx()
            )
        }
    }
}