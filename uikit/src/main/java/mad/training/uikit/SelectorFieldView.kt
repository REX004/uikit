package mad.training.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectorFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val selectorText: TextView
    private val errorText: TextView // Пока не используется, но может пригодиться для валидации
    private var items: List<String> = emptyList()
    private var externalOnItemSelected: ((String) -> Unit)? = null // Переименовал, чтобы не путать
    private var internalOnSelectionChangedListener: (() -> Unit)? = null // Наш новый слушатель

    private var selectedValue: String? = null
        private set // Устанавливаем только внутри этого класса

    init {
        orientation = VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.selector_field_view, this, true)
        selectorText = view.findViewById(R.id.selector_text)
        errorText =
            view.findViewById(R.id.error_text) // Убедись, что этот ID есть в R.layout.selector_field_view

        selectorText.setOnClickListener {
            if (items.isNotEmpty()) showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(context)
        val container = LinearLayout(context).apply {
            orientation = VERTICAL
            // Добавим немного отступов для элементов в боттомшите
            setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8))
        }

        items.forEach { item ->
            val itemView = TextView(context).apply {
                text = item
                textSize = 16f
                setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12))
                isClickable = true
                isFocusable = true

                setOnClickListener {
                    if (selectedValue != item) { // Вызываем слушатели только если значение изменилось
                        this@SelectorFieldView.selectedValue =
                            item // Обновляем внутреннее состояние
                        selectorText.text = item // Обновляем текст на самом View
                        externalOnItemSelected?.invoke(item)
                        internalOnSelectionChangedListener?.invoke() // Уведомляем об изменении
                    }
                    dialog.dismiss()
                }
            }
            container.addView(itemView)
        }

        dialog.setContentView(container)
        dialog.show()
    }

    fun setItems(
        list: List<String>,
        onItemSelected: ((String) -> Unit)? = null
    ) {
        this.items = list
        this.externalOnItemSelected = onItemSelected
        if (list.isEmpty() || (list.size == 1 && list.firstOrNull() == selectorText.hint?.toString())) {
            clearSelection()
        } else if (selectedValue != null && !list.contains(selectedValue)) {
            clearSelection()
        }
    }

    fun setOnSelectionChangedListener(listener: () -> Unit) {
        this.internalOnSelectionChangedListener = listener
    }

    fun setValue(value: String?) {
        this.selectedValue = value
        selectorText.text = value ?: "" // Если null, отображаем пустую строку (или можно hint)
        if (value == null) {
            selectorText.text = selectorText.hint // Восстанавливаем hint при очистке
        }
    }

    fun setHint(hint: String) {
        selectorText.hint = hint
        if (selectedValue == null) { // Если ничего не выбрано, показываем hint
            selectorText.text = "" // Очищаем текст, чтобы hint был виден
        }
    }

    fun getSelectedValue(): String? { // Добавили метод для получения значения
        return selectedValue
    }

    fun clearSelection() {
        val changed = selectedValue != null
        selectedValue = null
        selectorText.text = "" // Очищаем текст, чтобы hint стал видимым
        if (changed) { // Уведомляем, только если значение действительно изменилось на null
            internalOnSelectionChangedListener?.invoke()
        }
    }

    // Вспомогательная функция
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}