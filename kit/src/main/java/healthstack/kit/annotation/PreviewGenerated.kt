package healthstack.kit.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FUNCTION

@MustBeDocumented
@Retention(RUNTIME)
@Target(FUNCTION, CONSTRUCTOR)
annotation class PreviewGenerated
