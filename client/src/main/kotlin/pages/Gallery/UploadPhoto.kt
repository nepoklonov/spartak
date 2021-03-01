package pages.Gallery

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import model.PhotoDTO
import react.*
import services.PhotoService
import styled.styledButton
import styled.styledDiv

external interface UploadPhotoComponentProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedGallerySection: String
}

class UploadPhotoComponentState : RState {
    var photoForm: Boolean = false
}

class UploadPhotoComponent : RComponent<UploadPhotoComponentProps, UploadPhotoComponentState>() {
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
}