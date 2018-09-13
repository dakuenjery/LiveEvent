package com.github.dakuenjery.liveevent

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

open class LiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)
    private val observers = HashSet<Observer<T>>()

    private val eventObserver = Observer<T> {
        if (pending.compareAndSet(true, false)) {
            for (observer in observers) {
                observer.onChanged(it)
            }
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        observers.add(observer)

        if (!hasObservers())
            super.observe(owner, eventObserver)
    }

    override fun removeObserver(observer: Observer<T>) {
        observers.remove(observer)

        if (observers.isEmpty())
            super.removeObserver(eventObserver)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        observers.clear()
        super.removeObservers(owner)
    }

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }

    fun emit(value: T) {
        postValue(value)
    }
}