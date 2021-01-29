package view
//Попытка написать общий компонент для всех панелей навигации на страницах Teams, Gallery и Games, потому что они абсолютно одинаковые

//open class NavigationObject(
//    val link: String,
//    val header: String
//)
//
//external interface SmallNavigationProps : RProps {
//    var navigationList: List<NavigationObject>
//    var selectedChampionship: String
//}
//
//class SmallNavigationComponent: RComponent<SmallNavigationProps, RState>() {
//    override fun RBuilder.render() {
//        styledDiv {
//            css {
//                float = kotlinx.css.Float.left
//                backgroundColor = Color.white
//                textDecoration = TextDecoration.none
//            }
//            props.navigationList.forEach {
//                navLink<GamesProps>(to = it.link) {
//                    styledDiv {
//                        css {
//                            textAlign = TextAlign.center
//                            color = ColorSpartak.Red.color
//                            width = 200.px
//                        }
//                        styledH2 {
//                            css {
//                                margin = 40.px.toString()
//                            }
//                            +it.header
//                        }
//                    }
//                }
//            }
//        }
//    }
//}