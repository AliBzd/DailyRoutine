package com.example.data

enum class TimeOfDay(val label: String, val timeRange: String, val iconName: String) {
    MORNING("Morning", "06:00 - 11:00", "wb_sunny"),
    AFTERNOON("Afternoon", "11:00 - 17:00", "wb_twilight"),
    EVENING("Evening", "17:00 - 22:00", "nights_stay"),
    ANYTIME("Anytime", "Flex Schedule", "schedule");

    companion object {
        fun fromString(value: String): TimeOfDay {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                ANYTIME
            }
        }
    }
}
