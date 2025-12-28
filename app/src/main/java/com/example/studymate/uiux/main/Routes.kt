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

    const val TIMER = "timer/{materiId}/{materiName}"
    fun timer(materiId: Int, materiName: String): String {
        return "timer/$materiId/$materiName"
    }

    const val SESSION_HISTORY = "session_history"

    const val STATS = "stats"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
}
