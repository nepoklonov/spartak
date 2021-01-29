package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import react.*
import react.router.dom.navLink
import services.PhotoService
import styled.*
import view.ColorSpartak

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

    override fun componentDidMount() {
        val photoService = PhotoService(coroutineContext)

        props.coroutineScope.launch {
            val listOfPhotos = try {
                photoService.getAllPhotosBySection("trainingProcess")
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

    override fun RBuilder.render() {
        styledDiv {
            css {
                overflow = Overflow.hidden
            }

            styledH1 {
                +"Галерея"
            }

            styledDiv {
                css {
                    float = kotlinx.css.Float.left
                    backgroundColor = Color.white
                    textDecoration = TextDecoration.none
                }
                galleryNavigationList.forEach {
                    navLink<GalleryProps>(to = it.link) {
                        styledDiv {
                            css {
                                textAlign = TextAlign.center
                                color = ColorSpartak.Red.color
                                width = 200.px
                            }
                            styledH2 {
                                css {
                                    margin = 40.px.toString()
                                }
                                +it.header
                            }
                        }
                    }
                }
            }

            styledDiv {
                if (state.images == null) {
                    +"загрузка..."
                } else {
                    state.images!!.forEach {
                        styledImg(src = "/images/$it") {
                            css {
                                width = 100.px
                                margin = 20.px.toString()
                                float = Float.left
                            }
                        }
                    }
                }
            }
        }
    }
}