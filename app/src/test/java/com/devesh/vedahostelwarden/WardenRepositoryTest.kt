package com.devesh.vedahostelwarden

import com.devesh.vedahostelwarden.data.datasource.LocalMockDataSource
import com.devesh.vedahostelwarden.data.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class WardenRepositoryTest {

    @Test
    fun testAddStudentAndActivation() = runBlocking {
        val initialCount = LocalMockDataSource.students.value.size
        val result = LocalMockDataSource.addStudent(
            fullName = "Test Student",
            rollNumber = "24TEST101",
            email = "test@veda.edu.in",
            phone = "+91 99000 11122",
            course = "B.Tech",
            branch = "CSE",
            year = "1st Year",
            hostelId = "HOSTEL_CHARAK",
            hostelName = "Charak Chatras",
            roomId = "ROOM_103",
            roomNumber = "103",
            floor = "Floor 1"
        )

        assertTrue(result.isSuccess)
        val student = result.getOrNull()
        assertNotNull(student)
        assertEquals(ActivationStatus.PENDING_ACTIVATION, student?.activationStatus)
        assertTrue(student?.activationCode?.startsWith("VD") == true)
        assertEquals(initialCount + 1, LocalMockDataSource.students.value.size)

        // Simulate Activation
        val actResult = LocalMockDataSource.simulateStudentActivation(student!!.studentId)
        assertTrue(actResult.isSuccess)
        val updatedStudent = LocalMockDataSource.getStudentById(student.studentId)
        assertEquals(ActivationStatus.ACTIVE, updatedStudent?.activationStatus)
    }

    @Test
    fun testAttendanceSessionFlow() = runBlocking {
        if (LocalMockDataSource.activeSession.value != null) {
            LocalMockDataSource.closeActiveSession()
        }
        assertNull(LocalMockDataSource.activeSession.value)

        val createResult = LocalMockDataSource.createSession(
            sessionType = SessionType.EVENING,
            date = "02 Oct 2026",
            startTime = "07:00 PM",
            endTime = "09:00 PM"
        )
        assertTrue(createResult.isSuccess)
        val session = LocalMockDataSource.activeSession.value
        assertNotNull(session)
        assertEquals(SessionType.EVENING, session?.sessionType)

        val records = LocalMockDataSource.currentRecords.value
        assertTrue(records.isNotEmpty())
        val firstRec = records.first()
        LocalMockDataSource.updateRecordStatus(firstRec.recordId, AttendanceStatus.PRESENT)
        val updatedRec = LocalMockDataSource.currentRecords.value.find { it.recordId == firstRec.recordId }
        assertEquals(AttendanceStatus.PRESENT, updatedRec?.status)

        val closeResult = LocalMockDataSource.closeActiveSession()
        assertTrue(closeResult.isSuccess)
        assertNull(LocalMockDataSource.activeSession.value)
    }

    @Test
    fun testNoticeCreation() = runBlocking {
        val initialCount = LocalMockDataSource.notices.value.size
        val noticeResult = LocalMockDataSource.createNotice(
            title = "Test Notice",
            message = "Test Message Body",
            targetAudience = TargetAudience.ALL_STUDENTS,
            targetDetail = "All Students",
            scheduleTime = "Send Now"
        )
        assertTrue(noticeResult.isSuccess)
        assertEquals(initialCount + 1, LocalMockDataSource.notices.value.size)

        val newNotice = noticeResult.getOrNull()
        assertNotNull(newNotice)
        LocalMockDataSource.deleteNotice(newNotice!!.noticeId)
        assertEquals(initialCount, LocalMockDataSource.notices.value.size)
    }

    @Test
    fun testMessMenuUpdate() = runBlocking {
        val result = LocalMockDataSource.updateMenuForDay(
            dayOfWeek = "Monday",
            breakfast = "Special Poha",
            lunch = "Special Thali",
            snacks = "Tea & Biscuits",
            dinner = "Special Paneer",
            specialNote = "Festive Special"
        )
        assertTrue(result.isSuccess)
        val mondayMenu = LocalMockDataSource.messMenu.value.find { it.dayOfWeek == "Monday" }
        assertEquals("Special Poha", mondayMenu?.breakfast)
    }
}
