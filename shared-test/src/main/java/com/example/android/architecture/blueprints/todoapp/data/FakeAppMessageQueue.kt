package com.example.android.architecture.blueprints.todoapp.data

import com.example.android.architecture.blueprints.todoapp.util.AppMessageQueue

class FakeAppMessageQueue : AppMessageQueue {

    private val fakePrefs = mutableMapOf<String, Int>()

    override fun produce(message: Int) {
        fakePrefs[MESSAGE_KEY] = message
    }

    override fun consume(): Int {
        return fakePrefs.remove(MESSAGE_KEY) ?: 0
    }

    private companion object {
        private const val MESSAGE_KEY = "message"
    }

}