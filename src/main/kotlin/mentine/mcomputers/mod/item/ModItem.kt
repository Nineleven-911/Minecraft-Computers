package mentine.mcomputers.mod.item

import mentine.mcomputers.mod.util.register

object ModItem {
    val handheldComputer: HandheldComputer = HandheldComputer()

    fun onRegister() {
        register("handheld_computer", handheldComputer)
    }
}
