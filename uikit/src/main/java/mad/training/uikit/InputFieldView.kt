package mad.training.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged

class InputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val editText: EditText
    private val errorText: TextView

    init {
        orientation = VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.input_field_view, this, true)
        editText = view.findViewById(R.id.edit_text)
        errorText = view.findViewById(R.id.error_text)

        setupListeners()
    }

    private fun setupListeners() {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editText.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_input_focused)
            } else {
                editText.background = ContextCompat.getDrawable(context, R.drawable.bg_input_normal)
            }
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                editText.clearFocus()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun setError(message: String?) {
        if (message.isNullOrEmpty()) {
            errorText.visibility = View.GONE
            editText.background = ContextCompat.getDrawable(context, R.drawable.bg_input_normal)
        } else {
            errorText.visibility = View.VISIBLE
            errorText.text = message
            editText.background = ContextCompat.getDrawable(context, R.drawable.bg_input_error)
        }
    }

    fun getText(): String = editText.text.toString()

    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setLabel(label: String) {
    }

    fun setOnTextChanged(listener: (String) -> Unit) {
        editText.doAfterTextChanged {
            listener.invoke(it.toString())
        }
    }
}

