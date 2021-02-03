package services

import database.Recruitment
import database.database
import model.RecruitmentDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import rpc.RPCService

actual class RecruitmentService : RPCService {
    actual suspend fun addRecruitment(recruitmentDTO: RecruitmentDTO): Boolean {
        database {
            Recruitment.insert {
                it[dates] = recruitmentDTO.dates
                it[name] = recruitmentDTO.name
                it[birthday] = recruitmentDTO.birthday
                it[role] = recruitmentDTO.role
                it[stickGrip] = recruitmentDTO.stickGrip
                it[params] = recruitmentDTO.params
                it[previousSchool] = recruitmentDTO.previousSchool
                it[city] = recruitmentDTO.city
                it[phone] = recruitmentDTO.phone
                it[email] = recruitmentDTO.email
            }
        }
        return true
    }

    actual suspend fun getAllRecruitments(): List<RecruitmentDTO> {
        val listOfRecruitmentDTO = mutableListOf<RecruitmentDTO>()
        database {
            Recruitment.selectAll().forEach {
                listOfRecruitmentDTO += RecruitmentDTO(
                    it[Recruitment.id].value,
                    it[Recruitment.dates],
                    it[Recruitment.name],
                    it[Recruitment.birthday],
                    it[Recruitment.role],
                    it[Recruitment.stickGrip],
                    it[Recruitment.params],
                    it[Recruitment.previousSchool],
                    it[Recruitment.city],
                    it[Recruitment.phone],
                    it[Recruitment.email],
                )
            }
        }
        return listOfRecruitmentDTO
    }

    actual suspend fun deleteRecruitment(id: Int): Boolean {
        database {
            Recruitment.deleteWhere { Recruitment.id eq id }
        }
        return true
    }
}