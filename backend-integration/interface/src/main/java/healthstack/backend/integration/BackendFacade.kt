package healthstack.backend.integration

import healthstack.backend.integration.healthdata.HealthDataSyncClient
import healthstack.backend.integration.registration.UserRegistrationClient
import healthstack.backend.integration.task.TaskClient

/**
 *  Facade that collects the interface required for integrating with the backend that stores health data.
 *  It provides a simple integrated interface.
 */
interface BackendFacade : HealthDataSyncClient, UserRegistrationClient, TaskClient
