import kotlinx.css.CSSBuilder
import kotlinx.css.GridTemplateAreas
import kotlinx.css.StyledElement
import kotlinx.css.gridTemplateAreas
import kotlin.reflect.KProperty


fun CSSBuilder.gridTemplateAreas(vararg s: String) {
    gridTemplateAreas = GridTemplateAreas(s.joinToString("") { "\"$it\" " })
}

@Suppress("UNCHECKED_CAST")
private class CSSProperty<T>(private val default: (() -> T)? = null) {
    operator fun getValue(thisRef: StyledElement, property: KProperty<*>): T {
        default?.let { default ->
            if (!thisRef.declarations.containsKey(property.name)) {
                thisRef.declarations[property.name] = default() as Any
            }
        }
        return thisRef.declarations[property.name] as T
    }

    operator fun setValue(thisRef: StyledElement, property: KProperty<*>, value: T) {
        thisRef.declarations[property.name] = value as Any
    }
}

var StyledElement.gridArea: String by CSSProperty()