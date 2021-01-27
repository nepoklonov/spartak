package services


expect class AdminService {
    suspend fun checkAdmin(login: String, password:String): Boolean
}