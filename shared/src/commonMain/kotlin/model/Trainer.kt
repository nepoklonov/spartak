package model

import kotlinx.serialization.Serializable

@Serializable
data class Trainer(
        val trainerId: Int, val trainerName: String, val trainerDateOfBirth: String, val trainerInfo: String
) {

}