package com.devesh.vedahostelwarden.data.repository

import android.content.Context
import com.devesh.vedahostelwarden.data.model.*
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentWarden: StateFlow<Warden?>
    val isAuthenticated: StateFlow<Boolean>
    suspend fun initSession(context: Context)
    suspend fun login(context: Context, email: String, pass: String): Result<Warden>
    suspend fun logout(context: Context)
}

interface StudentRepository {
    val students: StateFlow<List<Student>>
    fun getStudentById(studentId: String): Student?
    suspend fun addStudent(
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
        floor: String
    ): Result<Student>
    suspend fun updateStudent(student: Student): Result<Unit>
    suspend fun generateActivationCode(studentId: String): String
    suspend fun simulateStudentActivation(studentId: String): Result<Unit>
}

interface HostelRepository {
    val hostel: StateFlow<Hostel?>
    val rooms: StateFlow<List<Room>>
    val contacts: StateFlow<List<HostelContact>>
    fun getRoomById(roomId: String): Room?
    suspend fun createHostel(
        context: Context,
        name: String,
        location: String,
        address: String,
        city: String,
        state: String,
        pinCode: String,
        floors: Int,
        roomsCount: Int,
        description: String
    ): Result<Hostel>
    suspend fun updateHostel(
        context: Context,
        name: String,
        location: String,
        address: String,
        city: String,
        state: String,
        pinCode: String,
        description: String
    ): Result<Unit>
    suspend fun addContact(name: String, role: String, phone: String, isEmergency: Boolean): Result<Unit>
    suspend fun updateContact(contact: HostelContact): Result<Unit>
    suspend fun deleteContact(contactId: String): Result<Unit>
}

interface AttendanceRepository {
    val sessions: StateFlow<List<AttendanceSession>>
    val activeSession: StateFlow<AttendanceSession?>
    val currentRecords: StateFlow<List<AttendanceRecord>>
    suspend fun createSession(
        sessionType: SessionType,
        date: String,
        startTime: String,
        endTime: String
    ): Result<AttendanceSession>
    suspend fun updateRecordStatus(recordId: String, status: AttendanceStatus): Result<Unit>
    suspend fun closeActiveSession(): Result<Unit>
}

interface NoticeRepository {
    val notices: StateFlow<List<Notice>>
    suspend fun createNotice(
        title: String,
        message: String,
        targetAudience: TargetAudience,
        targetDetail: String,
        scheduleTime: String
    ): Result<Notice>
    suspend fun deleteNotice(noticeId: String): Result<Unit>
}

interface MessRepository {
    val messMenu: StateFlow<List<MessMenu>>
    suspend fun updateMenuForDay(
        dayOfWeek: String,
        breakfast: String,
        lunch: String,
        snacks: String,
        dinner: String,
        specialNote: String
    ): Result<Unit>
}
