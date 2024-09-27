package researchstack.data.datasource.http

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Url

interface FileUploadApi {
    @Multipart
    @PUT
    suspend fun uploadImage(
        @Url uploadUrl: String,
        @Part image: MultipartBody.Part,
    )

    @PUT
    suspend fun upload(
        @Url uploadUrl: String,
        @Body file: RequestBody,
    )
}
