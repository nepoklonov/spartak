package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import model.NavigationDTO
import model.PhotoDTO
import pageComponents.*
import react.*
import react.router.dom.route
import services.GalleryNavigationService
import services.PhotoService
import styled.*


external interface GalleryProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedGallerySection: String
}

class GalleryState : RState {
    var error: Throwable? = null
    var images: List<PhotoDTO>? = null
    var galleryNavigationList: List<NavigationDTO>? = null
    var photoForm: Boolean = false
    var smallNavigationForm: Boolean = false
    var editSmallNavigationForm: NavigationDTO? = null
}

class Gallery : RComponent<GalleryProps, GalleryState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(section: String, coroutineScope: CoroutineScope) {
        val photoService = PhotoService(coroutineContext)
        val galleryNavigationService = GalleryNavigationService(coroutineContext)
        coroutineScope.launch {
            val galleryNavigationList = try {
                galleryNavigationService.getGalleryNavigationList()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
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
                this.galleryNavigationList = galleryNavigationList
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
                if (state.galleryNavigationList != null) {
                    state.galleryNavigationList!!.forEach { galleryNavigation ->
                        route<SmallNavigationProps>("/gallery/:selectedLink") { linkProps ->
                            child(SmallNavigation::class) {
                                attrs.string = galleryNavigation.header
                                attrs.link = galleryNavigation.link
                                attrs.selectedLink = linkProps.match.params.selectedLink
                            }
                        }
                        child(DeleteButtonComponent::class) {
                            attrs.updateState = {
                                val galleryNavigationService = GalleryNavigationService(coroutineContext)
                                props.coroutineScope.launch {
                                    galleryNavigationService.deleteGallerySection(galleryNavigation.id!!)
                                }
                            }
                        }
                        if (state.editSmallNavigationForm != galleryNavigation) {
                            child(EditButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        editSmallNavigationForm = galleryNavigation
                                    }
                                }
                            }
                        } else {
                            child(SmallNavigationForm::class) {
                                attrs.inputValues = listOf(galleryNavigation.header, galleryNavigation.link)
                                attrs.addSection = { listOfInputValues ->
                                    val galleryNavigationService = GalleryNavigationService(coroutineContext)
                                    props.coroutineScope.launch {
                                        galleryNavigationService.editGallerySection(
                                            NavigationDTO(
                                                galleryNavigation.id,
                                                listOfInputValues[0],
                                                listOfInputValues[1]
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (!state.smallNavigationForm) {
                        child(AddButtonComponent::class) {
                            attrs.updateState = {
                                setState {
                                    smallNavigationForm = true
                                }
                            }
                        }
                    } else {
                        child(SmallNavigationForm::class) {
                            attrs.inputValues = listOf("", "")
                            attrs.addSection = { listOfInputValues ->
                                val galleryNavigationService = GalleryNavigationService(coroutineContext)
                                props.coroutineScope.launch {
                                    galleryNavigationService.addGallerySection(
                                        NavigationDTO(
                                            null,
                                            listOfInputValues[0],
                                            listOfInputValues[1]
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    +"Загрузка..."
                }
            }

            styledDiv {
                if (state.images == null) {
                    +"загрузка..."
                } else {
                    state.images!!.forEach {
                        styledDiv {
                            css {
                                backgroundImage = Image("url(/images/${it.url})")
                                backgroundSize = 230.px.toString()
                                width = 230.px
                                height = 230.px
                                margin = 10.px.toString()
                                float = Float.left
                            }

                            child(DeleteButtonComponent::class) {
                                attrs.updateState = {
                                    val photoService = PhotoService(coroutineContext)
                                    props.coroutineScope.launch {
                                        photoService.deletePhoto(it.id!!)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!state.photoForm) {
            child(AddButtonComponent::class) {
                attrs.updateState = {
                    setState {
                        photoForm = true
                    }
                }
            }
        } else {
            styledButton {
                attrs.onClickFunction = {
                    val photoService = PhotoService(coroutineContext)
                    props.coroutineScope.launch {
                        photoService.addPhoto(
                            PhotoDTO(
                                null,
                                "address.png",
                                props.selectedGallerySection
                            )
                        )
                    }
                }
                +"Добавить изображение (потом тут будет загрузка фоток честно)"
            }
        }
    }
}