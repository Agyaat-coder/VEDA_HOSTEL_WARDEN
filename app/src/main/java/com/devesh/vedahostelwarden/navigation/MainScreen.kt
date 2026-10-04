package com.devesh.vedahostelwarden.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devesh.vedahostelwarden.ui.screens.attendance.AttendanceMainScreen
import com.devesh.vedahostelwarden.ui.screens.dashboard.DashboardScreen
import com.devesh.vedahostelwarden.ui.screens.more.MoreScreen
import com.devesh.vedahostelwarden.ui.screens.notices.NoticesScreen
import com.devesh.vedahostelwarden.ui.screens.students.StudentListScreen
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.*

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    studentViewModel: StudentViewModel,
    attendanceViewModel: AttendanceViewModel,
    hostelViewModel: HostelViewModel,
    noticeViewModel: NoticeViewModel,
    messMenuViewModel: MessMenuViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateToCreateHostel: () -> Unit,
    onNavigateToEditHostel: () -> Unit,
    onNavigateToAddStudent: () -> Unit,
    onNavigateToStudentDetails: (String) -> Unit,
    onNavigateToCreateSession: () -> Unit,
    onNavigateToLiveAttendance: (String) -> Unit,
    onNavigateToAttendanceHistory: () -> Unit,
    onNavigateToCreateNotice: () -> Unit,
    onNavigateToHostel: () -> Unit,
    onNavigateToRooms: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToMessMenu: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = VedaNavySurface,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VedaTextPrimary,
                            selectedTextColor = VedaBlueLight,
                            indicatorColor = VedaBluePrimary,
                            unselectedIconColor = VedaTextSecondary,
                            unselectedTextColor = VedaTextSecondary
                        )
                    )
                }
            }
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                DashboardScreen(
                    dashboardViewModel = dashboardViewModel,
                    onNavigateToCreateHostel = onNavigateToCreateHostel,
                    onNavigateToStudents = {
                        bottomNavController.navigate(BottomNavItem.Students.route)
                    },
                    onNavigateToAddStudent = onNavigateToAddStudent,
                    onNavigateToAttendance = {
                        bottomNavController.navigate(BottomNavItem.Attendance.route)
                    },
                    onNavigateToLiveAttendance = onNavigateToLiveAttendance,
                    onNavigateToCreateSession = onNavigateToCreateSession,
                    onNavigateToRooms = onNavigateToRooms,
                    onNavigateToMessMenu = onNavigateToMessMenu,
                    onNavigateToNotices = {
                        bottomNavController.navigate(BottomNavItem.Notices.route)
                    }
                )
            }

            composable(BottomNavItem.Students.route) {
                StudentListScreen(
                    studentViewModel = studentViewModel,
                    onNavigateToAddStudent = onNavigateToAddStudent,
                    onNavigateToStudentDetails = onNavigateToStudentDetails
                )
            }

            composable(BottomNavItem.Attendance.route) {
                AttendanceMainScreen(
                    attendanceViewModel = attendanceViewModel,
                    onCreateSessionClick = onNavigateToCreateSession,
                    onLiveAttendanceClick = onNavigateToLiveAttendance,
                    onHistoryClick = onNavigateToAttendanceHistory
                )
            }

            composable(BottomNavItem.Notices.route) {
                NoticesScreen(
                    noticeViewModel = noticeViewModel,
                    onCreateNoticeClick = onNavigateToCreateNotice
                )
            }

            composable(BottomNavItem.More.route) {
                MoreScreen(
                    profileViewModel = profileViewModel,
                    authViewModel = authViewModel,
                    onNavigateToHostel = onNavigateToHostel,
                    onNavigateToRooms = onNavigateToRooms,
                    onNavigateToContacts = onNavigateToContacts,
                    onNavigateToMessMenu = onNavigateToMessMenu,
                    onNavigateToReports = onNavigateToReports,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToSettings = onNavigateToSettings,
                    onNavigateToHelp = onNavigateToHelp,
                    onNavigateToAbout = onNavigateToAbout,
                    onLoggedOut = onLoggedOut
                )
            }
        }
    }
}
