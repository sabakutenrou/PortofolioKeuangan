package com.example.portfoliokeuangan.data

// Data class to represent the "data" inside the "donutChart" object
data class TransactionData(
    val trx_date: String,
    val nominal: Int
)

// Data class to represent the "donutChart" object
data class DonutChartData(
    val label: String,
    val percentage: String,
    val data: List<TransactionData>
)

// Data class to represent the "lineChart" object
data class LineChartData(
    val month: List<Int>
)

// Data class to represent the main JSON structure containing both types of charts
data class Portfolio(
    val type: String,
    val data: Any // We use Any here to store both DonutChartData and LineChartData objects
)

