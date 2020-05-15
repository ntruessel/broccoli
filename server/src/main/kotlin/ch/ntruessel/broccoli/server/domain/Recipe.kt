package ch.ntruessel.broccoli.server.domain

class Recipe(
        val id: RecipeId = RecipeId(),
        var name: String,
        var yield: Yield,
        var ingredients: MutableList<Ingredient>,
        var instructions: MutableList<String>,
        var tags: MutableList<Tag>
)
