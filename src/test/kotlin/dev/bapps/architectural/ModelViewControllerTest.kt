package dev.bapps.architectural

import dev.bapps.architectural.ui.fakes.FakeButton
import dev.bapps.architectural.ui.fakes.FakeTextView
import dev.bapps.architectural.ui.fakes.FakeUiFramework
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModelViewControllerTest {

    private class FakeCounterRepository : MvcCounterRepository {
        private var counter = MvcModel.MIN_COUNTER
        override fun getCounter(): Int = counter
        override fun saveCounter(value: Int) {
            counter = value
        }
    }

    private lateinit var repository: FakeCounterRepository
    private lateinit var model: MvcModel
    private lateinit var controller: MvcCounterController
    private lateinit var primaryButton: FakeButton
    private lateinit var secondaryButton: FakeButton
    private lateinit var counterText: FakeTextView
    private lateinit var view: MvcCounterScreenView

    @BeforeEach
    fun setUp() {
        repository = FakeCounterRepository()
        model = MvcModel(repository)
        controller = MvcCounterController(model)
        primaryButton = FakeButton("primary.button")
        secondaryButton = FakeButton("secondary.button")
        counterText = FakeTextView("counter.text")

        view = MvcCounterScreenView(
            model = model,
            controller = controller,
            uiFramework = FakeUiFramework(
                "primary.button" to primaryButton,
                "secondary.button" to secondaryButton,
                "counter.text" to counterText
            )
        )
    }

    @Test
    fun `GIVEN closed screen WHEN screen opened THEN should display initial counter and disable decrement button`() {
        // given
        repository.saveCounter(MvcModel.MIN_COUNTER)

        // when
        view.onScreenOpened()

        // then
        assertEquals(MvcModel.MIN_COUNTER.toString(), counterText.text)
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
        assertEquals(MvcModel.MIN_COUNTER.toString(), counterText.text)
        assertEquals(true, primaryButton.isEnabled)
        assertEquals(false, secondaryButton.isEnabled)
        assertEquals(MvcModel.MIN_COUNTER, model.counter())
    }

    @Test
    fun `GIVEN opened screen WHEN primary button clicked up to max limit THEN should disable increment button`() {
        // given
        repository.saveCounter(MvcModel.MAX_COUNTER - 1)
        view.onScreenOpened()

        // when
        primaryButton.click()

        // then
        assertEquals(MvcModel.MAX_COUNTER.toString(), counterText.text)
        assertEquals(false, primaryButton.isEnabled)
        assertEquals(true, secondaryButton.isEnabled)
        assertEquals(MvcModel.MAX_COUNTER, model.counter())
    }

    @Test
    fun `GIVEN opened screen at zero WHEN secondary button clicked THEN should have no effect`() {
        // given
        view.onScreenOpened()

        // when
        secondaryButton.click()

        // then
        assertEquals(MvcModel.MIN_COUNTER.toString(), counterText.text)
        assertEquals(MvcModel.MIN_COUNTER, model.counter())
    }

    @Test
    fun `GIVEN closed screen WHEN model increments THEN should not update view`() {
        // given
        view.onScreenOpened()
        view.onScreenClosed()

        // when
        model.incrementCounter()

        // then
        assertEquals(MvcModel.MIN_COUNTER.toString(), counterText.text)
        assertEquals(1, model.counter())
    }

    @Test
    fun `GIVEN opened screen at upper bound WHEN primary button clicked THEN should have no effect`() {
        // given
        repository.saveCounter(MvcModel.MAX_COUNTER)
        view.onScreenOpened()

        // when
        primaryButton.click()

        // then
        assertEquals(MvcModel.MAX_COUNTER.toString(), counterText.text)
        assertEquals(MvcModel.MAX_COUNTER, model.counter())
    }
}
