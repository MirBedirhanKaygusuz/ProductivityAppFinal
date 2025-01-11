package com.example.productivityapptest

data class DailyStats(
    val date: String,        // Günün tarihi
    var totalTasks: Int = 0, // O günkü toplam görev sayısı
    var completedTasks: Int = 0 // Tamamlanan görev sayısı
)