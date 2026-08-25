# M4chanic Mechanic - Backend Compatibility & API Specification

This document details the backend integration specifications, contracts, workarounds, and data models implemented in the **M4chanic Mechanic Android Application** connecting to the M4chanic Railway Node.js/Express and Socket.IO server.

---

## 1. Base URLs & Infrastructure

- **REST API Base URL:** `https://m4chanic-app-production.up.railway.app/`
- **Socket.IO Base URL:** `https://m4chanic-app-production.up.railway.app`
- **Socket Transport:** Websockets with long-polling fallback, auto-reconnection, and query parameters `auth` with JWT `token`.

---

## 2. Authentication & Authorization

### Flow:
1. **Send OTP (`POST /api/auth/send-otp`)**
   - Request:
     ```json
     {
       "phone": "9876543210",
       "name": "Ramesh Kumar",
       "role": "mechanic"
     }
     ```
   - Phone numbers must be normalized 10-digit Indian mobile numbers starting with `[6-9]`.
   - Response:
     ```json
     {
       "success": true,
       "message": "OTP sent successfully",
       "sessionId": "sess_abc123"
     }
     ```

2. **Verify OTP (`POST /api/auth/verify-otp`)**
   - Request:
     ```json
     {
       "phone": "9876543210",
       "otp": "123456",
       "sessionId": "sess_abc123",
       "name": "Ramesh Kumar"
     }
     ```
   - Response:
     ```json
     {
       "success": true,
       "token": "eyJhbGciOi...",
       "user": {
         "_id": "usr_789",
         "name": "Ramesh Kumar",
         "phone": "9876543210",
         "role": "mechanic",
         "status": "approved",
         "mechanicStatus": "approved"
       }
     }
     ```

---

## 3. Mechanic Registration & Profile

### Registration (`POST /api/mechanic/register`)
- Header: `Authorization: Bearer <token>`
- Request:
  ```json
  {
    "fullName": "Ramesh Kumar",
    "email": "ramesh@gmail.com",
    "experience": 5,
    "vehicle": {
      "model": "Maruti Suzuki Eeco",
      "color": "White",
      "registrationNumber": "BR11AB1234"
    },
    "location": {
      "address": "Purnia Bus Stand Road, Bihar",
      "lat": 25.7771,
      "lng": 87.4753
    },
    "services": [
      { "type": "Puncture", "specialization": ["Two Wheeler", "Car"], "price": 199.0 },
      { "type": "Battery", "specialization": ["Car"], "price": 299.0 }
    ]
  }
  ```

### Profile Retrieval (`GET /api/mechanic/profile` or `/api/mechanic/me`)
- Returns mechanic status (`pending`, `approved`, `rejected`, `suspended`), current online status, rating, completed jobs count, today's earnings, and configured services.

### Online/Offline Status (`PUT /api/mechanic/status`)
- Request:
  ```json
  {
    "isOnline": true
  }
  ```
- Broadcasts real-time mechanic availability to the server socket.

---

## 4. Order Management & Job Execution

### 1. Incoming Order Broadcast (Socket.IO event: `new_order` / `order_request`)
- Server emits to online mechanic room:
  ```json
  {
    "orderId": "ord_999",
    "customerName": "Amit Sharma",
    "customerPhone": "9876543211",
    "serviceType": "Puncture",
    "customerLocation": {
      "lat": 25.7800,
      "lng": 87.4700,
      "address": "NH-31 Near Gulabbagh, Purnia"
    },
    "vehicle": {
      "brand": "Maruti",
      "model": "Swift Dzire",
      "registrationNumber": "BR11X1111"
    },
    "distance": 2.3,
    "eta": 8,
    "price": 199.0
  }
  ```

### 2. Accept / Reject Order (`POST /api/orders/{orderId}/respond`)
- Request:
  ```json
  {
    "accept": true
  }
  ```
- Also emits socket event `mechanic_accepted` or `mechanic_rejected` with `{ "orderId": "..." }`.

### 3. Complete Job (`POST /api/orders/{orderId}/complete`)
- Request:
  ```json
  {
    "notes": "Replaced tubeless puncture strip and calibrated tyre air pressure"
  }
  ```
- Emits socket event `order_completed`.

---

## 5. Live GPS Location Tracking & Socket Protocol

### Foreground Location Service (`LocationTrackingService`)
- While an active job is accepted, `LocationTrackingService` runs as an Android foreground service with notification `M4chanic Live Job Tracking Active`.
- Transmits GPS location updates every 5 seconds to `SocketManager.updateLocation(lat, lng, heading)`:
  - Socket event: `update_location`
  - Payload:
    ```json
    {
      "orderId": "ord_999",
      "lat": 25.7780,
      "lng": 87.4720,
      "heading": 45.0,
      "speed": 22.5
    }
    ```

---

## 6. Real-Time Customer Chat

- **Send Message (`POST /api/chat/send` and Socket `send_message`)**
  - Payload:
    ```json
    {
      "orderId": "ord_999",
      "receiverId": "usr_cust_123",
      "message": "I have reached the petrol pump. Where are you parked?"
    }
    ```
- **Listen for Incoming Messages (Socket `new_message` / `receive_message`)**
  - Automatically updates the Jetpack Compose chat stream in real-time.

---

## 7. Direct Cash / UPI Settlement Architecture

- In accordance with roadside mechanics in India, the application follows a 100% direct-to-mechanic settlement model.
- Payment is collected in cash or customer UPI on spot upon completion of repair.
- No wallet deduction or gateway fees are applied.
