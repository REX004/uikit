package mad.training.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val backButton: ImageView
    private val deleteButton: ImageView
    private val titleView: TextView

    init {
        orientation = VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.header_view, this, true)
        backButton = view.findViewById(R.id.button_back)
        deleteButton = view.findViewById(R.id.button_delete)
        titleView = view.findViewById(R.id.header_title)
    }

    fun setTitle(text: String) {
        titleView.text = text
    }

    fun setOnBackClickListener(listener: () -> Unit) {
        backButton.setOnClickListener { listener() }
    }

    fun setOnDeleteClickListener(listener: () -> Unit) {
        deleteButton.setOnClickListener { listener() }
    }

    fun showBackButton(show: Boolean) {
        backButton.visibility = if (show) VISIBLE else GONE
    }

    fun showDeleteButton(show: Boolean) {
        deleteButton.visibility = if (show) VISIBLE else GONE
    }
}
