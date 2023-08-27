package com.nikcapko.logger

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

private const val LOG_TAG = "LogDebug"

@Aspect
class LogDebugAspect {

    @Around("execution(@com.nikcapko.logger.LogDebug * *(..))")
    fun logDebug(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()
        val signature = joinPoint.signature.toShortString()
        val result = try {
            with(StringBuilder("start -> Executing $signature, ")) {
                appendParameters(joinPoint.args)
                Log.i(LOG_TAG, toString())
            }
            joinPoint.proceed()
        } catch (throwable: Throwable) {
            Log.e(LOG_TAG, "*** Exception during executing $signature,", throwable)
            throw throwable
        }
        val duration = System.currentTimeMillis() - start
        Log.i(
            LOG_TAG,
            "end -> Finished executing: $signature, returned: '$result', duration: $duration ms",
        )
        return result
    }

    private fun StringBuilder.appendParameters(args: Array<Any>) {
        append("parameters: [")
        args.forEachIndexed { i, p ->
            append(p.javaClass.simpleName).append("(").append(p.toString()).append(")")
            if (i < args.size - 1) append(", ")
        }
        append("]")
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogDebug
