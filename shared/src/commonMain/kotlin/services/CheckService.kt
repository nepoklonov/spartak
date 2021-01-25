package services

import model.Check

expect class CheckService {
    suspend fun getCheck(): Check
}