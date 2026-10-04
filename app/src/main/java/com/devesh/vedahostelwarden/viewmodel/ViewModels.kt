package com.devesh.vedahostelwarden.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devesh.vedahostelwarden.data.datasource.LocalMockDataSource
import com.devesh.vedahostelwarden.data.model.*
import com.devesh.vedahostelwarden.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepository = LocalMockDataSource
) : ViewModel() {
    val currentWarden: StateFlow<Warden?> = authRepo.currentWarden
    val isAuthenticated: StateFlow<Boolean> = authRepo.isAuthenticated

    private val _isAuthenticating = MutableStateFlow(false)
    val isAuthenticating: StateFlow<Boolean> = _isAuthenticating.asStateFlow()

    fun initSession(context: Context) {
        viewModelScope.launch {
            authRepo.initSession(context)
        }
    }

    fun login(context: Context, email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_isAuthenticating.value) return
        _isAuthenticating.value = true
        viewModelScope.launch {
            val result = authRepo.login(context, email, pass)
            _isAuthenticating.value = false
            result.onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Invalid email or password.") }
        }
    }

    fun logout(context: Context, onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepo.logout(context)
            onLoggedOut()
        }
    }
}

class DashboardViewModel(
    authRepo: AuthRepository = LocalMockDataSource,
    studentRepo: StudentRepository = LocalMockDataSource,
    hostelRepo: HostelRepository = LocalMockDataSource,
    attendanceRepo: AttendanceRepository = LocalMockDataSource,
    noticeRepo: NoticeRepository = LocalMockDataSource
) : ViewModel() {

    val warden: StateFlow<Warden?> = authRepo.currentWarden
    val hostel: StateFlow<Hostel?> = hostelRepo.hostel

    val studentsCount: StateFlow<Int> = combine(hostelRepo.hostel, studentRepo.students) { h, sList ->
        if (h == null) 0 else sList.size
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val pendingCount: StateFlow<Int> = combine(hostelRepo.hostel, studentRepo.students) { h, sList ->
        if (h == null) 0 else sList.count { it.activationStatus == ActivationStatus.PENDING_ACTIVATION }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val activeSessionsCount: StateFlow<Int> = combine(hostelRepo.hostel, attendanceRepo.activeSession) { h, sess ->
        if (h == null || sess == null) 0 else 1
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val noticesCount: StateFlow<Int> = combine(hostelRepo.hostel, noticeRepo.notices) { h, nList ->
        if (h == null) 0 else nList.size
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val roomsCount: StateFlow<Int> = combine(hostelRepo.hostel, hostelRepo.rooms) { h, rList ->
        if (h == null) 0 else rList.size
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val activeSession: StateFlow<AttendanceSession?> = attendanceRepo.activeSession
}

class StudentViewModel(
    private val studentRepo: StudentRepository = LocalMockDataSource,
    private val hostelRepo: HostelRepository = LocalMockDataSource
) : ViewModel() {

    val students: StateFlow<List<Student>> = studentRepo.students
    val rooms: StateFlow<List<Room>> = hostelRepo.rooms

    fun getStudentById(studentId: String): Student? = studentRepo.getStudentById(studentId)

    fun addStudent(
        fullName: String,
        rollNumber: String,
        email: String,
        phone: String,
        course: String,
        branch: String,
        year: String,
        hostelId: String,
        hostelName: String,
        roomId: String,
        roomNumber: String,
        floor: String,
        onSuccess: (Student) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = studentRepo.addStudent(
                fullName, rollNumber, email, phone, course, branch, year,
                hostelId, hostelName, roomId, roomNumber, floor
            )
            result.onSuccess { onSuccess(it) }
                .onFailure { onError(it.message ?: "Failed to add student") }
        }
    }

    fun generateNewCode(studentId: String, onGenerated: (String) -> Unit) {
        viewModelScope.launch {
            val newCode = studentRepo.generateActivationCode(studentId)
            onGenerated(newCode)
        }
    }

    fun simulateActivation(studentId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val res = studentRepo.simulateStudentActivation(studentId)
            res.onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Activation simulation failed") }
        }
    }
}

class AttendanceViewModel(
    private val attendanceRepo: AttendanceRepository = LocalMockDataSource
) : ViewModel() {

    val sessions: StateFlow<List<AttendanceSession>> = attendanceRepo.sessions
    val activeSession: StateFlow<AttendanceSession?> = attendanceRepo.activeSession
    val currentRecords: StateFlow<List<AttendanceRecord>> = attendanceRepo.currentRecords

    fun createSession(
        sessionType: SessionType,
        date: String,
        startTime: String,
        endTime: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val res = attendanceRepo.createSession(sessionType, date, startTime, endTime)
            res.onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Failed to create session") }
        }
    }

    fun toggleRecordStatus(recordId: String, currentStatus: AttendanceStatus) {
        val nextStatus = when (currentStatus) {
            AttendanceStatus.NOT_MARKED -> AttendanceStatus.PRESENT
            AttendanceStatus.PRESENT -> AttendanceStatus.ABSENT
            AttendanceStatus.ABSENT -> AttendanceStatus.PRESENT
        }
        viewModelScope.launch {
            attendanceRepo.updateRecordStatus(recordId, nextStatus)
        }
    }

    fun markRecordStatus(recordId: String, status: AttendanceStatus) {
        viewModelScope.launch {
            attendanceRepo.updateRecordStatus(recordId, status)
        }
    }

    fun closeActiveSession(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val res = attendanceRepo.closeActiveSession()
            res.onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Failed to close session") }
        }
    }
}

class HostelViewModel(
    private val hostelRepo: HostelRepository = LocalMockDataSource
) : ViewModel() {

    val hostel: StateFlow<Hostel?> = hostelRepo.hostel
    val rooms: StateFlow<List<Room>> = hostelRepo.rooms
    val contacts: StateFlow<List<HostelContact>> = hostelRepo.contacts

    fun createHostel(
        context: Context,
        name: String,
        location: String,
        address: String,
        city: String,
        state: String,
        pinCode: String,
        floors: Int,
        roomsCount: Int,
        description: String,
        onSuccess: (Hostel) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val res = hostelRepo.createHostel(context, name, location, address, city, state, pinCode, floors, roomsCount, description)
            res.onSuccess { onSuccess(it) }
                .onFailure { onError(it.message ?: "Failed to create hostel.") }
        }
    }

    fun updateHostel(
        context: Context,
        name: String,
        location: String,
        address: String,
        city: String,
        state: String,
        pinCode: String,
        description: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val res = hostelRepo.updateHostel(context, name, location, address, city, state, pinCode, description)
            res.onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Failed to update hostel.") }
        }
    }

    fun addContact(name: String, role: String, phone: String, isEmergency: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            hostelRepo.addContact(name, role, phone, isEmergency)
            onSuccess()
        }
    }

    fun updateContact(contact: HostelContact, onSuccess: () -> Unit) {
        viewModelScope.launch {
            hostelRepo.updateContact(contact)
            onSuccess()
        }
    }

    fun deleteContact(contactId: String) {
        viewModelScope.launch {
            hostelRepo.deleteContact(contactId)
        }
    }
}

class NoticeViewModel(
    private val noticeRepo: NoticeRepository = LocalMockDataSource
) : ViewModel() {

    val notices: StateFlow<List<Notice>> = noticeRepo.notices

    fun createNotice(
        title: String,
        message: String,
        targetAudience: TargetAudience,
        targetDetail: String,
        scheduleTime: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val res = noticeRepo.createNotice(title, message, targetAudience, targetDetail, scheduleTime)
            res.onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Failed to create notice") }
        }
    }

    fun deleteNotice(noticeId: String) {
        viewModelScope.launch {
            noticeRepo.deleteNotice(noticeId)
        }
    }
}

class MessMenuViewModel(
    private val messRepo: MessRepository = LocalMockDataSource
) : ViewModel() {

    val messMenu: StateFlow<List<MessMenu>> = messRepo.messMenu

    fun updateMenuForDay(
        dayOfWeek: String,
        breakfast: String,
        lunch: String,
        snacks: String,
        dinner: String,
        specialNote: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            messRepo.updateMenuForDay(dayOfWeek, breakfast, lunch, snacks, dinner, specialNote)
            onSuccess()
        }
    }
}

class ProfileViewModel(
    private val authRepo: AuthRepository = LocalMockDataSource,
    private val hostelRepo: HostelRepository = LocalMockDataSource
) : ViewModel() {

    val warden: StateFlow<Warden?> = authRepo.currentWarden
    val hostel: StateFlow<Hostel?> = hostelRepo.hostel
}
