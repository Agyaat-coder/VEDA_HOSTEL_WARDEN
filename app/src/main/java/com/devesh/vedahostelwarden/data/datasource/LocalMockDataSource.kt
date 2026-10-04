package com.devesh.vedahostelwarden.data.datasource

import android.content.Context
import com.devesh.vedahostelwarden.data.model.*
import com.devesh.vedahostelwarden.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LocalMockDataSource : AuthRepository, StudentRepository, HostelRepository, AttendanceRepository, NoticeRepository, MessRepository {

    // --- MOCK WARDEN & AUTH ---
    private val defaultWarden = Warden(
        wardenId = "WARDEN_101",
        name = "Dr. S. Mishra",
        role = "Chief Hostel Warden",
        email = "warden@veda.edu.in",
        phone = "+91 98765 43210",
        hostelId = "HOSTEL_CHARAK",
        hostelName = "Charak Chatras"
    )

    private val _currentWarden = MutableStateFlow<Warden?>(null)
    override val currentWarden: StateFlow<Warden?> = _currentWarden.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    override val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    override suspend fun initSession(context: Context) {
        val prefs = UserPreferencesRepository(context)
        val isLoggedIn = prefs.isLoggedIn()
        if (isLoggedIn) {
            val email = prefs.getLoggedInEmail() ?: "warden@veda.edu.in"
            _currentWarden.value = defaultWarden.copy(email = email)
            _isAuthenticated.value = true
        } else {
            _currentWarden.value = null
            _isAuthenticated.value = false
        }
    }

    override suspend fun login(context: Context, email: String, pass: String): Result<Warden> {
        if (email.isBlank()) {
            return Result.failure(Exception("Enter your email."))
        }
        if (pass.isBlank()) {
            return Result.failure(Exception("Enter your password."))
        }
        if (email.trim().lowercase() != "warden@veda.edu.in" && !email.contains("@")) {
            return Result.failure(Exception("Invalid email or password."))
        }

        val warden = defaultWarden.copy(email = email)
        _currentWarden.value = warden
        _isAuthenticated.value = true

        val prefs = UserPreferencesRepository(context)
        prefs.saveUserSession(email, _hostel.value?.hostelId)

        return Result.success(warden)
    }

    override suspend fun logout(context: Context) {
        _isAuthenticated.value = false
        _currentWarden.value = null
        val prefs = UserPreferencesRepository(context)
        prefs.clearUserSession()
    }

    // --- HOSTEL & ROOMS ---
    private val defaultHostel = Hostel(
        hostelId = "HOSTEL_CHARAK",
        name = "Charak Chatras",
        location = "North Campus, Block A",
        address = "Sector 14, University Road",
        city = "New Delhi",
        state = "Delhi",
        pinCode = "110001",
        description = "Official campus hostel for senior students.",
        wardenId = "WARDEN_101",
        wardenName = "Dr. S. Mishra",
        totalStudents = 128,
        totalRooms = 60,
        totalFloors = 4,
        occupancyPercentage = 92,
        createdAt = "01 Sep 2026",
        isActive = true
    )

    private val _hostel = MutableStateFlow<Hostel?>(defaultHostel)
    override val hostel: StateFlow<Hostel?> = _hostel.asStateFlow()

    private val initialRooms = buildList {
        for (floorNum in 1..4) {
            for (roomIndex in 1..15) {
                val roomNo = "${floorNum}${if (roomIndex < 10) "0$roomIndex" else "$roomIndex"}"
                val cap = if (roomIndex % 2 == 0) 2 else 3
                val occ = if (roomNo == "103") 1 else cap
                add(
                    Room(
                        roomId = "ROOM_$roomNo",
                        hostelId = "HOSTEL_CHARAK",
                        roomNumber = roomNo,
                        floor = "Floor $floorNum",
                        capacity = cap,
                        occupied = occ
                    )
                )
            }
        }
    }
    private val _rooms = MutableStateFlow(initialRooms)
    override val rooms: StateFlow<List<Room>> = _rooms.asStateFlow()

    override fun getRoomById(roomId: String): Room? = _rooms.value.find { it.roomId == roomId }

    override suspend fun createHostel(
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
    ): Result<Hostel> {
        if (name.isBlank()) return Result.failure(Exception("Hostel name cannot be empty."))
        if (location.isBlank() && address.isBlank()) return Result.failure(Exception("Hostel location or address cannot be empty."))
        if (floors <= 0) return Result.failure(Exception("Number of floors must be greater than 0."))
        if (roomsCount <= 0) return Result.failure(Exception("Total rooms must be greater than 0."))

        val newHostelId = "HOSTEL_${System.currentTimeMillis()}"
        val wardenName = _currentWarden.value?.name ?: "Dr. S. Mishra"
        val wardenId = _currentWarden.value?.wardenId ?: "WARDEN_101"

        val newHostel = Hostel(
            hostelId = newHostelId,
            name = name,
            location = location.ifBlank { address },
            address = address.ifBlank { location },
            city = city.ifBlank { "Campus City" },
            state = state.ifBlank { "State" },
            pinCode = pinCode.ifBlank { "000000" },
            description = description,
            wardenId = wardenId,
            wardenName = wardenName,
            totalStudents = _students.value.size,
            totalRooms = roomsCount,
            totalFloors = floors,
            occupancyPercentage = 0,
            createdAt = getCurrentDateFormatted(),
            isActive = true
        )

        // Generate rooms auto-assigned to new hostel
        val roomsPerFloor = (roomsCount / floors).coerceAtLeast(1)
        val generatedRooms = buildList {
            for (f in 1..floors) {
                for (r in 1..roomsPerFloor) {
                    val rNo = "${f}${if (r < 10) "0$r" else "$r"}"
                    add(
                        Room(
                            roomId = "ROOM_${newHostelId}_$rNo",
                            hostelId = newHostelId,
                            roomNumber = rNo,
                            floor = "Floor $f",
                            capacity = 2,
                            occupied = 0
                        )
                    )
                }
            }
        }

        _hostel.value = newHostel
        _rooms.value = generatedRooms
        _currentWarden.value = _currentWarden.value?.copy(hostelId = newHostelId, hostelName = name)

        UserPreferencesRepository(context).updateAssignedHostelId(newHostelId)
        updateHostelStats()

        return Result.success(newHostel)
    }

    override suspend fun updateHostel(
        context: Context,
        name: String,
        location: String,
        address: String,
        city: String,
        state: String,
        pinCode: String,
        description: String
    ): Result<Unit> {
        val current = _hostel.value ?: return Result.failure(Exception("No hostel exists to edit."))
        if (name.isBlank()) return Result.failure(Exception("Hostel name cannot be empty."))

        val updated = current.copy(
            name = name,
            location = location,
            address = address,
            city = city,
            state = state,
            pinCode = pinCode,
            description = description,
            updatedAt = getCurrentDateFormatted()
        )
        _hostel.value = updated
        _currentWarden.value = _currentWarden.value?.copy(hostelName = name)
        return Result.success(Unit)
    }

    // --- CONTACTS ---
    private val _contacts = MutableStateFlow(
        listOf(
            HostelContact("C1", "HOSTEL_CHARAK", "Warden Office", "Chief Warden", "+91 98765 43210", false),
            HostelContact("C2", "HOSTEL_CHARAK", "Hostel Security Desk", "Security Supervisor", "+91 98765 00000", true),
            HostelContact("C3", "HOSTEL_CHARAK", "Mess Supervisor", "Catering Admin", "+91 98765 11111", false),
            HostelContact("C4", "HOSTEL_CHARAK", "Medical Emergency Desk", "Campus Infirmary", "+91 98765 22222", true)
        )
    )
    override val contacts: StateFlow<List<HostelContact>> = _contacts.asStateFlow()

    override suspend fun addContact(name: String, role: String, phone: String, isEmergency: Boolean): Result<Unit> {
        val hostelId = _hostel.value?.hostelId ?: "HOSTEL_DEFAULT"
        val newContact = HostelContact("C_${System.currentTimeMillis()}", hostelId, name, role, phone, isEmergency)
        _contacts.value = _contacts.value + newContact
        return Result.success(Unit)
    }

    override suspend fun updateContact(contact: HostelContact): Result<Unit> {
        _contacts.value = _contacts.value.map { if (it.contactId == contact.contactId) contact else it }
        return Result.success(Unit)
    }

    override suspend fun deleteContact(contactId: String): Result<Unit> {
        _contacts.value = _contacts.value.filter { it.contactId != contactId }
        return Result.success(Unit)
    }

    // --- STUDENTS ---
    private val initialStudents = listOf(
        Student("S101", "Aarav Sharma", "24A1001", "aarav.sharma@veda.edu.in", "+91 98111 22201", "B.Tech", "Computer Science", "2nd Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_101", "101", "Floor 1", ActivationStatus.ACTIVE, "VD123456", "01 Sep 2026"),
        Student("S102", "Priya Kumari", "24A1002", "priya.kumari@veda.edu.in", "+91 98111 22202", "B.Tech", "Information Tech", "2nd Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_102", "102", "Floor 1", ActivationStatus.ACTIVE, "VD123457", "01 Sep 2026"),
        Student("S103", "Rohan Verma", "24A1003", "rohan.verma@veda.edu.in", "+91 98111 22203", "B.Tech", "Mechanical", "3rd Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_103", "103", "Floor 1", ActivationStatus.ACTIVE, "VD123458", "02 Sep 2026"),
        Student("S104", "Neha Singh", "24A1004", "neha.singh@veda.edu.in", "+91 98111 22204", "B.Tech", "Civil Engineering", "1st Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_103", "103", "Floor 1", ActivationStatus.PENDING_ACTIVATION, "VD889900", "01 Oct 2026"),
        Student("S105", "Aditya Patel", "24A1005", "aditya.patel@veda.edu.in", "+91 98111 22205", "B.Tech", "Electronics", "2nd Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_104", "104", "Floor 1", ActivationStatus.ACTIVE, "VD123460", "05 Sep 2026"),
        Student("S106", "Sneha Gupta", "24A1006", "sneha.gupta@veda.edu.in", "+91 98111 22206", "B.Tech", "Computer Science", "3rd Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_201", "201", "Floor 2", ActivationStatus.ACTIVE, "VD123461", "06 Sep 2026"),
        Student("S107", "Vikram Malhotra", "24A1007", "vikram.m@veda.edu.in", "+91 98111 22207", "B.Arch", "Architecture", "4th Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_202", "202", "Floor 2", ActivationStatus.INACTIVE, "VD123462", "10 Aug 2026"),
        Student("S108", "Ananya Das", "24A1008", "ananya.das@veda.edu.in", "+91 98111 22208", "B.Tech", "Electrical", "2nd Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_203", "203", "Floor 2", ActivationStatus.ACTIVE, "VD123463", "12 Sep 2026"),
        Student("S109", "Deepak Joshi", "24A1009", "deepak.j@veda.edu.in", "+91 98111 22209", "M.Tech", "Data Science", "1st Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_301", "301", "Floor 3", ActivationStatus.ACTIVE, "VD123464", "15 Sep 2026"),
        Student("S110", "Rahul Kumar", "24A1010", "rahul.k@veda.edu.in", "+91 98111 22210", "B.Tech", "Computer Science", "1st Year", "HOSTEL_CHARAK", "Charak Chatras", "ROOM_302", "302", "Floor 3", ActivationStatus.PENDING_ACTIVATION, "VD991122", "02 Oct 2026")
    )

    private val _students = MutableStateFlow(initialStudents)
    override val students: StateFlow<List<Student>> = _students.asStateFlow()

    override fun getStudentById(studentId: String): Student? = _students.value.find { it.studentId == studentId }

    override suspend fun addStudent(
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
    ): Result<Student> {
        val targetRoom = getRoomById(roomId)
        if (targetRoom != null && targetRoom.isFull) {
            return Result.failure(Exception("Room $roomNumber is full! Choose another room."))
        }

        val currentHostel = _hostel.value
        val effectiveHostelId = if (hostelId.isNotBlank()) hostelId else currentHostel?.hostelId ?: "HOSTEL_DEFAULT"
        val effectiveHostelName = if (hostelName.isNotBlank()) hostelName else currentHostel?.name ?: "My Hostel"

        val codeNum = (100000..999999).random()
        val codeStr = "VD$codeNum"
        val newStudent = Student(
            studentId = "S_${System.currentTimeMillis()}",
            fullName = fullName,
            rollNumber = rollNumber,
            email = email,
            phone = phone,
            course = course,
            branch = branch,
            year = year,
            hostelId = effectiveHostelId,
            hostelName = effectiveHostelName,
            roomId = roomId,
            roomNumber = roomNumber,
            floor = floor,
            activationStatus = ActivationStatus.PENDING_ACTIVATION,
            activationCode = codeStr,
            createdAt = getCurrentDateFormatted()
        )

        _students.value = _students.value + newStudent
        _rooms.value = _rooms.value.map {
            if (it.roomId == roomId) it.copy(occupied = it.occupied + 1) else it
        }
        updateHostelStats()

        _activeSession.value?.let { session ->
            val record = AttendanceRecord(
                recordId = "REC_${newStudent.studentId}",
                sessionId = session.sessionId,
                studentId = newStudent.studentId,
                studentName = newStudent.fullName,
                rollNumber = newStudent.rollNumber,
                roomNumber = newStudent.roomNumber,
                status = AttendanceStatus.NOT_MARKED
            )
            _currentRecords.value = _currentRecords.value + record
        }

        return Result.success(newStudent)
    }

    override suspend fun updateStudent(student: Student): Result<Unit> {
        _students.value = _students.value.map { if (it.studentId == student.studentId) student else it }
        return Result.success(Unit)
    }

    override suspend fun generateActivationCode(studentId: String): String {
        val code = "VD${(100000..999999).random()}"
        _students.value = _students.value.map {
            if (it.studentId == studentId) it.copy(activationCode = code) else it
        }
        return code
    }

    override suspend fun simulateStudentActivation(studentId: String): Result<Unit> {
        _students.value = _students.value.map {
            if (it.studentId == studentId) it.copy(activationStatus = ActivationStatus.ACTIVE) else it
        }
        updateHostelStats()
        return Result.success(Unit)
    }

    // --- ATTENDANCE ---
    private val initialSession = AttendanceSession(
        sessionId = "SESS_MORNING_TODAY",
        hostelId = "HOSTEL_CHARAK",
        hostelName = "Charak Chatras",
        sessionType = SessionType.MORNING,
        date = getCurrentDateFormatted(),
        startTime = "07:00 AM",
        endTime = "09:00 AM",
        status = SessionStatus.ACTIVE,
        createdBy = "Dr. S. Mishra",
        createdAt = getCurrentDateFormatted()
    )

    private val initialRecords = initialStudents.mapIndexed { idx, st ->
        val status = when {
            idx in 0..6 -> AttendanceStatus.PRESENT
            idx == 7 -> AttendanceStatus.ABSENT
            else -> AttendanceStatus.NOT_MARKED
        }
        AttendanceRecord(
            recordId = "REC_${st.studentId}",
            sessionId = initialSession.sessionId,
            studentId = st.studentId,
            studentName = st.fullName,
            rollNumber = st.rollNumber,
            roomNumber = st.roomNumber,
            status = status,
            markedAt = if (status == AttendanceStatus.PRESENT) "07:25 AM" else null
        )
    }

    private val _sessions = MutableStateFlow(listOf(initialSession))
    override val sessions: StateFlow<List<AttendanceSession>> = _sessions.asStateFlow()

    private val _activeSession = MutableStateFlow<AttendanceSession?>(initialSession)
    override val activeSession: StateFlow<AttendanceSession?> = _activeSession.asStateFlow()

    private val _currentRecords = MutableStateFlow(initialRecords)
    override val currentRecords: StateFlow<List<AttendanceRecord>> = _currentRecords.asStateFlow()

    override suspend fun createSession(
        sessionType: SessionType,
        date: String,
        startTime: String,
        endTime: String
    ): Result<AttendanceSession> {
        if (_activeSession.value != null) {
            return Result.failure(Exception("There is already an active attendance session! Close it first."))
        }

        val hostelId = _hostel.value?.hostelId ?: "HOSTEL_DEFAULT"
        val hostelName = _hostel.value?.name ?: "My Hostel"

        val newSession = AttendanceSession(
            sessionId = "SESS_${sessionType.name}_${System.currentTimeMillis()}",
            hostelId = hostelId,
            hostelName = hostelName,
            sessionType = sessionType,
            date = date,
            startTime = startTime,
            endTime = endTime,
            status = SessionStatus.ACTIVE,
            createdBy = _currentWarden.value?.name ?: "Dr. S. Mishra",
            createdAt = getCurrentDateFormatted()
        )

        val newRecords = _students.value.map { st ->
            AttendanceRecord(
                recordId = "REC_${st.studentId}_${System.currentTimeMillis()}",
                sessionId = newSession.sessionId,
                studentId = st.studentId,
                studentName = st.fullName,
                rollNumber = st.rollNumber,
                roomNumber = st.roomNumber,
                status = AttendanceStatus.NOT_MARKED
            )
        }

        _sessions.value = listOf(newSession) + _sessions.value
        _activeSession.value = newSession
        _currentRecords.value = newRecords

        return Result.success(newSession)
    }

    override suspend fun updateRecordStatus(recordId: String, status: AttendanceStatus): Result<Unit> {
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        _currentRecords.value = _currentRecords.value.map {
            if (it.recordId == recordId) {
                it.copy(status = status, markedAt = if (status == AttendanceStatus.PRESENT) timeNow else null)
            } else it
        }
        return Result.success(Unit)
    }

    override suspend fun closeActiveSession(): Result<Unit> {
        val current = _activeSession.value ?: return Result.failure(Exception("No active session to close"))
        val closedSession = current.copy(status = SessionStatus.CLOSED)

        _sessions.value = _sessions.value.map {
            if (it.sessionId == current.sessionId) closedSession else it
        }
        _activeSession.value = null
        return Result.success(Unit)
    }

    // --- NOTICES ---
    private val initialNotices = listOf(
        Notice("N1", "Water Supply Maintenance", "Water supply will be unavailable from 10:00 AM to 01:00 PM due to urgent pipeline maintenance work.", TargetAudience.ALL_STUDENTS, "All Students", "Send Now", NoticeStatus.PUBLISHED, "Dr. S. Mishra", "02 Oct 2026"),
        Notice("N2", "Special Mess Menu - Gandhi Jayanti", "Special festive lunch will be served today including Paneer Special, Puri, Kheer, and Gulab Jamun.", TargetAudience.ALL_STUDENTS, "All Students", "Send Now", NoticeStatus.PUBLISHED, "Dr. S. Mishra", "02 Oct 2026"),
        Notice("N3", "Late Entry Gate Pass Guidelines", "All students returning to hostel after 09:30 PM must register entry at security desk and inform Warden Office.", TargetAudience.ALL_STUDENTS, "All Students", "Send Now", NoticeStatus.PUBLISHED, "Dr. S. Mishra", "28 Sep 2026"),
        Notice("N4", "Wi-Fi Router Maintenance", "Floor 2 Wi-Fi access points will undergo routine maintenance tonight between 11:00 PM and 01:00 AM.", TargetAudience.SPECIFIC_HOSTEL, "Charak Chatras", "Send Now", NoticeStatus.PUBLISHED, "Dr. S. Mishra", "25 Sep 2026")
    )

    private val _notices = MutableStateFlow(initialNotices)
    override val notices: StateFlow<List<Notice>> = _notices.asStateFlow()

    override suspend fun createNotice(
        title: String,
        message: String,
        targetAudience: TargetAudience,
        targetDetail: String,
        scheduleTime: String
    ): Result<Notice> {
        val newNotice = Notice(
            noticeId = "N_${System.currentTimeMillis()}",
            title = title,
            message = message,
            targetAudience = targetAudience,
            targetDetail = targetDetail,
            scheduleTime = scheduleTime,
            status = NoticeStatus.PUBLISHED,
            createdBy = _currentWarden.value?.name ?: "Dr. S. Mishra",
            createdAt = getCurrentDateFormatted()
        )
        _notices.value = listOf(newNotice) + _notices.value
        return Result.success(newNotice)
    }

    override suspend fun deleteNotice(noticeId: String): Result<Unit> {
        _notices.value = _notices.value.filter { it.noticeId != noticeId }
        return Result.success(Unit)
    }

    // --- MESS MENU ---
    private val initialMessMenu = listOf(
        MessMenu("M1", "HOSTEL_CHARAK", "Monday", "Poha, Jalebi, Tea/Coffee", "Dal Tadka, Mix Veg, Rice, Roti, Salad", "Samosa, Tea", "Shahi Paneer, Rice, Roti, Gulab Jamun"),
        MessMenu("M2", "HOSTEL_CHARAK", "Tuesday", "Aloo Paratha, Curd, Tea", "Rajma Chawal, Roti, Boondi Raita", "Veg Cutlet, Tea", "Aloo Gobi, Dal Fry, Rice, Roti"),
        MessMenu("M3", "HOSTEL_CHARAK", "Wednesday", "Idli Sambhar, Coconut Chutney", "Kadi Pakoda, Rice, Roti, Papad", "Pav Bhaji, Tea", "Chole Masala, Bhature/Roti, Rice, Kheer"),
        MessMenu("M4", "HOSTEL_CHARAK", "Thursday", "Puri Bhaji, Tea/Coffee", "Black Chana, Rice, Roti, Salad", "Bhel Puri, Tea", "Dal Makhani, Mix Veg, Rice, Roti"),
        MessMenu("M5", "HOSTEL_CHARAK", "Friday", "Upma, Banana, Tea/Coffee", "Chole Puri, Rice, Pickle", "French Fries, Tea", "Paneer Butter Masala, Jeera Rice, Naan"),
        MessMenu("M6", "HOSTEL_CHARAK", "Saturday", "Uttapam, Sambhar, Chutney", "Veg Biryani, Raita, Salan", "Bread Pakoda, Tea", "Egg Curry / Kadai Paneer, Rice, Roti"),
        MessMenu("M7", "HOSTEL_CHARAK", "Sunday", "Masala Dosa, Sambhar, Tea", "Special Veg Thali / Chicken Curry", "Biscuits, Tea", "Palak Paneer, Dal Fry, Rice, Roti, Ice Cream")
    )

    private val _messMenu = MutableStateFlow(initialMessMenu)
    override val messMenu: StateFlow<List<MessMenu>> = _messMenu.asStateFlow()

    override suspend fun updateMenuForDay(
        dayOfWeek: String,
        breakfast: String,
        lunch: String,
        snacks: String,
        dinner: String,
        specialNote: String
    ): Result<Unit> {
        _messMenu.value = _messMenu.value.map {
            if (it.dayOfWeek.equals(dayOfWeek, ignoreCase = true)) {
                it.copy(breakfast = breakfast, lunch = lunch, snacks = snacks, dinner = dinner, specialNote = specialNote)
            } else it
        }
        return Result.success(Unit)
    }

    private fun updateHostelStats() {
        val hostel = _hostel.value ?: return
        val totalCapacity = _rooms.value.sumOf { it.capacity }
        val totalOccupied = _rooms.value.sumOf { it.occupied }
        val occPct = if (totalCapacity > 0) ((totalOccupied.toDouble() / totalCapacity) * 100).toInt() else 0

        _hostel.value = hostel.copy(
            totalStudents = _students.value.size,
            occupancyPercentage = occPct
        )
    }

    private fun getCurrentDateFormatted(): String {
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
    }
}
