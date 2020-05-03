package aquacrew.aquabutton.model

fun <T : ISelectableModel> List<T>.selectItem(item: T?) {
    forEach {
        it.selected = it == item
    }
}

fun textTranslationOf(vararg pairs: Pair<String, String?>): TextTranslation {
    val textTranslation = TextTranslation()
    pairs.forEach { (key, value) ->
        if (value != null) {
            textTranslation[key] = value
        }
    }
    return textTranslation
}

class LicenseItemBuilder {

    var name: String = ""
    var license: String = ""
    var author: String = ""
    var url: String = ""

    fun githubUrl(repoPath: String) {
        url = "https://github.com/$repoPath"
    }

    fun build(): LicenseItem {
        return LicenseItem(name, license, author, url)
    }

}

class LicensesListBuilder {

    private val list = mutableListOf<LicenseItem>()

    fun license(block: LicenseItemBuilder.() -> Unit) {
        list += LicenseItemBuilder().apply(block).build()
    }

    fun sortByName() {
        list.sortBy { it.productName }
    }

    fun build() = list.toList()

}

fun licenses(block: LicensesListBuilder.() -> Unit): List<LicenseItem> {
    return LicensesListBuilder().apply(block).build()
}

fun LicensesListBuilder.apache2(block: LicenseItemBuilder.() -> Unit) {
    license {
        license = "Apache 2.0 License"
        block()
    }
}

fun LicensesListBuilder.mit(block: LicenseItemBuilder.() -> Unit) {
    license {
        license = "MIT License"
        block()
    }
}

fun LicensesListBuilder.bsd3clause(block: LicenseItemBuilder.() -> Unit) {
    license {
        license = "BSD 3-Clause License"
        block()
    }
}
