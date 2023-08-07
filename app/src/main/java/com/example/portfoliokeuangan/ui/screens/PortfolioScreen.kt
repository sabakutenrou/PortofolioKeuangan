package com.example.portfoliokeuangan.ui.screens

import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.portfoliokeuangan.data.DonutChartData
import com.example.portfoliokeuangan.data.Portfolio
import com.example.portfoliokeuangan.ui.theme.*
import com.example.portfoliokeuangan.ui.viewmodel.PortfolioViewModel
import kotlin.math.atan2
import kotlin.math.min
import kotlin.random.Random

@Composable
fun PortfolioScreen(
    portfolioViewModel: PortfolioViewModel
) {
    val portfolio: List<Portfolio> = portfolioViewModel.getPortfolio()

    val chartValues = mutableListOf<Float>()
    var chartNum = 0
    portfolio.forEach { chart ->
        when (chart.type) {
            "donutChart" -> {
                val donutChartDataList = chart.data as List<DonutChartData>
                donutChartDataList.forEach { donutChartData ->
                    chartValues.add(donutChartData.percentage.toFloat())
                    chartNum++
                }
            }
        }
    }
    val chartColors = generateColors(chartNum)

//    val chartValues = listOf(60f, 10f, 20f, 20f, 120f)

    PieChart(
        modifier = Modifier.padding(20.dp),
        colors = chartColors,
        inputValues = chartValues,
        textColor = purple
    )

}

@Composable
internal fun PieChart(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    inputValues: List<Float>,
    textColor: Color = MaterialTheme.colorScheme.primary
) {
    val context = LocalContext.current
    val emptyIndex = -1
    val chartDegrees = 360f // circle shape

    // start drawing clockwise (top to right)
    var startAngle = 270f

    // calculate each input percentage
    val proportions = inputValues.map {
        it * 100 / inputValues.sum()
    }

    // calculate each input slice degrees
    val angleProgress = proportions.map { prop ->
        chartDegrees * prop / 100
    }

    // clicked slice index
    var clickedItemIndex by remember {
        mutableStateOf(emptyIndex)
    }

    // calculate each slice end point in degrees, for handling click position
    val progressSize = mutableListOf<Float>()

    LaunchedEffect(angleProgress) {
        progressSize.add(angleProgress.first())
        for (x in 1 until angleProgress.size) {
            progressSize.add(angleProgress[x] + progressSize[x - 1])
        }
    }

    // text style
    val density = LocalDensity.current
    val textFontSize = with(density) { 30.dp.toPx() }
    val textPaint = remember {
        Paint().apply {
            color = textColor.toArgb()
            textSize = textFontSize
            textAlign = Paint.Align.CENTER
        }
    }

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {

        val canvasSize = min(constraints.maxWidth, constraints.maxHeight)
        val size = Size(canvasSize.toFloat(), canvasSize.toFloat())
        val canvasSizeDp = with(density) { canvasSize.toDp() }

        Canvas(
            modifier = Modifier
                .size(canvasSizeDp)
                .pointerInput(inputValues) {

                    detectTapGestures { offset ->
                        val clickedAngle = touchPointToAngle(
                            width = canvasSize.toFloat(),
                            height = canvasSize.toFloat(),
                            touchX = offset.x,
                            touchY = offset.y,
                            chartDegrees = chartDegrees
                        )
                        progressSize.forEachIndexed { index, item ->
                            if (clickedAngle <= item) {
                                clickedItemIndex = index
                                return@detectTapGestures
                            }
                        }
                    }
                }
        ) {

            angleProgress.forEachIndexed { index, angle ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = angle,
                    useCenter = false,
                    size = size,
                    style = Stroke(width = with(density) { 60.dp.toPx() })
                )
                startAngle += angle
            }

            if (clickedItemIndex != emptyIndex) {
                    Toast.makeText(context, clickedItemIndex.toString(), Toast.LENGTH_SHORT).show()
//                drawIntoCanvas { canvas ->
//
//                    canvas.nativeCanvas.drawText(
//                        "${proportions[clickedItemIndex].roundToInt()}%",
//                        (canvasSize / 2) + textFontSize / 4,
//                        (canvasSize / 2) + textFontSize / 4,
//                        textPaint
//                    )
//                }
            }
        }
    }
}

fun touchPointToAngle(
    width: Float,
    height: Float,
    touchX: Float,
    touchY: Float,
    chartDegrees: Float
): Double {
    val x = touchX - (width * 0.5f)
    val y = touchY - (height * 0.5f)
    var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
    angle = if (angle < 0) angle + chartDegrees else angle
    return angle
}

fun generateColors(numColors: Int): List<Color> {
    return List(numColors) {
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat()
        )
    }
}