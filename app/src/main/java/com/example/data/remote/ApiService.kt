package com.example.data.remote

import com.example.data.dto.AdminDashboardResponse
import com.example.data.dto.AdminLiveTrackingResponse
import com.example.data.dto.AdminLoginRequest
import com.example.data.dto.AdminLoginResponse
import com.example.data.dto.AdminMechanicsResponse
import com.example.data.dto.AdminVerifyRequest
import com.example.data.dto.AdminVerifyResponse
import com.example.data.dto.ChallengeRequest
import com.example.data.dto.ChallengeResponse
import com.example.data.dto.ChatHistoryResponse
import com.example.data.dto.CompleteJobRequest
import com.example.data.dto.CompleteJobResponse
import com.example.data.dto.CompleteOrderWithRatingRequest
import com.example.data.dto.CreateOrderRequest
import com.example.data.dto.CreateOrderResponse
import com.example.data.dto.GenericResponse
import com.example.data.dto.MechanicProfileResponse
import com.example.data.dto.MechanicRegisterRequest
import com.example.data.dto.MechanicRegisterResponse
import com.example.data.dto.NearbyMechanicsRequest
import com.example.data.dto.NearbyMechanicsResponse
import com.example.data.dto.OrderHistoryResponse
import com.example.data.dto.OrderStatusResponse
import com.example.data.dto.ProfileResponse
import com.example.data.dto.RespondOrderRequest
import com.example.data.dto.RespondOrderResponse
import com.example.data.dto.SendMessageRequest
import com.example.data.dto.SendMessageResponse
import com.example.data.dto.SendOtpRequest
import com.example.data.dto.SendOtpResponse
import com.example.data.dto.UpdateStatusRequest
import com.example.data.dto.UpdateStatusResponse
import com.example.data.dto.VerifyOtpRequest
import com.example.data.dto.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ============================================
    // AUTH
    // ============================================
    @POST("api/auth/challenge")
    suspend fun createChallenge(@Body request: ChallengeRequest = ChallengeRequest()): Response<ChallengeResponse>

    @POST("api/auth/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<SendOtpResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @GET("api/auth/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<GenericResponse>

    // ============================================
    // MECHANIC SPECIFIC
    // ============================================
    @POST("api/mechanic/register")
    suspend fun registerMechanic(@Body request: MechanicRegisterRequest): Response<MechanicRegisterResponse>

    @GET("api/mechanic/profile")
    suspend fun getMechanicProfile(): Response<MechanicProfileResponse>

    @PUT("api/mechanic/status")
    suspend fun updateMechanicStatus(@Body request: UpdateStatusRequest): Response<UpdateStatusResponse>

    @POST("api/mechanic/nearby")
    suspend fun getNearbyMechanics(@Body request: NearbyMechanicsRequest): Response<NearbyMechanicsResponse>

    // ============================================
    // ORDERS
    // ============================================
    @POST("api/order/create")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<CreateOrderResponse>

    @GET("api/order/{orderId}/status")
    suspend fun getOrderStatus(@Path("orderId") orderId: String): Response<OrderStatusResponse>

    @GET("api/order/history")
    suspend fun getOrderHistory(): Response<OrderHistoryResponse>

    @PUT("api/order/{orderId}/respond")
    suspend fun respondToOrder(
        @Path("orderId") orderId: String,
        @Body request: RespondOrderRequest
    ): Response<RespondOrderResponse>

    @PUT("api/order/{orderId}/complete")
    suspend fun completeJob(
        @Path("orderId") orderId: String,
        @Body request: CompleteJobRequest = CompleteJobRequest()
    ): Response<CompleteJobResponse>

    @PUT("api/order/{orderId}/complete")
    suspend fun completeOrderWithRating(
        @Path("orderId") orderId: String,
        @Body request: CompleteOrderWithRatingRequest
    ): Response<CompleteJobResponse>

    // ============================================
    // CHAT
    // ============================================
    @GET("api/chat/{orderId}")
    suspend fun getChatHistory(@Path("orderId") orderId: String): Response<ChatHistoryResponse>

    @POST("api/chat/send")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<SendMessageResponse>

    // ============================================
    // ADMIN (Matching Server Code)
    // ============================================
    @POST("api/admin/login")
    suspend fun adminLogin(@Body request: AdminLoginRequest): Response<AdminLoginResponse>

    @POST("api/admin/verify")
    suspend fun adminVerify(@Body request: AdminVerifyRequest): Response<AdminVerifyResponse>

    @GET("api/admin/dashboard")
    suspend fun getAdminDashboard(): Response<AdminDashboardResponse>

    @GET("api/admin/mechanics")
    suspend fun getAdminMechanics(): Response<AdminMechanicsResponse>

    @GET("api/admin/pending-mechanics")
    suspend fun getAdminPendingMechanics(): Response<AdminMechanicsResponse>

    @PUT("api/admin/approve-mechanic/{userId}")
    suspend fun approveMechanic(@Path("userId") userId: String): Response<GenericResponse>

    @PUT("api/admin/reject-mechanic/{userId}")
    suspend fun rejectMechanic(@Path("userId") userId: String): Response<GenericResponse>

    @GET("api/admin/live-tracking")
    suspend fun getAdminLiveTracking(): Response<AdminLiveTrackingResponse>
}
