package mad.training.tmatule.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern // Для валидации email

/**
 * Описание назначения класса: ViewModel для экрана авторизации.
 * Управляет состоянием полей ввода, валидацией и процессом входа.
 * Автор: Твое Имя
 */
class LoginViewModel : ViewModel() {

    // LiveData для ошибок валидации
    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccessEvent = MutableLiveData<Event<Unit>>() // Используем Event для одноразовых событий
    val loginSuccessEvent: LiveData<Event<Unit>> = _loginSuccessEvent

    private val _navigateToCreateProfileEvent = MutableLiveData<Event<Unit>>()
    val navigateToCreateProfileEvent: LiveData<Event<Unit>> = _navigateToCreateProfileEvent

    private val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    /**
     * Описание назначения метода: Выполняет попытку входа пользователя.
     * Производит валидацию введенных данных.
     */
    fun loginUser(email: String, password: String) {
        if (!validateInput(email, password)) {
            return // Если валидация не прошла, выходим
        }

        _isLoading.value = true // Показываем состояние загрузки

        // TODO: Здесь будет вызов UseCase или напрямую Repository для сетевого запроса
        // Имитируем сетевой запрос
        // viewModelScope.launch {
        //     delay(2000) // Имитация задержки сети
        //     _isLoading.value = false
        //
        //     if (email == "test@example.com" && password == "password") {
        //         _loginSuccessEvent.value = Event(Unit) // Сигнал об успешном входе
        //     } else if (email == "new@example.com") {
        //          _navigateToCreateProfileEvent.value = Event(Unit) // Пользователь не найден, переводим на создание профиля
        //     }
        //     else {
        //         _passwordError.value = "Неверный email или пароль" // Общая ошибка для простоты
        //     }
        // }

        _isLoading.value = false // Убираем состояние загрузки (пока нет реального запроса)
        if (email.contains("new", ignoreCase = true)) {
            _navigateToCreateProfileEvent.value = Event(Unit)
        } else if (email.contains("valid", ignoreCase = true) && password.isNotEmpty()) {
            _loginSuccessEvent.value = Event(Unit)
        } else {
            _passwordError.value = "Неверный email или пароль (тест)"
        }
    }

    /**
     * Описание назначения метода: Валидирует поля email и password.
     * Устанавливает LiveData ошибок, если валидация не прошла.
     * @return true, если все поля валидны, иначе false.
     */
    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isBlank()) {
            _emailError.value = "Email не может быть пустым"
            isValid = false
        } else if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            _emailError.value = "Некорректный формат email"
            isValid = false
        } else {
            _emailError.value = null // Сбрасываем ошибку, если поле валидно
        }

        if (password.isBlank()) {
            _passwordError.value = "Пароль не может быть пустым"
            isValid = false
        }
        // TODO: Добавить здесь более сложную валидацию пароля согласно заданию
        // (не менее 8 символов, заглавные/строчные, цифры, спецсимволы)
        // Например:
        // else if (password.length < 8) {
        //    _passwordError.value = "Пароль должен быть не менее 8 символов"
        //    isValid = false
        // }
        else {
            _passwordError.value = null // Сбрасываем ошибку, если поле валидно
        }

        return isValid
    }

    /**
     * Описание назначения метода: Вызывается, когда текст в поле email изменяется.
     * Используется для сброса ошибки валидации email при вводе.
     */
    fun onEmailTextChanged() {
        if (_emailError.value != null) {
            _emailError.value = null
        }
    }

    /**
     * Описание назначения метода: Вызывается, когда текст в поле password изменяется.
     * Используется для сброса ошибки валидации password при вводе.
     */
    fun onPasswordTextChanged() {
        if (_passwordError.value != null) {
            _passwordError.value = null
        }
    }
}

/**
 * Обертка для событий LiveData, которые должны обрабатываться только один раз.
 * Например, для навигации или показа Toast.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    @Suppress("unused")
    fun peekContent(): T = content
}