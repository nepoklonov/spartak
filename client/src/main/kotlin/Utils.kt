import kotlinx.browser.document

val isAdmin = document.cookie.contains("role=admin")