package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.LocationPoint
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import com.example.domain.model.VehicleInfo

@Entity(tableName = "cached_orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val orderNumber: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val mechanicId: String,
    val mechanicUserId: String,
    val mechanicName: String,
    val serviceType: String,
    val servicePrice: Double,
    val totalAmount: Double,
    val distanceKm: Double,
    val etaMinutes: Int,
    val status: String,
    val customerLat: Double,
    val customerLng: Double,
    val customerAddress: String,
    val vehicleType: String,
    val vehicleBrand: String,
    val vehicleModel: String,
    val vehicleRegNo: String,
    val problemNotes: String,
    val createdAtFormatted: String,
    val acceptedAtFormatted: String?,
    val completedAtFormatted: String?,
    val ratingGiven: Double?,
    val reviewGiven: String?
) {
    fun toDomain(): Order = Order(
        id = id,
        orderNumber = orderNumber,
        customerId = customerId,
        customerName = customerName,
        customerPhone = customerPhone,
        mechanicId = mechanicId,
        mechanicUserId = mechanicUserId,
        mechanicName = mechanicName,
        serviceType = serviceType,
        servicePrice = servicePrice,
        totalAmount = totalAmount,
        distanceKm = distanceKm,
        etaMinutes = etaMinutes,
        status = OrderStatus.fromString(status),
        customerLocation = LocationPoint(customerLat, customerLng, customerAddress),
        vehicle = VehicleInfo(vehicleType, vehicleBrand, vehicleModel, vehicleRegNo),
        problemNotes = problemNotes,
        createdAtFormatted = createdAtFormatted,
        acceptedAtFormatted = acceptedAtFormatted,
        completedAtFormatted = completedAtFormatted,
        ratingGiven = ratingGiven,
        reviewGiven = reviewGiven
    )

    companion object {
        fun fromDomain(order: Order): OrderEntity = OrderEntity(
            id = order.id,
            orderNumber = order.orderNumber,
            customerId = order.customerId,
            customerName = order.customerName,
            customerPhone = order.customerPhone,
            mechanicId = order.mechanicId,
            mechanicUserId = order.mechanicUserId,
            mechanicName = order.mechanicName,
            serviceType = order.serviceType,
            servicePrice = order.servicePrice,
            totalAmount = order.totalAmount,
            distanceKm = order.distanceKm,
            etaMinutes = order.etaMinutes,
            status = order.status.rawValue,
            customerLat = order.customerLocation.lat,
            customerLng = order.customerLocation.lng,
            customerAddress = order.customerLocation.address,
            vehicleType = order.vehicle.type,
            vehicleBrand = order.vehicle.brand,
            vehicleModel = order.vehicle.model,
            vehicleRegNo = order.vehicle.registrationNumber,
            problemNotes = order.problemNotes,
            createdAtFormatted = order.createdAtFormatted,
            acceptedAtFormatted = order.acceptedAtFormatted,
            completedAtFormatted = order.completedAtFormatted,
            ratingGiven = order.ratingGiven,
            reviewGiven = order.reviewGiven
        )
    }
}
