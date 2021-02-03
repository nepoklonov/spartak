package services

import kotlinx.serialization.builtins.serializer
import model.RecruitmentDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class RecruitmentService(coroutineContext: CoroutineContext) {
        private val transport = Transport(coroutineContext)
        actual suspend fun addRecruitment(recruitmentDTO: RecruitmentDTO): Boolean {
                return transport.post("addRecruitment", Boolean.serializer(), "recruitmentDTO"  to recruitmentDTO)
        }
        actual suspend fun getAllRecruitments(): List<RecruitmentDTO> {
                return transport.getList("getAllRecruitments", RecruitmentDTO.serializer())
        }
        actual suspend fun deleteRecruitment(id: Int): Boolean {
                return transport.post("deleteRecruitment", Boolean.serializer(), "id" to id)
        }
}