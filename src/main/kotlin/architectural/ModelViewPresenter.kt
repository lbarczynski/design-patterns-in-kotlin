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
        fun attachView(view: ScreenContract.View)
        fun onPrimaryButtonClick()
        fun onSecondaryButtonClick()
        fun detachView()
    }

    interface View {
        fun renderPrimaryButton(text: String, onClick: () -> Unit)
        fun renderSecondaryButton(text: String, onClick: () -> Unit)
        fun renderCounter(counter: Int)
        fun setPrimaryButtonEnabled(isEnabled: Boolean)
        fun setSecondaryButtonEnabled(isEnabled: Boolean)
    }
}

interface CounterRepository {
    fun getCounter(): Int
    fun saveCounter(value: Int)
}

class Model(
    private val repository: CounterRepository
) : ScreenContract.Model {
    private val observers: MutableSet<ScreenContract.Model.Observer> = hashSetOf()

    override fun addObserver(observer: ScreenContract.Model.Observer) {
        observers += observer
    }

    override fun removeObserver(observer: ScreenContract.Model.Observer) {
        observers -= observer
    }

    override fun counter(): Int {
        return repository.getCounter()
    }

    override fun canIncrement(): Boolean {
        return repository.getCounter() < MAX_COUNTER
    }

    override fun incrementCounter() {
        require(canIncrement())
        repository.saveCounter(repository.getCounter() + 1)
        notifyObservers()
    }

    override fun canDecrement(): Boolean {
        return repository.getCounter() > MIN_COUNTER
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
    private val modelObserver =  ScreenContract.Model.Observer {
        view?.update(model)
    }

    override fun attachView(view: ScreenContract.View) {
        this.view = view
        model.addObserver(modelObserver)
        view.renderPrimaryButton(text = "Increment Counter", onClick = ::onPrimaryButtonClick)
        view.renderSecondaryButton(text = "Decrement Counter", onClick = ::onSecondaryButtonClick)
        view.update(model)
    }

    override fun onPrimaryButtonClick() {
        model.incrementCounter()
    }

    override fun onSecondaryButtonClick() {
        model.decrementCounter()
    }

    override fun detachView() {
        model.removeObserver(modelObserver)
        this.view = null
    }

    private companion object {
        private fun ScreenContract.View.update(model: ScreenContract.Model) {
            renderCounter(model.counter())
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

    fun onScreenOpened() {
        presenter.attachView(this)
    }

    fun onScreenClosed() {
        presenter.detachView()
        primaryButton.cleanup()
        secondaryButton.cleanup()
    }

    override fun renderPrimaryButton(text: String, onClick: () -> Unit) {
        primaryButton.setText(text)
        primaryButton.addOnClickListener(onClick)
    }

    override fun renderSecondaryButton(text: String, onClick: () -> Unit) {
        secondaryButton.setText(text)
        secondaryButton.addOnClickListener(onClick)
    }

    override fun renderCounter(counter: Int) {
        counterTextView.setText(counter.toString())
    }

    override fun setPrimaryButtonEnabled(isEnabled: Boolean) {
        if (isEnabled) primaryButton.enable() else primaryButton.disable()
    }

    override fun setSecondaryButtonEnabled(isEnabled: Boolean) {
        if (isEnabled) secondaryButton.enable() else secondaryButton.disable()
    }
}
