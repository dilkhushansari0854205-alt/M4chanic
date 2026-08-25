package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigation.NavRoutes
import com.example.ui.auth.AuthViewModel
import com.example.ui.auth.AuthViewModelFactory
import com.example.ui.auth.LoginScreen
import com.example.ui.auth.OtpScreen
import com.example.ui.auth.SplashScreen
import com.example.ui.chat.ChatViewModel
import com.example.ui.chat.ChatViewModelFactory
import com.example.ui.chat.CustomerChatScreen
import com.example.ui.components.MechanicBottomTab
import com.example.ui.customer.AllServicesScreen
import com.example.ui.customer.ChooseLocationScreen
import com.example.ui.customer.CustomerHelpSupportScreen
import com.example.ui.customer.CustomerHomeScreen
import com.example.ui.customer.CustomerLiveTrackingScreen
import com.example.ui.customer.CustomerMechanicChatScreen
import com.example.ui.customer.CustomerNotificationsScreen
import com.example.ui.customer.CustomerProfileScreen
import com.example.ui.customer.EditCustomerProfileScreen
import com.example.ui.customer.MechanicDetailsScreen
import com.example.ui.customer.MechanicsNearYouScreen
import com.example.ui.customer.MyOrdersScreen
import com.example.ui.customer.OrderDetailsScreen
import com.example.ui.customer.ReviewBookingScreen
import com.example.ui.customer.SavedAddressesScreen
import com.example.ui.customer.SendingRequestRadarScreen
import com.example.ui.customer.ServiceBookingDetailsScreen
import com.example.ui.customer.ServiceCompletedRatingScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.dashboard.DashboardViewModel
import com.example.ui.dashboard.DashboardViewModelFactory
import com.example.ui.earnings.EarningsScreen
import com.example.ui.job.ActiveJobMapScreen
import com.example.ui.job.ActiveJobViewModel
import com.example.ui.job.ActiveJobViewModelFactory
import com.example.ui.job.JobAcceptedScreen
import com.example.ui.job.JobCompletedSuccessScreen
import com.example.ui.job.JobDetailsScreen
import com.example.ui.jobs.JobsHistoryScreen
import com.example.ui.profile.EditProfileScreen
import com.example.ui.profile.ManageServicesScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.registration.AccountSuspendedScreen
import com.example.ui.registration.ApprovalPendingScreen
import com.example.ui.registration.LocationReviewRegScreen
import com.example.ui.registration.MechanicIntroScreen
import com.example.ui.registration.PersonalVehicleRegScreen
import com.example.ui.registration.RegistrationRejectedScreen
import com.example.ui.registration.RegistrationViewModel
import com.example.ui.registration.RegistrationViewModelFactory
import com.example.ui.registration.ServicesPricingRegScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as M4chanicApp).container

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    M4chanicMechanicApp(appContainer = appContainer)
                }
            }
        }
    }
}

@Composable
fun M4chanicMechanicApp(appContainer: com.example.di.AppContainer) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    // ViewModels with factories
    val authViewModel: AuthViewModel = viewModel(
        factory = remember {
            AuthViewModelFactory(
                authRepository = appContainer.authRepository,
                sessionManager = appContainer.sessionManager
            )
        }
    )

    val registrationViewModel: RegistrationViewModel = viewModel(
        factory = remember {
            RegistrationViewModelFactory(
                mechanicRepository = appContainer.mechanicRepository,
                sessionManager = appContainer.sessionManager
            )
        }
    )

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = remember {
            DashboardViewModelFactory(
                mechanicRepository = appContainer.mechanicRepository,
                orderRepository = appContainer.orderRepository,
                socketManager = appContainer.socketManager,
                sessionManager = appContainer.sessionManager
            )
        }
    )

    val activeJobViewModel: ActiveJobViewModel = viewModel(
        factory = remember {
            ActiveJobViewModelFactory(
                orderRepository = appContainer.orderRepository,
                locationManager = appContainer.locationManager,
                socketManager = appContainer.socketManager
            )
        }
    )

    val chatViewModel: ChatViewModel = viewModel(
        factory = remember {
            ChatViewModelFactory(
                chatRepository = appContainer.chatRepository,
                socketManager = appContainer.socketManager,
                sessionManager = appContainer.sessionManager
            )
        }
    )

    val customerViewModel: com.example.ui.customer.CustomerViewModel = viewModel(
        factory = remember {
            com.example.ui.customer.CustomerViewModel.Factory(
                mechanicRepository = appContainer.mechanicRepository,
                orderRepository = appContainer.orderRepository,
                socketManager = appContainer.socketManager,
                sessionManager = appContainer.sessionManager
            )
        }
    )

    fun navigateCustomerTab(tab: String) {
        when (tab) {
            "home" -> navController.navigate(NavRoutes.CUSTOMER_HOME) { launchSingleTop = true }
            "orders" -> navController.navigate(NavRoutes.CUSTOMER_MY_ORDERS) { launchSingleTop = true }
            "chat" -> navController.navigate(NavRoutes.CUSTOMER_NOTIFICATIONS) { launchSingleTop = true }
            "profile" -> navController.navigate(NavRoutes.CUSTOMER_PROFILE) { launchSingleTop = true }
        }
    }

    fun navigateBottomTab(tab: MechanicBottomTab) {
        when (tab) {
            MechanicBottomTab.HOME -> {
                navController.navigate(NavRoutes.DASHBOARD) {
                    popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
                }
            }
            MechanicBottomTab.JOBS -> {
                navController.navigate(NavRoutes.JOBS_HISTORY) {
                    launchSingleTop = true
                }
            }
            MechanicBottomTab.EARNINGS -> {
                navController.navigate(NavRoutes.EARNINGS) {
                    launchSingleTop = true
                }
            }
            MechanicBottomTab.PROFILE -> {
                navController.navigate(NavRoutes.PROFILE) {
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        // Splash Screen
        composable(NavRoutes.SPLASH) {
            SplashScreen(
                sessionManager = appContainer.sessionManager,
                onNavigateNext = { destination ->
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToOtp = { phone, name, sessionId ->
                    navController.navigate(NavRoutes.createOtpRoute(phone, name, sessionId))
                },
                onRoleSelected = { role ->
                    appContainer.sessionManager.saveRole(role)
                }
            )
        }

        // Role Selection Screen (/role-selection)
        composable(NavRoutes.ROLE_SELECTION) {
            com.example.ui.auth.RoleSelectionScreen(
                onSelectCustomer = {
                    appContainer.sessionManager.saveRole("customer")
                    navController.navigate(NavRoutes.CUSTOMER_HOME) {
                        popUpTo(NavRoutes.ROLE_SELECTION) { inclusive = true }
                    }
                },
                onSelectMechanic = {
                    navController.navigate(NavRoutes.REGISTRATION_STEP_1)
                }
            )
        }

        // OTP Verification Screen
        composable(
            route = NavRoutes.OTP_VERIFICATION,
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("fullName") { type = NavType.StringType; defaultValue = "" },
                navArgument("sessionId") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            val name = backStackEntry.arguments?.getString("fullName") ?: ""
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""

            OtpScreen(
                phoneNumber = phone,
                fullName = name,
                sessionId = sessionId,
                viewModel = authViewModel,
                sessionManager = appContainer.sessionManager,
                onBack = { navController.popBackStack() },
                onNavigateNext = { destination ->
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Customer Home Screen (Image 1, Screen 1)
        composable(NavRoutes.CUSTOMER_HOME) {
            CustomerHomeScreen(
                customerName = "Amit",
                currentLocation = "Purnia, Bihar",
                onSelectService = { serviceName, price ->
                    navController.navigate(NavRoutes.createCustomerServiceDetailsRoute(serviceName, price))
                },
                onNavigateAllServices = {
                    navController.navigate(NavRoutes.CUSTOMER_ALL_SERVICES)
                },
                onNavigateChooseLocation = {
                    navController.navigate(NavRoutes.CUSTOMER_CHOOSE_LOCATION)
                },
                onNavigateMechanicsNearYou = {
                    navController.navigate(NavRoutes.CUSTOMER_MECHANICS_NEAR_YOU)
                },
                onNavigateEmergency = {
                    navController.navigate(NavRoutes.CUSTOMER_HELP_SUPPORT)
                },
                onTabSelected = { tab -> navigateCustomerTab(tab) }
            )
        }

        // All Services Screen (Image 1, Screen 2)
        composable(NavRoutes.CUSTOMER_ALL_SERVICES) {
            AllServicesScreen(
                onBack = { navController.popBackStack() },
                onSelectService = { serviceName, price ->
                    navController.navigate(NavRoutes.createCustomerServiceDetailsRoute(serviceName, price))
                },
                onTabSelected = { tab -> navigateCustomerTab(tab) }
            )
        }

        // Choose Location Screen (Image 1, Screen 3)
        composable(NavRoutes.CUSTOMER_CHOOSE_LOCATION) {
            ChooseLocationScreen(
                onBack = { navController.popBackStack() },
                onConfirmLocation = { _ ->
                    navController.popBackStack()
                }
            )
        }

        // Mechanics Near You (Image 1, Screen 4)
        composable(NavRoutes.CUSTOMER_MECHANICS_NEAR_YOU) {
            MechanicsNearYouScreen(
                onBack = { navController.popBackStack() },
                onSelectMechanic = { _, name ->
                    navController.navigate(NavRoutes.createCustomerMechanicDetailsRoute(name))
                },
                onTabSelected = { tab -> navigateCustomerTab(tab) }
            )
        }

        // Mechanic Details Screen (Image 2, Screen 1)
        composable(
            route = NavRoutes.CUSTOMER_MECHANIC_DETAILS,
            arguments = listOf(navArgument("mechanicName") { type = NavType.StringType; defaultValue = "Rakesh Kumar" })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("mechanicName") ?: "Rakesh Kumar"
            MechanicDetailsScreen(
                mechanicName = name,
                onBack = { navController.popBackStack() },
                onBookMechanic = {
                    navController.navigate(NavRoutes.createCustomerServiceDetailsRoute("Puncture Repair", 149))
                }
            )
        }

        // Service Details Screen (Image 2, Screen 2)
        composable(
            route = NavRoutes.CUSTOMER_SERVICE_DETAILS,
            arguments = listOf(
                navArgument("serviceName") { type = NavType.StringType; defaultValue = "Puncture Repair" },
                navArgument("price") { type = NavType.IntType; defaultValue = 149 }
            )
        ) { backStackEntry ->
            val sName = backStackEntry.arguments?.getString("serviceName") ?: "Puncture Repair"
            val price = backStackEntry.arguments?.getInt("price") ?: 149

            ServiceBookingDetailsScreen(
                initialService = sName,
                initialPrice = price,
                onBack = { navController.popBackStack() },
                onContinue = { name, vehicle, _, _, finalPrice ->
                    navController.navigate(NavRoutes.createCustomerReviewBookingRoute(name, vehicle, finalPrice))
                }
            )
        }

        // Review Booking Screen (Image 2, Screen 3)
        composable(
            route = NavRoutes.CUSTOMER_REVIEW_BOOKING,
            arguments = listOf(
                navArgument("serviceName") { type = NavType.StringType; defaultValue = "Puncture Repair" },
                navArgument("vehicleType") { type = NavType.StringType; defaultValue = "Car" },
                navArgument("price") { type = NavType.IntType; defaultValue = 149 }
            )
        ) { backStackEntry ->
            val sName = backStackEntry.arguments?.getString("serviceName") ?: "Puncture Repair"
            val vType = backStackEntry.arguments?.getString("vehicleType") ?: "Car"
            val price = backStackEntry.arguments?.getInt("price") ?: 149

            ReviewBookingScreen(
                mechanicName = "Rakesh Kumar",
                serviceName = sName,
                vehicleType = vType,
                serviceCharge = price,
                onBack = { navController.popBackStack() },
                onConfirmAndRequest = {
                    navController.navigate(NavRoutes.createCustomerRadarRoute("Rakesh Kumar", sName))
                }
            )
        }

        // Sending Request Radar Screen (Image 2, Screen 4)
        composable(
            route = NavRoutes.CUSTOMER_SENDING_RADAR,
            arguments = listOf(
                navArgument("mechanicName") { type = NavType.StringType; defaultValue = "Rakesh Kumar" },
                navArgument("serviceName") { type = NavType.StringType; defaultValue = "Puncture Repair" }
            )
        ) { backStackEntry ->
            val mName = backStackEntry.arguments?.getString("mechanicName") ?: "Rakesh Kumar"
            val sName = backStackEntry.arguments?.getString("serviceName") ?: "Puncture Repair"

            SendingRequestRadarScreen(
                mechanicName = mName,
                serviceName = sName,
                onAccepted = {
                    navController.navigate(NavRoutes.CUSTOMER_LIVE_TRACKING) {
                        popUpTo(NavRoutes.CUSTOMER_HOME) { inclusive = false }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // Live Tracking Screen (Image 3, Screen 1 & 2)
        composable(NavRoutes.CUSTOMER_LIVE_TRACKING) {
            CustomerLiveTrackingScreen(
                mechanicName = "Rakesh Kumar",
                onBack = { navController.popBackStack() },
                onCallMechanic = { },
                onChatMechanic = {
                    navController.navigate(NavRoutes.CUSTOMER_MECHANIC_CHAT)
                },
                onViewOrderDetails = {
                    navController.navigate(NavRoutes.createCustomerOrderDetailsRoute("MCN-784512"))
                },
                onCompleteJob = {
                    navController.navigate(NavRoutes.CUSTOMER_SERVICE_RATING)
                }
            )
        }

        // Customer Chat Screen (Image 3, Screen 3)
        composable(NavRoutes.CUSTOMER_MECHANIC_CHAT) {
            CustomerMechanicChatScreen(
                mechanicName = "Rakesh Kumar",
                onBack = { navController.popBackStack() },
                onCall = { }
            )
        }

        // Service Completed Rating Screen (Image 3, Screen 4)
        composable(NavRoutes.CUSTOMER_SERVICE_RATING) {
            ServiceCompletedRatingScreen(
                mechanicName = "Rakesh Kumar",
                serviceName = "Puncture Repair",
                totalPaid = 199,
                onSubmitRating = { _, _ ->
                    navController.navigate(NavRoutes.CUSTOMER_MY_ORDERS) {
                        popUpTo(NavRoutes.CUSTOMER_HOME) { inclusive = false }
                    }
                },
                onSkip = {
                    navController.navigate(NavRoutes.CUSTOMER_HOME) {
                        popUpTo(NavRoutes.CUSTOMER_HOME) { inclusive = true }
                    }
                }
            )
        }

        // My Orders Screen (Image 4, Screen 1)
        composable(NavRoutes.CUSTOMER_MY_ORDERS) {
            MyOrdersScreen(
                onSelectOrder = { orderId ->
                    navController.navigate(NavRoutes.createCustomerOrderDetailsRoute(orderId))
                },
                onTabSelected = { tab -> navigateCustomerTab(tab) }
            )
        }

        // Order Details Screen (Image 4, Screen 2)
        composable(
            route = NavRoutes.CUSTOMER_ORDER_DETAILS,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType; defaultValue = "MCN-784512" })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: "MCN-784512"
            OrderDetailsScreen(
                orderId = orderId,
                onBack = { navController.popBackStack() },
                onBookAgain = { navController.navigate(NavRoutes.CUSTOMER_HOME) },
                onGetHelp = { navController.navigate(NavRoutes.CUSTOMER_HELP_SUPPORT) }
            )
        }

        // Customer Notifications Screen (Image 4, Screen 3)
        composable(NavRoutes.CUSTOMER_NOTIFICATIONS) {
            CustomerNotificationsScreen(
                onBack = { navController.popBackStack() },
                onTabSelected = { tab -> navigateCustomerTab(tab) }
            )
        }

        // Customer Profile Screen (Image 4, Screen 4)
        composable(NavRoutes.CUSTOMER_PROFILE) {
            val user = appContainer.sessionManager.currentUser.collectAsState().value
            CustomerProfileScreen(
                customerName = user?.fullName ?: "Customer",
                customerPhone = user?.phoneNumber ?: "+91 98765 43210",
                onEditProfile = { navController.navigate(NavRoutes.CUSTOMER_EDIT_PROFILE) },
                onSavedAddresses = { navController.navigate(NavRoutes.CUSTOMER_SAVED_ADDRESSES) },
                onHelpSupport = { navController.navigate(NavRoutes.CUSTOMER_HELP_SUPPORT) },
                onBecomeMechanic = { navController.navigate(NavRoutes.MECHANIC_INTRO) },
                onLogout = {
                    coroutineScope.launch { appContainer.authRepository.logout() }
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.CUSTOMER_HOME) { inclusive = true }
                    }
                },
                onTabSelected = { tab -> navigateCustomerTab(tab) }
            )
        }

        // Edit Profile Screen (Image 5, Screen 1)
        composable(NavRoutes.CUSTOMER_EDIT_PROFILE) {
            EditCustomerProfileScreen(
                onBack = { navController.popBackStack() },
                onSave = { _, _ -> navController.popBackStack() }
            )
        }

        // Saved Addresses Screen (Image 5, Screen 2)
        composable(NavRoutes.CUSTOMER_SAVED_ADDRESSES) {
            SavedAddressesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Help & Support Screen (Image 5, Screen 3)
        composable(NavRoutes.CUSTOMER_HELP_SUPPORT) {
            CustomerHelpSupportScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Mechanic Registration & Dashboard Routes
        composable(NavRoutes.MECHANIC_INTRO) {
            MechanicIntroScreen(
                onStartRegistration = { navController.navigate(NavRoutes.REGISTRATION_STEP_1) }
            )
        }

        composable(NavRoutes.REGISTRATION_STEP_1) {
            PersonalVehicleRegScreen(
                viewModel = registrationViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(NavRoutes.REGISTRATION_STEP_2) }
            )
        }

        composable(NavRoutes.REGISTRATION_STEP_2) {
            ServicesPricingRegScreen(
                viewModel = registrationViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(NavRoutes.REGISTRATION_STEP_3) }
            )
        }

        composable(NavRoutes.REGISTRATION_STEP_3) {
            LocationReviewRegScreen(
                viewModel = registrationViewModel,
                locationManager = appContainer.locationManager,
                onBack = { navController.popBackStack() },
                onNavigatePending = {
                    navController.navigate(NavRoutes.APPROVAL_PENDING) {
                        popUpTo(NavRoutes.MECHANIC_INTRO) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.APPROVAL_PENDING) {
            ApprovalPendingScreen(
                mechanicRepository = appContainer.mechanicRepository,
                socketManager = appContainer.socketManager,
                onApproved = {
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.APPROVAL_PENDING) { inclusive = true }
                    }
                },
                onRejected = {
                    navController.navigate(NavRoutes.REGISTRATION_REJECTED) {
                        popUpTo(NavRoutes.APPROVAL_PENDING) { inclusive = true }
                    }
                },
                onSuspended = {
                    navController.navigate(NavRoutes.ACCOUNT_SUSPENDED) {
                        popUpTo(NavRoutes.APPROVAL_PENDING) { inclusive = true }
                    }
                },
                onLogout = {
                    coroutineScope.launch { appContainer.authRepository.logout() }
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.APPROVAL_PENDING) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.REGISTRATION_REJECTED) {
            RegistrationRejectedScreen(
                onReapply = {
                    navController.navigate(NavRoutes.REGISTRATION_STEP_1) {
                        popUpTo(NavRoutes.REGISTRATION_REJECTED) { inclusive = true }
                    }
                },
                onLogout = {
                    coroutineScope.launch { appContainer.authRepository.logout() }
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.REGISTRATION_REJECTED) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.ACCOUNT_SUSPENDED) {
            AccountSuspendedScreen(
                onLogout = {
                    coroutineScope.launch { appContainer.authRepository.logout() }
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.ACCOUNT_SUSPENDED) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.DASHBOARD) {
            DashboardScreen(
                viewModel = dashboardViewModel,
                onTabSelected = { navigateBottomTab(it) },
                onNavigateToJobAccepted = { orderId ->
                    navController.navigate(NavRoutes.createJobAcceptedRoute(orderId))
                },
                onNavigateToActiveMap = { orderId ->
                    navController.navigate(NavRoutes.createActiveJobMapRoute(orderId))
                }
            )
        }

        composable(
            route = NavRoutes.JOB_ACCEPTED,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            JobAcceptedScreen(
                orderId = orderId,
                viewModel = activeJobViewModel,
                onNavigateToMap = { ordId ->
                    navController.navigate(NavRoutes.createActiveJobMapRoute(ordId)) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = false }
                    }
                },
                onNavigateToDetails = { ordId ->
                    navController.navigate(NavRoutes.createJobDetailsRoute(ordId))
                }
            )
        }

        composable(
            route = NavRoutes.ACTIVE_JOB_MAP,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            ActiveJobMapScreen(
                orderId = orderId,
                viewModel = activeJobViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToDetails = { ordId ->
                    navController.navigate(NavRoutes.createJobDetailsRoute(ordId))
                },
                onNavigateToChat = { ordId, custId, custName ->
                    navController.navigate(NavRoutes.createChatRoute(ordId, custId, custName))
                },
                onNavigateToCompleted = { ordId ->
                    navController.navigate(NavRoutes.createJobCompletedSuccessRoute(ordId)) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.JOB_DETAILS,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            JobDetailsScreen(
                orderId = orderId,
                viewModel = activeJobViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToMap = { ordId ->
                    navController.navigate(NavRoutes.createActiveJobMapRoute(ordId))
                },
                onNavigateToCompleted = { ordId ->
                    navController.navigate(NavRoutes.createJobCompletedSuccessRoute(ordId)) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.JOB_COMPLETED_SUCCESS,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            JobCompletedSuccessScreen(
                orderId = orderId,
                viewModel = activeJobViewModel,
                onBackToDashboard = {
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.CUSTOMER_CHAT,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("customerId") { type = NavType.StringType },
                navArgument("customerName") { type = NavType.StringType; defaultValue = "Customer" }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            val customerName = backStackEntry.arguments?.getString("customerName") ?: "Customer"

            CustomerChatScreen(
                orderId = orderId,
                customerId = customerId,
                customerName = customerName,
                viewModel = chatViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.JOBS_HISTORY) {
            JobsHistoryScreen(
                orderRepository = appContainer.orderRepository,
                onTabSelected = { navigateBottomTab(it) },
                onNavigateToDetails = { orderId ->
                    navController.navigate(NavRoutes.createJobDetailsRoute(orderId))
                }
            )
        }

        composable(NavRoutes.EARNINGS) {
            EarningsScreen(
                mechanicRepository = appContainer.mechanicRepository,
                onTabSelected = { navigateBottomTab(it) }
            )
        }

        composable(NavRoutes.PROFILE) {
            ProfileScreen(
                mechanicRepository = appContainer.mechanicRepository,
                sessionManager = appContainer.sessionManager,
                onTabSelected = { navigateBottomTab(it) },
                onNavigateToEditProfile = { navController.navigate(NavRoutes.EDIT_PROFILE) },
                onNavigateToManageServices = { navController.navigate(NavRoutes.MANAGE_SERVICES) },
                onSwitchToCustomerMode = { navController.navigate(NavRoutes.CUSTOMER_HOME) },
                onLogout = {
                    coroutineScope.launch { appContainer.authRepository.logout() }
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.EDIT_PROFILE) {
            EditProfileScreen(
                mechanicRepository = appContainer.mechanicRepository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.MANAGE_SERVICES) {
            ManageServicesScreen(
                mechanicRepository = appContainer.mechanicRepository,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
