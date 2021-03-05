package pages.gallery

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import loading
import model.NavigationDTO
import react.*
import react.router.dom.route
import services.GalleryNavigationService
import structure.SmallNavigation
import structure.SmallNavigationProps

external interface GalleryNavigationProps : RProps {
    var coroutineScope: CoroutineScope
}

class GalleryNavigationState : RState {
    var galleryNavigationList: List<NavigationDTO>? = null
}

class GalleryNavigation : RComponent<GalleryNavigationProps, GalleryNavigationState>() {
    init {
        state = GalleryNavigationState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val galleryNavigationService = GalleryNavigationService(coroutineContext)
        props.coroutineScope.launch {
            val galleryNavigationList = galleryNavigationService.getGalleryNavigationList()


            setState {
                this.galleryNavigationList = galleryNavigationList
            }
        }
    }


    override fun RBuilder.render() {
        val galleryNavigationService = GalleryNavigationService(coroutineContext)
        state.galleryNavigationList?.let { galleryNavigationList ->
            route<SmallNavigationProps>("/gallery/:selectedLineLink") { selectedLineLink ->
                child(SmallNavigation::class) {
                    attrs.navLines = galleryNavigationList
                    attrs.selectedLineLink = selectedLineLink.match.params.selectedLineLink
                    attrs.deleteFunction = { id: Int ->
                        props.coroutineScope.launch {
                            galleryNavigationService.deleteGallerySection(id)
                        }
                    }
                    attrs.editFunction = { id: Int, listOfInputValues: List<String> ->
                        props.coroutineScope.launch {
                            galleryNavigationService.editGallerySection(
                                NavigationDTO(
                                    id,
                                    listOfInputValues[0],
                                    listOfInputValues[1]
                                )
                            )
                        }
                    }
                    attrs.addFunction = { listOfInputValues ->
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
        } ?: run { loading() }
    }
}