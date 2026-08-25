package com.example.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val OTP_VERIFICATION = "otp/{phoneNumber}/{fullName}/{sessionId}"
    const val ROLE_SELECTION = "role_selection"
    const val MECHANIC_INTRO = "mechanic_intro"
    const val REGISTRATION_STEP_1 = "reg_step_1"
    const val REGISTRATION_STEP_2 = "reg_step_2"
    const val REGISTRATION_STEP_3 = "reg_step_3"
    const val APPROVAL_PENDING = "approval_pending"
    const val REGISTRATION_REJECTED = "registration_rejected"
    const val ACCOUNT_SUSPENDED = "account_suspended"
    const val DASHBOARD = "dashboard"
    const val JOB_ACCEPTED = "job_accepted/{orderId}"
    const val ACTIVE_JOB_MAP = "active_job_map/{orderId}"
    const val JOB_DETAILS = "job_details/{orderId}"
    const val CUSTOMER_CHAT = "customer_chat/{orderId}/{customerId}/{customerName}"
    const val JOB_COMPLETED_SUCCESS = "job_completed_success/{orderId}"
    const val JOBS_HISTORY = "jobs_history"
    const val EARNINGS = "earnings"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val MANAGE_SERVICES = "manage_services"

    // Customer App Experience Routes
    const val CUSTOMER_HOME = "customer_home"
    const val CUSTOMER_ALL_SERVICES = "customer_all_services"
    const val CUSTOMER_CHOOSE_LOCATION = "customer_choose_location"
    const val CUSTOMER_MECHANICS_NEAR_YOU = "customer_mechanics_near_you"
    const val CUSTOMER_MECHANIC_DETAILS = "customer_mechanic_details/{mechanicName}"
    const val CUSTOMER_SERVICE_DETAILS = "customer_service_details/{serviceName}/{price}"
    const val CUSTOMER_REVIEW_BOOKING = "customer_review_booking/{serviceName}/{vehicleType}/{price}"
    const val CUSTOMER_SENDING_RADAR = "customer_sending_radar/{mechanicName}/{serviceName}"
    const val CUSTOMER_LIVE_TRACKING = "customer_live_tracking"
    const val CUSTOMER_MECHANIC_CHAT = "customer_mechanic_chat"
    const val CUSTOMER_SERVICE_RATING = "customer_service_rating"
    const val CUSTOMER_MY_ORDERS = "customer_my_orders"
    const val CUSTOMER_ORDER_DETAILS = "customer_order_details/{orderId}"
    const val CUSTOMER_NOTIFICATIONS = "customer_notifications"
    const val CUSTOMER_PROFILE = "customer_profile"
    const val CUSTOMER_EDIT_PROFILE = "customer_edit_profile"
    const val CUSTOMER_SAVED_ADDRESSES = "customer_saved_addresses"
    const val CUSTOMER_HELP_SUPPORT = "customer_help_support"

    // Admin App Routes (Server API backend)
    const val ADMIN_LOGIN = "admin_login"
    const val ADMIN_OTP = "admin_otp/{phoneNumber}"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_MECHANICS = "admin_mechanics"
    const val ADMIN_PENDING_APPROVALS = "admin_pending_approvals"
    const val ADMIN_LIVE_TRACKING = "admin_live_tracking"

    fun createOtpRoute(phone: String, name: String = "Mechanic", sessionId: String = "session"): String {
        val safeName = if (name.isBlank()) "Mechanic" else name
        val safeSession = if (sessionId.isBlank()) "session" else sessionId
        return "otp/$phone/$safeName/$safeSession"
    }

    fun createAdminOtpRoute(phone: String): String = "admin_otp/$phone"

    fun createJobAcceptedRoute(orderId: String): String = "job_accepted/$orderId"
    fun createActiveJobMapRoute(orderId: String): String = "active_job_map/$orderId"
    fun createJobDetailsRoute(orderId: String): String = "job_details/$orderId"

    fun createChatRoute(orderId: String, customerId: String, customerName: String): String {
        val safeName = if (customerName.isBlank()) "Customer" else customerName
        val safeId = if (customerId.isBlank()) "customer" else customerId
        return "customer_chat/$orderId/$safeId/$safeName"
    }

    fun createJobCompletedSuccessRoute(orderId: String): String = "job_completed_success/$orderId"

    fun createCustomerMechanicDetailsRoute(mechanicName: String): String = "customer_mechanic_details/$mechanicName"
    fun createCustomerServiceDetailsRoute(serviceName: String, price: Int): String = "customer_service_details/$serviceName/$price"
    fun createCustomerReviewBookingRoute(serviceName: String, vehicleType: String, price: Int): String = "customer_review_booking/$serviceName/$vehicleType/$price"
    fun createCustomerRadarRoute(mechanicName: String, serviceName: String): String = "customer_sending_radar/$mechanicName/$serviceName"
    fun createCustomerOrderDetailsRoute(orderId: String): String = "customer_order_details/$orderId"
}
