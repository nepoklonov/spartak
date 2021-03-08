package pages.gallery

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
import consts.galleryInputs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.PhotoDTO
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import services.PhotoService
import styled.styledDiv
import styled.styledForm

external interface UploadPhotoComponentProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedGallerySection: String
}

class UploadPhotoComponentState : FormState {
    var photoForm: Boolean = false
    override var inputs: MutableMap<String, Input> = galleryInputs

}

class UploadPhotoComponent : RComponent<UploadPhotoComponentProps, UploadPhotoComponentState>() {
    init {
        state.photoForm = false
        state.inputs = galleryInputs
    }
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext
    override fun RBuilder.render() {
        styledDiv {
            if (!state.photoForm) {
                adminButton(AdminButtonType.Add) {
                    setState {
                        photoForm = true
                    }
                }
            } else {
                styledForm{
                    attrs.onSubmitFunction = { event ->
                        event.preventDefault()
                        event.stopPropagation()
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
                    formComponent(this)
                }
            }
        }
    }
}