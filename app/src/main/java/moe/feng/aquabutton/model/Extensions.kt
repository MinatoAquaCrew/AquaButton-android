package moe.feng.aquabutton.model

fun <T : ISelectableModel> List<T>.selectItem(item: T?) {
    forEach {
        it.selected = it == item
    }
}
