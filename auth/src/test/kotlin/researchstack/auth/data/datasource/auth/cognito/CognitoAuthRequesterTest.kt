package researchstack.auth.data.datasource.auth.cognito

import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import aws.sdk.kotlin.services.cognitoidentityprovider.model.AuthenticationResultType
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InitiateAuthResponse
import aws.sdk.kotlin.services.cognitoidentityprovider.model.NotAuthorizedException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.auth.NEGATIVE_TEST
import researchstack.auth.POSITIVE_TEST

internal class CognitoAuthRequesterTest {
    private val cognitoClient = mockk<CognitoIdentityProviderClient>()
    private val requester = CognitoAuthRequester(cognitoClient, "test-id")

    private val username = "test@t.com"
    private val password = "!testpassword@"

    @Tag(POSITIVE_TEST)
    @Test
    fun `signIn should return success when username and password is valid`() = runTest {
        val returnIdToken = "return.idtoken.sample"
        coEvery { cognitoClient.initiateAuth(any()) } returns InitiateAuthResponse {
            authenticationResult = AuthenticationResultType {
                idToken = returnIdToken
            }
        }

        val result = requester.signIn(username, password)
        assertTrue(result.isSuccess)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `signIn should return failure when username and password is not valid`() = runTest {
        coEvery { cognitoClient.initiateAuth(any()) } throws NotAuthorizedException { }

        val result = requester.signIn(username, "wrong password")
        assertTrue(result.isFailure)
        assertThrows<NotAuthorizedException> { result.getOrThrow() }
    }
}
