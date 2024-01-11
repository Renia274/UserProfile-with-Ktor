# User Profile with Ktor

## Overview

This repository contains a user profile app with an integrated Ktor module, providing additional functionalities such as create, delete, fetch, and update posts 
from [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com/). The goal is to demonstrate a modular architecture where the Ktor implementation operates 
independently of the main application.

## Features
**User Profile Management:** Allows users to create, update, and view their profiles.
**Ktor Implementation:** Includes a separate Ktor module with CRUD operations.
**Firebase Integration:** Utilizes Firebase services for OTP (One-Time Password) for users created through the user profile app.


## Users

The following users have been added to the profile app for simulation/testing purposes:

1. Bob Johnson
2. Alice Smith
3. Eve Brown
   
## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- [Java Development Kit (JDK)](https://adoptopenjdk.net/)
- [Kotlin](https://kotlinlang.org/)
- KotlinX Serialization
- Hilt Injection
- Kapt
- Firebase dependencies:core,messaging,crashlytics,auth,analytics
- Google services

### Installation

1. Clone the repository:
    git clone https://github.com/Renia274/UserProfile-with-Ktor.git

2. Navigate to the project directory:
    cd UserProfile-with-Ktor

3. Create a Firebase project through the [Firebase Console](https://console.firebase.google.com/).
   
4. Download `google-services.json` from the Firebase Console and place it in your project's `app` directory.

