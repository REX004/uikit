package mad.training.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout

class PinCodeFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val pinDots: List<ImageView>
    private var currentPinLength = 0
    private val maxPinLength: Int

    var onPinFilledListener: ((String) -> Unit)? = null
    private var currentPinValue = StringBuilder()


    init {
        LayoutInflater.from(context).inflate(R.layout.view_simple_pin_code_field, this, true)

        pinDots = listOfNotNull(
            findViewById(R.id.pinDot1),
            findViewById(R.id.pinDot2),
            findViewById(R.id.pinDot3),
            findViewById(R.id.pinDot4)
        )

        maxPinLength = pinDots.size

        updateDisplay()
    }

    private fun updateDisplay() {
        for (i in pinDots.indices) {
            if (i < currentPinLength) {
                pinDots[i].setImageResource(R.drawable.pin_dot_active)
            } else {
                pinDots[i].setImageResource(R.drawable.pin_dot_inactive)
            }
        }
    }

    fun setEnteredDigitsCount(length: Int) {
        if (length in 0..maxPinLength) {
            currentPinLength = length
            updateDisplay()
        }
    }

    fun appendDigit(digit: Char) {
        if (currentPinLength < maxPinLength) {
            currentPinLength++
            currentPinValue.append(digit)
            updateDisplay()
            if (currentPinLength == maxPinLength) {
                onPinFilledListener?.invoke(currentPinValue.toString())
            }
        }
    }

    fun deleteDigit() {
        if (currentPinLength > 0) {
            currentPinLength--
            if (currentPinValue.isNotEmpty()) {
                currentPinValue.deleteCharAt(currentPinValue.length - 1)
            }
            updateDisplay()
        }
    }

    fun clear() {
        currentPinLength = 0
        currentPinValue.clear()
        updateDisplay()
    }

    fun getEnteredDigitsCount(): Int = currentPinLength

    fun getMaxLength(): Int = maxPinLength

    fun isFilled(): Boolean = currentPinLength == maxPinLength

    fun getPin(): String = currentPinValue.toString()

}