package mad.training.uikit

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class PrimaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    enum class State {
        ACTIVE, DISABLED, OUTLINED
    }

    var currentState: State = State.ACTIVE
        private set

    init {
        isAllCaps = false
        textSize = 16f
        setPadding(20, 16, 20, 16)
        setState(State.ACTIVE)
        elevation = 0f
    }

    fun setState(state: State) {
        currentState = state
        when (state) {
            State.ACTIVE -> {
                isEnabled = true
                background = ContextCompat.getDrawable(context, R.drawable.bg_button_active)
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }

            State.DISABLED -> {
                isEnabled = false
                background = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled)
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }

            State.OUTLINED -> {
                isEnabled = true
                background = ContextCompat.getDrawable(context, R.drawable.bg_button_outlined)
                setTextColor(ContextCompat.getColor(context, R.color.accent))
            }
        }
    }
}
