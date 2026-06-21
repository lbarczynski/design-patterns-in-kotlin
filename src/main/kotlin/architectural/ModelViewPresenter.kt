package dev.bapps.architectural

import dev.bapps.architectural.ui.Button
import dev.bapps.architectural.ui.TextView
import dev.bapps.architectural.ui.UiFramework

interface ScreenContract {
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

    interface Presenter {
        fun attachView(view: View)
        fun onPrimaryButtonClick()
        fun onSecondaryButtonClick()
        fun detachView()
    }

    interface View {
        fun setPrimaryButtonText(text: String)
        fun setSecondaryButtonText(text: String)
        fun setCounterText(counter: String)
        fun setPrimaryButtonEnabled(isEnabled: Boolean)
        fun setSecondaryButtonEnabled(isEnabled: Boolean)
    }
}

interface CounterRepository {
    fun getCounter(): Int
    fun saveCounter(value: Int)
}

class Model(private val repository: CounterRepository) : ScreenContract.Model {
    private val observers: MutableSet<ScreenContract.Model.Observer> = hashSetOf()

    override fun addObserver(observer: ScreenContract.Model.Observer) {
        observers += observer
    }

    override fun removeObserver(observer: ScreenContract.Model.Observer) {
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
        observers.onEach(ScreenContract.Model.Observer::onModelUpdated)
    }

    companion object {
        const val MIN_COUNTER = 0
        const val MAX_COUNTER = 100
    }
}

class Presenter(private val model: ScreenContract.Model) : ScreenContract.Presenter {
    private var view: ScreenContract.View? = null
    private val modelObserver = ScreenContract.Model.Observer {
        view?.update(model)
    }

    override fun attachView(view: ScreenContract.View) {
        this.view = view
        model.addObserver(modelObserver)
        view.setPrimaryButtonText("Increment Counter")
        view.setSecondaryButtonText("Decrement Counter")
        view.update(model)
    }

    override fun onPrimaryButtonClick() = model.incrementCounter()
    override fun onSecondaryButtonClick() = model.decrementCounter()

    override fun detachView() {
        model.removeObserver(modelObserver)
        this.view = null
    }

    private companion object {
        private fun ScreenContract.View.update(model: ScreenContract.Model) {
            setCounterText(model.counter().toString())
            setPrimaryButtonEnabled(model.canIncrement())
            setSecondaryButtonEnabled(model.canDecrement())
        }
    }
}

class CounterScreenView(
    private val presenter: ScreenContract.Presenter,
    uiFramework: UiFramework
) : ScreenContract.View {
    private val primaryButton: Button = uiFramework.findButtonById("primary.button")
    private val secondaryButton: Button = uiFramework.findButtonById("secondary.button")
    private val counterTextView: TextView = uiFramework.findTextViewById("counter.text")

    private val primaryClickListener = Button.OnClickListener { presenter.onPrimaryButtonClick() }
    private val secondaryClickListener = Button.OnClickListener { presenter.onSecondaryButtonClick() }

    fun onScreenOpened() {
        primaryButton.addOnClickListener(primaryClickListener)
        secondaryButton.addOnClickListener(secondaryClickListener)
        presenter.attachView(this)
    }

    fun onScreenClosed() {
        presenter.detachView()
        primaryButton.removeOnClickListener(primaryClickListener)
        secondaryButton.removeOnClickListener(secondaryClickListener)
        primaryButton.cleanup()
        secondaryButton.cleanup()
    }

    override fun setPrimaryButtonText(text: String) {
        primaryButton.setText(text)
    }

    override fun setSecondaryButtonText(text: String) {
        secondaryButton.setText(text)
    }

    override fun setCounterText(counter: String) {
        counterTextView.setText(counter)
    }

    override fun setPrimaryButtonEnabled(isEnabled: Boolean) {
        if (isEnabled) primaryButton.enable() else primaryButton.disable()
    }

    override fun setSecondaryButtonEnabled(isEnabled: Boolean) {
        if (isEnabled) secondaryButton.enable() else secondaryButton.disable()
    }
}
