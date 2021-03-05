package pages.gallery

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import content
import grid
import header
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import loading
import model.PhotoDTO
import navigation
import react.*
import services.PhotoService
import styled.css
import styled.styledDiv
import styled.styledImg


external interface GalleryProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedGallerySection: String
}

class GalleryState : RState {
    var images: List<PhotoDTO>? = null
    var selectedPhotoIndex: Int? = null
}

class Gallery : RComponent<GalleryProps, GalleryState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(section: String, coroutineScope: CoroutineScope) {
        val photoService = PhotoService(coroutineContext)
        coroutineScope.launch {
            val listOfPhotos = photoService.getAllPhotosBySection(section)
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

    private fun RBuilder.galleryView() {
        styledDiv {
            if (state.images == null) {
                loading()
            } else {
                styledDiv {
                    css {
                        display = Display.flex
                        justifyContent = JustifyContent.flexStart
                        flexWrap = FlexWrap.wrap
                        gap = Gap("15px 30px")
                    }

                    state.images!!.forEachIndexed { index, photo ->
                        styledDiv {
                            attrs.onClickFunction = {
                                setState {
                                    selectedPhotoIndex = index
                                }
                            }
                            css {
                                backgroundImage = Image("url(/images/${photo.url})")
                                backgroundRepeat = BackgroundRepeat.noRepeat
                                backgroundSize = "cover"
                                width = 280.px
                                backgroundPosition = "center"
                                height = 230.px
                                margin = 10.px.toString()
                            }

                            adminButton(AdminButtonType.Delete) {
                                val photoService = PhotoService(coroutineContext)
                                props.coroutineScope.launch {
                                    photoService.deletePhoto(photo.id!!)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun RBuilder.onePhotoView() {
        if (state.selectedPhotoIndex != null) {
            styledDiv {
                attrs.onClickFunction = {
                    setState {
                        selectedPhotoIndex = null
                    }
                }
                css {
                    top = 0.px
                    left = 0.px
                    position = Position.fixed
                    width = 100.pct
                    height = 100.pct
                    backgroundColor = rgba(0, 0, 0, 0.5)
                    display = Display.flex
                    justifyContent = JustifyContent.center
                    alignItems = Align.center
                }
                styledImg(src = "/images/left-arrow.png") {
                    attrs.onClickFunction = {
                        it.stopPropagation()
                        if (state.selectedPhotoIndex!! > 0) {
                            setState {
                                selectedPhotoIndex = selectedPhotoIndex!! - 1
                            }
                        }
                    }
                    css {
                        width = 55.px
                        margin = 100.px.toString()
                    }
                }
                styledImg(src = "/images/" + state.images?.get(state.selectedPhotoIndex!!)!!.url) {
                    css {
                        height = 500.px
                        backgroundRepeat = BackgroundRepeat.noRepeat
                    }
                }
                styledImg(src = "/images/right-arrow.png") {
                    attrs.onClickFunction = {
                        it.stopPropagation()
                        if (state.selectedPhotoIndex!! < state.images!!.size - 1) {
                            setState {
                                selectedPhotoIndex = selectedPhotoIndex!! + 1
                            }
                        }
                    }
                    css {
                        width = 55.px
                        margin = 100.px.toString()
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {

        grid {
            header {
                +"Галерея"
            }

            navigation {
                child(GalleryNavigation::class){
                    attrs.coroutineScope = props.coroutineScope
                }
            }

            content {
                galleryView()
                onePhotoView()
                if (isAdmin) {
                    child(UploadPhotoComponent::class) {
                        attrs.selectedGallerySection = props.selectedGallerySection
                    }
                }
            }
        }
    }
}