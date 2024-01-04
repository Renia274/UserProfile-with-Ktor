package com.example.practice.helpers


// Helper function to validate email
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    return email.matches(emailRegex.toRegex())
}

// Helper function to validate password
fun isValidPassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[A-Z]).{8,}\$"
    return password.matches(passwordRegex.toRegex())
}



