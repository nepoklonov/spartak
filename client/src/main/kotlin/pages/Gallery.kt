package pages

import content
import grid
import header
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import model.NavigationDTO
import model.PhotoDTO
import navigation
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
    var selectedPhotoIndex: Int? = null
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

        grid {
            header {
                +"Галерея"
            }

            navigation {
                if (state.galleryNavigationList != null) {
                    state.galleryNavigationList!!.forEach { galleryNavigation ->
                        route<SmallNavigationProps>("/gallery/:selectedLink") { linkProps ->
                            child(SmallNavigation::class) {
                                attrs.string = galleryNavigation.header
                                attrs.link = galleryNavigation.link
                                attrs.selectedLink = linkProps.match.params.selectedLink
                            }
                        }
                        if (document.cookie.contains("role=admin")) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    val galleryNavigationService = GalleryNavigationService(coroutineContext)
                                    props.coroutineScope.launch {
                                        galleryNavigationService.deleteGallerySection(galleryNavigation.id!!)
                                    }
                                }
                                attrs.type = "delete"
                            }

                            if (state.editSmallNavigationForm != galleryNavigation) {
                                child(AdminButtonComponent::class) {
                                    attrs.updateState = {
                                        setState {
                                            editSmallNavigationForm = galleryNavigation
                                        }
                                    }
                                    attrs.type = "edit"
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
                    }
                    if (document.cookie.contains("role=admin")) {
                        if (!state.smallNavigationForm) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        smallNavigationForm = true
                                    }
                                }
                                attrs.type = "add"
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
                    }
                } else {
                    +"Загрузка..."
                }
            }

            content {

                styledDiv {
                    if (state.images == null) {
                        +"загрузка..."
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

                                    if (document.cookie.contains("role=admin")) {
                                        child(AdminButtonComponent::class) {
                                            attrs.updateState = {
                                                val photoService = PhotoService(coroutineContext)
                                                props.coroutineScope.launch {
                                                    photoService.deletePhoto(photo.id!!)
                                                }
                                            }
                                            attrs.type = "delete"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                styledDiv {
                    if (document.cookie.contains("role=admin")) {
                        if (!state.photoForm) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        photoForm = true
                                    }
                                }
                                attrs.type = "add"
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
            }
        }
    }
}

