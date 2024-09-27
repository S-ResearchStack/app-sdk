package researchstack.data.datasource.grpc.mapper

import researchstack.domain.model.UserProfileModel
import researchstack.backend.grpc.SubjectProfile as GrpcSubjectProfile

fun GrpcSubjectProfile.toDomain(): UserProfileModel = UserProfileModel(
    firstName = firstName,
    lastName = lastName,
    birthday = birthday.toLocalDate(),
    email = email,
    phoneNumber = phoneNumber,
    address = address
)

fun UserProfileModel.toData(): GrpcSubjectProfile = GrpcSubjectProfile.newBuilder()
    .setFirstName(firstName)
    .setLastName(lastName)
    .setBirthday(birthday.toDate())
    .setPhoneNumber(phoneNumber)
    .setEmail(email)
    .setAddress(address)
    .build()
