package services

import model.RecruitmentDTO

expect class RecruitmentService{
    suspend fun addRecruitment(recruitmentDTO: RecruitmentDTO): Boolean
    suspend fun getAllRecruitments(): List<RecruitmentDTO>
    suspend fun deleteRecruitment(id: Int): Boolean
}