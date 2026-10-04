package com.devesh.vedahostelwarden.data.model

enum class ActivationStatus {
    ACTIVE,
    PENDING_ACTIVATION,
    INACTIVE
}

enum class SessionType {
    MORNING,
    EVENING
}

enum class SessionStatus {
    SCHEDULED,
    ACTIVE,
    CLOSED,
    CANCELLED
}

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    NOT_MARKED
}

enum class TargetAudience {
    ALL_STUDENTS,
    SPECIFIC_HOSTEL,
    SPECIFIC_ROOM
}

enum class NoticeStatus {
    PUBLISHED,
    SCHEDULED,
    DRAFT,
    ARCHIVED
}

data class Warden(
    val wardenId: String,
    val name: String,
    val role: String,
    val email: String,
    val phone: String,
    val hostelId: String,
    val hostelName: String,
    val profileImageUrl: String? = null
)

data class Student(
    val studentId: String,
    val fullName: String,
    val rollNumber: String,
    val email: String,
    val phone: String,
    val course: String,
    val branch: String,
    val year: String,
    val hostelId: String,
    val hostelName: String,
    val roomId: String,
    val roomNumber: String,
    val floor: String,
    val activationStatus: ActivationStatus,
    val activationCode: String,
    val createdAt: String
)

data class Hostel(
    val hostelId: String,
    val name: String,
    val location: String,
    val address: String = "Block A, Campus Area",
    val city: String = "New Delhi",
    val state: String = "Delhi",
    val pinCode: String = "110001",
    val description: String = "",
    val wardenId: String,
    val wardenName: String,
    val totalStudents: Int = 0,
    val totalRooms: Int = 0,
    val totalFloors: Int = 1,
    val occupancyPercentage: Int = 0,
    val createdAt: String = "",
    val updatedAt: String = "",
    val isActive: Boolean = true
)

data class Room(
    val roomId: String,
    val hostelId: String,
    val roomNumber: String,
    val floor: String,
    val capacity: Int,
    val occupied: Int
) {
    val available: Int
        get() = (capacity - occupied).coerceAtLeast(0)
    val isFull: Boolean
        get() = occupied >= capacity
}

data class AttendanceSession(
    val sessionId: String,
    val hostelId: String,
    val hostelName: String,
    val sessionType: SessionType,
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: SessionStatus,
    val createdBy: String,
    val createdAt: String
)

data class AttendanceRecord(
    val recordId: String,
    val sessionId: String,
    val studentId: String,
    val studentName: String,
    val rollNumber: String,
    val roomNumber: String,
    val status: AttendanceStatus,
    val markedAt: String? = null
)

data class Notice(
    val noticeId: String,
    val title: String,
    val message: String,
    val targetAudience: TargetAudience,
    val targetDetail: String = "All Students",
    val scheduleTime: String = "Send Now",
    val status: NoticeStatus,
    val createdBy: String,
    val createdAt: String
)

data class MessMenu(
    val menuId: String,
    val hostelId: String,
    val dayOfWeek: String,
    val breakfast: String,
    val lunch: String,
    val snacks: String,
    val dinner: String,
    val specialNote: String = ""
)

data class HostelContact(
    val contactId: String,
    val hostelId: String,
    val name: String,
    val role: String,
    val phone: String,
    val isEmergency: Boolean = false
)

data class ActivationCode(
    val codeId: String,
    val code: String,
    val studentId: String,
    val studentName: String,
    val hostelId: String,
    val createdBy: String,
    val createdAt: String,
    val isUsed: Boolean
)
