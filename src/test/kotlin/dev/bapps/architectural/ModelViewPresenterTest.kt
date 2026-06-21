package dev.bapps.architectural

import dev.bapps.architectural.ui.fakes.FakeButton
import dev.bapps.architectural.ui.fakes.FakeTextView
import dev.bapps.architectural.ui.fakes.FakeUiFramework
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ModelViewPresenterTest {

    private class FakeCounterRepository : CounterRepository {
        private var counter = Model.MIN_COUNTER
        override fun getCounter(): Int = counter
        override fun saveCounter(value: Int) {
            counter = value
        }
    }

    private lateinit var repository: FakeCounterRepository
    private lateinit var model: Model
    private lateinit var presenter: Presenter
    private lateinit var primaryButton: FakeButton
    private lateinit var secondaryButton: FakeButton
    private lateinit var counterText: FakeTextView
    private lateinit var view: CounterScreenView

    @BeforeEach
    fun setUp() {
        repository = FakeCounterRepository()
        model = Model(repository)
        presenter = Presenter(model)
        primaryButton = FakeButton("primary.button")
        secondaryButton = FakeButton("secondary.button")
        counterText = FakeTextView("counter.text")

        view = CounterScreenView(
            presenter, uiFramework = FakeUiFramework(
                "primary.button" to primaryButton,
                "secondary.button" to secondaryButton,
                "counter.text" to counterText
            )
        )
    }

    @Test
    fun `GIVEN closed screen WHEN screen opened THEN should display initial counter and disable decrement button`() {
        // given
        repository.saveCounter(Model.MIN_COUNTER)

        // when
        view.onScreenOpened()

        // then
        assertEquals(Model.MIN_COUNTER.toString(), counterText.text)
        assertEquals(true, primaryButton.isEnabled)
        assertEquals(false, secondaryButton.isEnabled)
    }

    @Test
    fun `GIVEN opened screen WHEN primary button clicked THEN should increment counter and enable decrement button`() {
        // given
        view.onScreenOpened()

        // when
        primaryButton.click()

        // then
        assertEquals("1", counterText.text)
        assertEquals(true, primaryButton.isEnabled)
        assertEquals(true, secondaryButton.isEnabled)
        assertEquals(1, model.counter())
    }

    @Test
    fun `GIVEN opened screen with counter at one WHEN secondary button clicked THEN should decrement counter and disable decrement button`() {
        // given
        repository.saveCounter(1)
        view.onScreenOpened()

        // when
        secondaryButton.click()

        // then
        assertEquals(Model.MIN_COUNTER.toString(), counterText.text)
        assertEquals(true, primaryButton.isEnabled)
        assertEquals(false, secondaryButton.isEnabled)
        assertEquals(Model.MIN_COUNTER, model.counter())
    }

    @Test
    fun `GIVEN opened screen WHEN primary button clicked up to max limit THEN should disable increment button`() {
        // given
        repository.saveCounter(Model.MAX_COUNTER - 1)
        view.onScreenOpened()

        // when
        primaryButton.click()

        // then
        assertEquals(Model.MAX_COUNTER.toString(), counterText.text)
        assertEquals(false, primaryButton.isEnabled)
        assertEquals(true, secondaryButton.isEnabled)
        assertEquals(Model.MAX_COUNTER, model.counter())
    }

    @Test
    fun `GIVEN opened screen at zero WHEN secondary button clicked THEN should have no effect`() {
        // given
        view.onScreenOpened()

        // when
        secondaryButton.click()

        // then
        assertEquals(Model.MIN_COUNTER.toString(), counterText.text)
        assertEquals(Model.MIN_COUNTER, model.counter())
    }

    @Test
    fun `GIVEN closed screen WHEN model increments THEN should not update view`() {
        // given
        view.onScreenOpened()
        view.onScreenClosed()

        // when
        model.incrementCounter()

        // then
        assertEquals(Model.MIN_COUNTER.toString(), counterText.text)
        assertEquals(1, model.counter())
    }

    @Test
    fun `GIVEN opened screen at upper bound WHEN primary button clicked THEN should have no effect`() {
        // given
        repository.saveCounter(Model.MAX_COUNTER)
        view.onScreenOpened()

        // when
        primaryButton.click()

        // then
        assertEquals(Model.MAX_COUNTER.toString(), counterText.text)
        assertEquals(Model.MAX_COUNTER, model.counter())
    }
}
