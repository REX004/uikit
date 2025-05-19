package mad.training.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged

class SearchFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val searchEditText: EditText
    private val clearButton: ImageView

    private var textChangedListener: ((String) -> Unit)? = null

    init {
        orientation = VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.search_field_view, this, true)
        searchEditText = view.findViewById(R.id.search_edit_text)
        clearButton = view.findViewById(R.id.clear_button)

        setupListeners()
    }

    private fun setupListeners() {
        searchEditText.doAfterTextChanged {
            val text = it.toString()
            clearButton.visibility = if (text.isNotEmpty()) VISIBLE else GONE
            textChangedListener?.invoke(text)
        }

        clearButton.setOnClickListener {
            searchEditText.text?.clear()
        }
    }

    fun setOnTextChangedListener(listener: (String) -> Unit) {
        textChangedListener = listener
    }

    fun getText(): String = searchEditText.text.toString()

    fun clearText() {
        searchEditText.text?.clear()
    }
}
