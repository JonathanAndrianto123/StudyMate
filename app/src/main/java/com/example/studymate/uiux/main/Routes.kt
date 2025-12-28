package com.example.studymate.uiux.main

object Routes {
    const val WELCOME = "welcome"
    const val SIGN_UP = "signup"
    const val SIGN_IN = "signin"

    const val MATERI_LIST = "materi_list"
    const val ADD_MATERI = "add_materi"
    const val DETAIL_MATERI = "detail_materi/{materiId}"
    fun detailMateri(materiId: Int): String {
        return "detail_materi/$materiId"
    }

    const val TIMER = "timer/{materiId}/{materiName}/{targetTime}"
    fun timer(materiId: Int, materiName: String, targetTime: String): String {
        return "timer/$materiId/$materiName/$targetTime"
    }

    const val STUDY_HISTORY = "study_history/{materiId}"
    fun studyHistory(materiId: Int): String {
        return "study_history/$materiId"
    }

    const val SESSION_HISTORY = "session_history"

    const val STATS = "stats"
    const val PROFILE = "profile"
}
