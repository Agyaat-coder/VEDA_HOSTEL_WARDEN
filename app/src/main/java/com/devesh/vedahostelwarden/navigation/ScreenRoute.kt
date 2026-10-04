package com.devesh.vedahostelwarden.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreenRoute(val route: String) {
    object Splash : ScreenRoute("splash")
    object Login : ScreenRoute("login")
    object Main : ScreenRoute("main")

    // Sub-routes
    object StudentDetails : ScreenRoute("student_details/{studentId}") {
        fun createRoute(studentId: String) = "student_details/$studentId"
    }
    object AddStudent : ScreenRoute("add_student")
    object ActivationCode : ScreenRoute("activation_code/{studentId}") {
        fun createRoute(studentId: String) = "activation_code/$studentId"
    }
    object CreateAttendanceSession : ScreenRoute("create_attendance_session")
    object LiveAttendance : ScreenRoute("live_attendance/{sessionId}") {
        fun createRoute(sessionId: String) = "live_attendance/$sessionId"
    }
    object AttendanceHistory : ScreenRoute("attendance_history")
    object CreateNotice : ScreenRoute("create_notice")
    object HostelDetails : ScreenRoute("hostel_details")
    object CreateHostel : ScreenRoute("create_hostel")
    object EditHostel : ScreenRoute("edit_hostel")
    object ManageRooms : ScreenRoute("manage_rooms")
    object Contacts : ScreenRoute("contacts")
    object MessMenu : ScreenRoute("mess_menu")
    object Reports : ScreenRoute("reports")
    object Profile : ScreenRoute("profile")
    object Settings : ScreenRoute("settings")
    object HelpFeedback : ScreenRoute("help_feedback")
    object AboutVeda : ScreenRoute("about_veda")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home_tab", "HOME", Icons.Filled.Home)
    object Students : BottomNavItem("students_tab", "STUDENTS", Icons.Filled.People)
    object Attendance : BottomNavItem("attendance_tab", "ATTENDANCE", Icons.Filled.Assignment)
    object Notices : BottomNavItem("notices_tab", "NOTICES", Icons.Filled.Campaign)
    object More : BottomNavItem("more_tab", "MORE", Icons.Filled.MoreHoriz)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Students,
    BottomNavItem.Attendance,
    BottomNavItem.Notices,
    BottomNavItem.More
)
