package com.devesh.vedahostelwarden.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devesh.vedahostelwarden.ui.screens.attendance.*
import com.devesh.vedahostelwarden.ui.screens.auth.LoginScreen
import com.devesh.vedahostelwarden.ui.screens.auth.SplashScreen
import com.devesh.vedahostelwarden.ui.screens.hostel.*
import com.devesh.vedahostelwarden.ui.screens.more.*
import com.devesh.vedahostelwarden.ui.screens.notices.CreateNoticeScreen
import com.devesh.vedahostelwarden.ui.screens.students.ActivationCodeScreen
import com.devesh.vedahostelwarden.ui.screens.students.AddStudentScreen
import com.devesh.vedahostelwarden.ui.screens.students.StudentDetailsScreen
import com.devesh.vedahostelwarden.viewmodel.*

@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = viewModel(),
    dashboardViewModel: DashboardViewModel = viewModel(),
    studentViewModel: StudentViewModel = viewModel(),
    attendanceViewModel: AttendanceViewModel = viewModel(),
    hostelViewModel: HostelViewModel = viewModel(),
    noticeViewModel: NoticeViewModel = viewModel(),
    messMenuViewModel: MessMenuViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val rootNavController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    NavHost(
        navController = rootNavController,
        startDestination = ScreenRoute.Splash.route
    ) {
        composable(ScreenRoute.Splash.route) {
            SplashScreen(
                authViewModel = authViewModel,
                onNavigateNext = {
                    if (isAuthenticated) {
                        rootNavController.navigate(ScreenRoute.Main.route) {
                            popUpTo(ScreenRoute.Splash.route) { inclusive = true }
                        }
                    } else {
                        rootNavController.navigate(ScreenRoute.Login.route) {
                            popUpTo(ScreenRoute.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(ScreenRoute.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    rootNavController.navigate(ScreenRoute.Main.route) {
                        popUpTo(ScreenRoute.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(ScreenRoute.Main.route) {
            MainScreen(
                authViewModel = authViewModel,
                dashboardViewModel = dashboardViewModel,
                studentViewModel = studentViewModel,
                attendanceViewModel = attendanceViewModel,
                hostelViewModel = hostelViewModel,
                noticeViewModel = noticeViewModel,
                messMenuViewModel = messMenuViewModel,
                profileViewModel = profileViewModel,
                onNavigateToCreateHostel = {
                    rootNavController.navigate(ScreenRoute.CreateHostel.route)
                },
                onNavigateToEditHostel = {
                    rootNavController.navigate(ScreenRoute.EditHostel.route)
                },
                onNavigateToAddStudent = {
                    rootNavController.navigate(ScreenRoute.AddStudent.route)
                },
                onNavigateToStudentDetails = { studentId ->
                    rootNavController.navigate(ScreenRoute.StudentDetails.createRoute(studentId))
                },
                onNavigateToCreateSession = {
                    rootNavController.navigate(ScreenRoute.CreateAttendanceSession.route)
                },
                onNavigateToLiveAttendance = { sessionId ->
                    rootNavController.navigate(ScreenRoute.LiveAttendance.createRoute(sessionId))
                },
                onNavigateToAttendanceHistory = {
                    rootNavController.navigate(ScreenRoute.AttendanceHistory.route)
                },
                onNavigateToCreateNotice = {
                    rootNavController.navigate(ScreenRoute.CreateNotice.route)
                },
                onNavigateToHostel = {
                    rootNavController.navigate(ScreenRoute.HostelDetails.route)
                },
                onNavigateToRooms = {
                    rootNavController.navigate(ScreenRoute.ManageRooms.route)
                },
                onNavigateToContacts = {
                    rootNavController.navigate(ScreenRoute.Contacts.route)
                },
                onNavigateToMessMenu = {
                    rootNavController.navigate(ScreenRoute.MessMenu.route)
                },
                onNavigateToReports = {
                    rootNavController.navigate(ScreenRoute.Reports.route)
                },
                onNavigateToProfile = {
                    rootNavController.navigate(ScreenRoute.Profile.route)
                },
                onNavigateToSettings = {
                    rootNavController.navigate(ScreenRoute.Settings.route)
                },
                onNavigateToHelp = {
                    rootNavController.navigate(ScreenRoute.HelpFeedback.route)
                },
                onNavigateToAbout = {
                    rootNavController.navigate(ScreenRoute.AboutVeda.route)
                },
                onLoggedOut = {
                    rootNavController.navigate(ScreenRoute.Login.route) {
                        popUpTo(ScreenRoute.Main.route) { inclusive = true }
                    }
                }
            )
        }

        // Sub-route composables
        composable(ScreenRoute.CreateHostel.route) {
            CreateHostelScreen(
                hostelViewModel = hostelViewModel,
                onNavigateBack = { rootNavController.popBackStack() },
                onHostelCreated = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.EditHostel.route) {
            EditHostelScreen(
                hostelViewModel = hostelViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(
            route = ScreenRoute.StudentDetails.route,
            arguments = listOf(navArgument("studentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            StudentDetailsScreen(
                studentId = studentId,
                studentViewModel = studentViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.AddStudent.route) {
            AddStudentScreen(
                studentViewModel = studentViewModel,
                onNavigateBack = { rootNavController.popBackStack() },
                onStudentCreated = { createdId ->
                    rootNavController.navigate(ScreenRoute.ActivationCode.createRoute(createdId)) {
                        popUpTo(ScreenRoute.AddStudent.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = ScreenRoute.ActivationCode.route,
            arguments = listOf(navArgument("studentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            ActivationCodeScreen(
                studentId = studentId,
                studentViewModel = studentViewModel,
                onDoneClick = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.CreateAttendanceSession.route) {
            CreateAttendanceSessionScreen(
                attendanceViewModel = attendanceViewModel,
                onNavigateBack = { rootNavController.popBackStack() },
                onSessionCreated = {
                    val active = attendanceViewModel.activeSession.value
                    if (active != null) {
                        rootNavController.navigate(ScreenRoute.LiveAttendance.createRoute(active.sessionId)) {
                            popUpTo(ScreenRoute.CreateAttendanceSession.route) { inclusive = true }
                        }
                    } else {
                        rootNavController.popBackStack()
                    }
                }
            )
        }

        composable(
            route = ScreenRoute.LiveAttendance.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            LiveAttendanceScreen(
                sessionId = sessionId,
                attendanceViewModel = attendanceViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.AttendanceHistory.route) {
            AttendanceHistoryScreen(
                attendanceViewModel = attendanceViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.CreateNotice.route) {
            CreateNoticeScreen(
                noticeViewModel = noticeViewModel,
                onNavigateBack = { rootNavController.popBackStack() },
                onNoticePublished = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.HostelDetails.route) {
            HostelManagementScreen(
                hostelViewModel = hostelViewModel,
                onNavigateToCreateHostel = { rootNavController.navigate(ScreenRoute.CreateHostel.route) },
                onNavigateToEditHostel = { rootNavController.navigate(ScreenRoute.EditHostel.route) },
                onNavigateToRooms = { rootNavController.navigate(ScreenRoute.ManageRooms.route) },
                onNavigateToContacts = { rootNavController.navigate(ScreenRoute.Contacts.route) },
                onNavigateToMessMenu = { rootNavController.navigate(ScreenRoute.MessMenu.route) },
                onNavigateToReports = { rootNavController.navigate(ScreenRoute.Reports.route) }
            )
        }

        composable(ScreenRoute.ManageRooms.route) {
            ManageRoomsScreen(
                hostelViewModel = hostelViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.Contacts.route) {
            ContactsScreen(
                hostelViewModel = hostelViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.MessMenu.route) {
            MessMenuScreen(
                messMenuViewModel = messMenuViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.Reports.route) {
            ReportsScreen(
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.Profile.route) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.Settings.route) {
            SettingsScreen(
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.HelpFeedback.route) {
            HelpFeedbackScreen(
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable(ScreenRoute.AboutVeda.route) {
            AboutVedaScreen(
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }
    }
}
