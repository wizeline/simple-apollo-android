package com.wizeline.simpleapollo.utils

import timber.log.Timber

internal class SimpleApolloDebuger : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? =
        "[Class: ${element.className} [Line: ${element.lineNumber} [Method: ${element.methodName}]] [Error: ${super.createStackElementTag(element)}]]"
}
