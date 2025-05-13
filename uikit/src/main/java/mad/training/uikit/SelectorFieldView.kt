package mad.training.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectorFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val selectorText: TextView
    private val errorText: TextView
    private var items: List<String> = emptyList()
    private var onItemSelected: ((String) -> Unit)? = null

    init {
        orientation = VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.selector_field_view, this, true)
        selectorText = view.findViewById(R.id.selector_text)
        errorText = view.findViewById(R.id.error_text)

        selectorText.setOnClickListener {
            if (items.isNotEmpty()) showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(context)
        val container = LinearLayout(context).apply {
            orientation = VERTICAL
            setPadding(32, 32, 32, 32)
        }

        items.forEach { item ->
            val itemView = TextView(context).apply {
                text = item
                textSize = 16f
                setPadding(0, 24, 0, 24)
                setOnClickListener {
                    setValue(item)
                    onItemSelected?.invoke(item)
                    dialog.dismiss()
                }
            }
            container.addView(itemView)
        }

        dialog.setContentView(container)
        dialog.show()
    }

    fun setItems(list: List<String>, onItemSelected: (String) -> Unit) {
        this.items = list
        this.onItemSelected = onItemSelected
    }

    fun setValue(value: String?) {
        selectorText.text = value ?: ""
    }

    fun setHint(hint: String) {
        selectorText.hint = hint
    }
}
