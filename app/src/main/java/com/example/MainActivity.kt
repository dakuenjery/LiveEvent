package com.example

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.R
import com.github.dakuenjery.liveevent.LiveEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val event = LiveEvent<Unit>()
    private val obsList = mutableListOf<Observer<Unit>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onSubscribe(view: View) {
        val obs = Observer<Unit> {
            text_view.text = "${text_view.text}\nevent ${obsList.size}"
        }
        event.observe(this, obs)
        obsList.add(obs)
    }

    fun onUnsubscribe(view: View) {
        if (obsList.isNotEmpty())
            event.removeObserver(obsList.removeAt(0))
    }

    fun onInvoke(view: View) {
        event.emit(Unit)
    }
}
