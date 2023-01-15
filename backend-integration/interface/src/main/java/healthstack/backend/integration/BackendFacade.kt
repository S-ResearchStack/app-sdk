package healthstack.backend.integration

import healthstack.backend.integration.healthdata.HealthDataSyncClient
import healthstack.backend.integration.registration.UserRegistrationClient
import healthstack.backend.integration.task.TaskClient

interface BackendFacade : HealthDataSyncClient, UserRegistrationClient, TaskClient
