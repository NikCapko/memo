package com.nikcapko.memo.core.test

import com.nikcapko.memo.core.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val DEFAULT_TIMEOUT = 10.seconds

/**
 * Правило для тестов, позволяющее автоматически подготавливать корутины для тестов.
 *
 * В файле теста добавить
 * ```
 * @ExtendWith(MainCoroutineDispatcherExtension::class)
 * ```
 *
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineDispatcherExtension(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : BeforeEachCallback, AfterEachCallback {

    val coroutineDispatchers: DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = dispatcher
        override val default: CoroutineDispatcher
            get() = dispatcher
        override val io: CoroutineDispatcher
            get() = dispatcher
        override val unconfined: CoroutineDispatcher
            get() = dispatcher
    }

    val scope: TestScope = TestScope(dispatcher)

    fun runTest(
        timeout: Duration = DEFAULT_TIMEOUT,
        testBody: suspend TestScope.() -> Unit,
    ): TestResult = scope.runTest(timeout, testBody)

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
