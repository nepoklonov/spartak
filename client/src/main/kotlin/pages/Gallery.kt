package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.router.dom.navLink
import services.PhotoService
import styled.*
import view.SmallNavigation

data class GalleryNavigation(val header: String, val link: String)

val galleryNavigationList = listOf(
    GalleryNavigation("Тренировочный процесс", "trainingProcess"),
    GalleryNavigation("Кубок Ладоги 2019", "LadogaCup2019"),
    GalleryNavigation("Пекин", "Beijing"),
    GalleryNavigation("Турнир 2011 г.р.", "championship2011")
)


external interface GalleryProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedGallerySection: String
}

class GalleryState : RState {
    var error: Throwable? = null
    var images: List<String>? = null

}

class Gallery : RComponent<GalleryProps, GalleryState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(section: String, coroutineScope: CoroutineScope) {
        val photoService = PhotoService(coroutineContext)
        coroutineScope.launch {
            val listOfPhotos = try {
                photoService.getAllPhotosBySection(section)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState {
                this.images = listOfPhotos
            }
        }
    }

    override fun componentDidMount() {
        getState(props.selectedGallerySection, props.coroutineScope)
    }

    override fun componentDidUpdate(prevProps: GalleryProps, prevState: GalleryState, snapshot: Any) {
        if (this.props.selectedGallerySection != prevProps.selectedGallerySection) {
            getState(props.selectedGallerySection, props.coroutineScope)
        }
    }

    override fun RBuilder.render() {

        styledDiv {
            css {
                overflow = Overflow.hidden
            }

            headerText {
                +"Галерея"
            }

            styledDiv {
                css {
                    float = Float.left
                    backgroundColor = Color.white
                }
                galleryNavigationList.forEach {
                    navLink<GalleryProps>(to = it.link) {
                        child(SmallNavigation::class) {
                            attrs.selectedString = it.header
                        }
                    }
                }
            }

            styledDiv {
                if (state.images == null) {
                    +"загрузка..."
                } else {
                    state.images!!.forEach {
                        styledDiv {
                            css{
                                backgroundImage = Image( "url(/images/$it)")
                                backgroundSize = 230.px.toString()
                                width = 230.px
                                height = 230.px
                                margin = 10.px.toString()
                                float = Float.left
                            }
                        }
                    }
                }
            }
        }
    }
}