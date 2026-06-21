package dev.bapps.architectural

import dev.bapps.architectural.ui.Button
import dev.bapps.architectural.ui.TextView
import dev.bapps.architectural.ui.UiFramework

interface MvcScreenContract {
    interface Model {
        fun interface Observer {
            fun onModelUpdated()
        }

        fun addObserver(observer: Observer)
        fun removeObserver(observer: Observer)
        fun counter(): Int
        fun canIncrement(): Boolean
        fun incrementCounter()
        fun canDecrement(): Boolean
        fun decrementCounter()
    }

    interface Controller {
        fun onPrimaryButtonClick()
        fun onSecondaryButtonClick()
    }

    interface View
}

interface MvcCounterRepository {
    fun getCounter(): Int
    fun saveCounter(value: Int)
}

class MvcModel(private val repository: MvcCounterRepository) : MvcScreenContract.Model {
    private val observers: MutableSet<MvcScreenContract.Model.Observer> = hashSetOf()

    override fun addObserver(observer: MvcScreenContract.Model.Observer) {
        observers += observer
    }

    override fun removeObserver(observer: MvcScreenContract.Model.Observer) {
        observers -= observer
    }

    override fun counter(): Int = repository.getCounter()
    override fun canIncrement(): Boolean = repository.getCounter() < MAX_COUNTER
    override fun canDecrement(): Boolean = repository.getCounter() > MIN_COUNTER

    override fun incrementCounter() {
        require(canIncrement())
        repository.saveCounter(repository.getCounter() + 1)
        notifyObservers()
    }

    override fun decrementCounter() {
        require(canDecrement())
        repository.saveCounter(repository.getCounter() - 1)
        notifyObservers()
    }

    private fun notifyObservers() {
        observers.onEach(MvcScreenContract.Model.Observer::onModelUpdated)
    }

    companion object {
        const val MIN_COUNTER = 0
        const val MAX_COUNTER = 100
    }
}

class MvcCounterController(private val model: MvcScreenContract.Model) : MvcScreenContract.Controller {
    override fun onPrimaryButtonClick() = model.incrementCounter()
    override fun onSecondaryButtonClick() = model.decrementCounter()
}

class MvcCounterScreenView(
    private val model: MvcScreenContract.Model,
    private val controller: MvcScreenContract.Controller,
    uiFramework: UiFramework
) : MvcScreenContract.View {

    private val primaryButton: Button = uiFramework.findButtonById("primary.button")
    private val secondaryButton: Button = uiFramework.findButtonById("secondary.button")
    private val counterTextView: TextView = uiFramework.findTextViewById("counter.text")

    private val modelObserver = MvcScreenContract.Model.Observer {
        updateUi()
    }

    private val primaryButtonClickListener = Button.OnClickListener {
        controller.onPrimaryButtonClick()
    }

    private val secondaryButtonClickListener = Button.OnClickListener {
        controller.onSecondaryButtonClick()
    }

    fun onScreenOpened() {
        model.addObserver(modelObserver)

        primaryButton.setText("Increment Counter")
        secondaryButton.setText("Decrement Counter")
        primaryButton.addOnClickListener(primaryButtonClickListener)
        secondaryButton.addOnClickListener(secondaryButtonClickListener)

        updateUi()
    }

    fun onScreenClosed() {
        model.removeObserver(modelObserver)
        primaryButton.removeOnClickListener(primaryButtonClickListener)
        secondaryButton.removeOnClickListener(secondaryButtonClickListener)
    }

    private fun updateUi() {
        counterTextView.setText(model.counter().toString())
        if (model.canIncrement()) primaryButton.enable() else primaryButton.disable()
        if (model.canDecrement()) secondaryButton.enable() else secondaryButton.disable()

    }
}
