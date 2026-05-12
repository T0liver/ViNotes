package hu.toliver.winotes.domain.model.enums

enum class WineSweetness {
    /*
    * | Rating      | Sugar content (g/l) |
    * | ----------- | ------------------- |
    * | Brut Nature | 0–3                 |
    * | Extra Brut  | 0–6                 |
    * | Brut        | 0–12                |
    * | Extra Dry   | 12–17               |
    * | Dry         | 17–32               |
    * | Semi-dry    | 32–50               |
    * | Sweet       | 50+                 |
    * */
    BRUT_NATURE, EXTRA_BRUT, BRUT, EXTRA_DRY, DRY, SEMI_DRY, SWEET, UNKNOWN
}