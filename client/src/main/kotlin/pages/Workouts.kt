package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.WorkoutDTO
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.navLink
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledH2
import view.ColorSpartak

data class WorkoutsNavigation(val header: String, val link: String) {
}

val workoutsNavigationList = listOf(
    WorkoutsNavigation("ШХМ", "/workouts/shhm"),
    WorkoutsNavigation("Спартак 2013", "/workouts/2013"),
    WorkoutsNavigation("Спартак 2003-2004", "/workouts/2003"),
    WorkoutsNavigation("Спартак 2005", "/workouts/2005"),
    WorkoutsNavigation("Вратарские Тренировки", "/workouts/goalkeepers"),
    WorkoutsNavigation("Группа набора", "/workouts/recruitment"),
    WorkoutsNavigation("Спартак 2006", "/workouts/2006"),
    WorkoutsNavigation("Спартак Красная Ракета", "/workouts/red"),
    WorkoutsNavigation("Спартак 2008", "/workouts/20008"),
)


external interface WorkoutsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedString: String
}

class WorkoutsState : RState {
    var error: Throwable? = null
    var workouts: List<WorkoutDTO>? = null

}

class Workouts : RComponent<WorkoutsProps, WorkoutsState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                overflow = Overflow.hidden
            }

            styledH1 {
                +"Тренировки"
            }
            styledDiv {
                css {
                    float = kotlinx.css.Float.left
                    backgroundColor = Color.white
                    textDecoration = TextDecoration.none
                }
                workoutsNavigationList.forEach {
                    navLink<WorkoutsProps>(to = it.link) {
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
        }
    }
}