//package mad.training.uikit
//
//import android.content.Context
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.EditText
//import android.widget.FrameLayout
//import android.widget.TextView
//
//class CustomInput @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//    private val editText: EditText
//    private val errorText: TextView
//
//    private var isError = false
//    private var hintText: String? = null
//
//    init {
//        val inflater = LayoutInflater.from(context)
//        inflater.inflate(R.layout.custom_input, this, true)
//
//        editText = findViewById(R.id.editText)
//        errorText = findViewById(R.id.errorText)
//
//        setupAttributes(attrs)
//        setupListeners()
//    }
//
//    private fun setupAttributes(attrs: AttributeSet?) {
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomInput)
//        try {
//            hintText = typedArray.getString(R.styleable.CustomInput_hint)
//            editText.hint = hintText
//        } finally {
//            typedArray.recycle()
//        }
//    }
//
//    private fun setupListeners() {
//        editText.setOnFocusChangeListener { _, hasFocus ->
//            updateInputState(hasFocus)
//        }
//
//        editText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (isError && !s.isNullOrEmpty()) {
//                    setErrorState(false)
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }
//
//    private fun updateInputState(hasFocus: Boolean) {
//        if (isError) return
//
//        val backgroundResId = when {
//            hasFocus && !editText.text.isNullOrEmpty() -> R.drawable.input_background_focused
//            hasFocus -> R.drawable.input_background_focused
//            !editText.text.isNullOrEmpty() -> R.drawable.input_background_filled
//            else -> R.drawable.input_background_default
//        }
//
//        editText.setBackgroundResource(backgroundResId)
//    }
//
//    fun setErrorState(isError: Boolean, errorMessage: String = "") {
//        this.isError = isError
//
//        if (isError) {
//            editText.setBackgroundResource(R.drawable.input_background_error)
//            errorText.text = errorMessage
//            errorText.visibility = View.VISIBLE
//        } else {
//            errorText.visibility = View.GONE
//            updateInputState(editText.hasFocus())
//        }
//    }
//
//    fun getText(): String = editText.text.toString()
//
//    fun setText(text: String) {
//        editText.setText(text)
//    }
//
//    fun setHint(hint: String) {
//        hintText = hint
//        editText.hint = hint
//    }
//}