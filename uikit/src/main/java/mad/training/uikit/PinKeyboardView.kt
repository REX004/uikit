package mad.training.uikit // Убедись, что это твой правильный пакет

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class PinKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * Интерфейс для обработки событий нажатия на кнопки клавиатуры.
     */
    interface OnPinButtonClickListener {
        fun onDigitClicked(digit: Char)

        fun onBackspaceClicked()
    }

    private var listener: OnPinButtonClickListener? = null

    // Кнопки клавиатуры
    private val key1: TextView
    private val key2: TextView
    private val key3: TextView
    private val key4: TextView
    private val key5: TextView
    private val key6: TextView
    private val key7: TextView
    private val key8: TextView
    private val key9: TextView
    private val key0: TextView
    private val keyBackspace: ImageButton

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pin_keyboard_simple_layout, this, true)

        key1 = findViewById(R.id.pin_key_1)
        key2 = findViewById(R.id.pin_key_2)
        key3 = findViewById(R.id.pin_key_3)
        key4 = findViewById(R.id.pin_key_4)
        key5 = findViewById(R.id.pin_key_5)
        key6 = findViewById(R.id.pin_key_6)
        key7 = findViewById(R.id.pin_key_7)
        key8 = findViewById(R.id.pin_key_8)
        key9 = findViewById(R.id.pin_key_9)
        key0 = findViewById(R.id.pin_key_0)
        keyBackspace = findViewById(R.id.pin_key_backspace)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val digitClickListener = View.OnClickListener { view ->
            val digitChar = (view as? TextView)?.text?.toString()?.firstOrNull()
            digitChar?.let {
                listener?.onDigitClicked(it)
            }
        }

        key1.setOnClickListener(digitClickListener)
        key2.setOnClickListener(digitClickListener)
        key3.setOnClickListener(digitClickListener)
        key4.setOnClickListener(digitClickListener)
        key5.setOnClickListener(digitClickListener)
        key6.setOnClickListener(digitClickListener)
        key7.setOnClickListener(digitClickListener)
        key8.setOnClickListener(digitClickListener)
        key9.setOnClickListener(digitClickListener)
        key0.setOnClickListener(digitClickListener)

        keyBackspace.setOnClickListener {
            listener?.onBackspaceClicked()
        }
    }

    /**
     * Устанавливает слушатель для событий клавиатуры.
     * @param listener Реализация [OnPinButtonClickListener].
     */
    fun setOnPinButtonClickListener(listener: OnPinButtonClickListener?) {
        this.listener = listener
    }
}