package com.example.domain.model

data class User(
    val id: String,
    val phoneNumber: String,
    val fullName: String,
    val email: String = "",
    val role: String = "customer", // "customer" or "mechanic"
    val profileImage: String? = null,
    val rating: Double = 5.0
) {
    fun getMaskedPhone(): String {
        val clean = phoneNumber.filter { it.isDigit() }
        return if (clean.length >= 10) {
            val start = clean.take(2)
            val end = clean.takeLast(3)
            "+91 $start•••••$end"
        } else {
            "+91 $phoneNumber"
        }
    }
}

data class MechanicServiceItem(
    val type: String, // "Puncture Repair", "Battery Jump Start", etc.
    val specialization: List<String> = listOf("Two Wheeler", "Car"),
    val price: Double = 199.0
)

data class EarningsSummary(
    val today: Double = 0.0,
    val week: Double = 0.0,
    val month: Double = 0.0,
    val total: Double = 0.0
)

data class MechanicProfile(
    val id: String, // Mechanic record ID
    val userId: String, // Mechanic user ID
    val fullName: String = "Rohit Sharma",
    val phoneNumber: String = "9876543210",
    val email: String = "",
    val rating: Double = 4.8,
    val totalReviews: Int = 0,
    val completedJobs: Int = 0,
    val experience: Int = 5,
    val isOnline: Boolean = false,
    val isAvailable: Boolean = true,
    val status: String = "approved", // "pending", "approved", "rejected", "suspended"
    val vehicleModel: String = "Maruti Suzuki Eeco",
    val vehicleColor: String = "White",
    val vehiclePlate: String = "BR11AB1234",
    val workAddress: String = "Purnia, Bihar",
    val workLat: Double = 25.7771,
    val workLng: Double = 87.4753,
    val services: List<MechanicServiceItem> = emptyList(),
    val earnings: EarningsSummary = EarningsSummary(),
    val currentOrderId: String? = null
) {
    fun getMaskedPhone(): String {
        val clean = phoneNumber.filter { it.isDigit() }
        return if (clean.length >= 10) {
            val start = clean.take(2)
            val end = clean.takeLast(3)
            "+91 $start•••••$end"
        } else {
            "+91 $phoneNumber"
        }
    }

    fun isApproved(): Boolean = status.equals("approved", ignoreCase = true)
    fun isPending(): Boolean = status.equals("pending", ignoreCase = true)
    fun isRejected(): Boolean = status.equals("rejected", ignoreCase = true)
    fun isSuspended(): Boolean = status.equals("suspended", ignoreCase = true)
}

data class LocationPoint(
    val lat: Double,
    val lng: Double,
    val address: String = ""
)

data class VehicleInfo(
    val type: String = "Car", // Bike, Car, Heavy, Other
    val brand: String = "Maruti",
    val model: String = "Swift Dzire",
    val registrationNumber: String = "BR01AB1234"
)

enum class OrderStatus(val rawValue: String, val label: String) {
    PENDING("pending", "Incoming Order"),
    ACCEPTED("accepted", "Active Job"),
    ON_THE_WAY("on_the_way", "On The Way"),
    ARRIVING_SOON("arriving_soon", "Arriving Soon"),
    ARRIVED("arrived", "Arrived"),
    IN_PROGRESS("in_progress", "Repair In Progress"),
    REJECTED("rejected", "Rejected"),
    COMPLETED("completed", "Job Completed");

    companion object {
        fun fromString(value: String?): OrderStatus {
            return when (value?.lowercase()) {
                "pending" -> PENDING
                "accepted" -> ACCEPTED
                "on_the_way", "on-the-way" -> ON_THE_WAY
                "arriving_soon", "arriving" -> ARRIVING_SOON
                "arrived" -> ARRIVED
                "in_progress", "inprogress" -> IN_PROGRESS
                "rejected", "cancelled" -> REJECTED
                "completed" -> COMPLETED
                else -> PENDING
            }
        }
    }
}

data class TimelineStep(
    val title: String,
    val time: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class IncomingOrderEvent(
    val orderId: String,
    val customerId: String,
    val customerName: String,
    val serviceType: String,
    val customerLocation: LocationPoint,
    val distance: Double,
    val eta: Int,
    val timestamp: Long
)

data class Order(
    val id: String,
    val orderNumber: String,
    val customerId: String = "",
    val customerName: String = "Amit Kumar",
    val customerPhone: String = "+91 9876543210",
    val mechanicId: String = "",
    val mechanicUserId: String = "",
    val mechanicName: String = "Rohit Sharma",
    val serviceType: String = "Puncture Repair",
    val servicePrice: Double = 199.0,
    val totalAmount: Double = 199.0,
    val distanceKm: Double = 2.3,
    val etaMinutes: Int = 8,
    val status: OrderStatus = OrderStatus.PENDING,
    val customerLocation: LocationPoint = LocationPoint(25.7800, 87.4700, "Customer Address"),
    val mechanicLocation: LocationPoint? = null,
    val vehicle: VehicleInfo = VehicleInfo(),
    val problemNotes: String = "",
    val createdAtFormatted: String = "Today, 10:15 AM",
    val acceptedAtFormatted: String? = null,
    val completedAtFormatted: String? = null,
    val ratingGiven: Double? = null,
    val reviewGiven: String? = null,
    val timeline: List<TimelineStep> = emptyList()
) {
    fun getDisplayOrderNumber(): String = if (orderNumber.isNotBlank()) orderNumber else "#MCN-${id.takeLast(6).uppercase()}"
}

data class ChatMessage(
    val id: String,
    val orderId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: Long,
    val timeFormatted: String,
    val isFromMe: Boolean,
    val isRead: Boolean = true
)
