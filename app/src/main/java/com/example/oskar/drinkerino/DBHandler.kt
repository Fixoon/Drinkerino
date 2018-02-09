package com.example.oskar.drinkerino

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*


class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Create all tables onCreate
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_DRINKS_TABLE = "CREATE TABLE Drinks (DrinkID INTEGER PRIMARY KEY, " + "DrinkName TEXT, IsLiked INTEGER, BaseSpirit TEXT, DrinkGlass INTEGER)"
        val CREATE_INGREDIENTS_TABLE = "CREATE TABLE Ingredients (IngredientID INTEGER PRIMARY KEY, " + "IngredientName TEXT)"
        val CREATE_PROPERTIES_TABLE = "CREATE TABLE Properties (PropertyID INTEGER PRIMARY KEY, " + "Property TEXT)"
        val CREATE_INSTRUCTIONS_TABLE = "CREATE TABLE Instructions (InstructionID INTEGER PRIMARY KEY, " +
                "Instructions TEXT, DrinkID INTEGER, FOREIGN KEY(DrinkID) REFERENCES Drinks(DrinkID))"
        val CREATE_MEASUREMENTS_TABLE = "CREATE TABLE Measurements (DrinkID INTEGER, IngredientID INTEGER, " +
                "Measurement TEXT, FOREIGN KEY(DrinkID) REFERENCES Drinks (DrinkID), FOREIGN KEY(IngredientID) " +
                "REFERENCES Ingredients(IngredientID), PRIMARY KEY(DrinkID, IngredientID))"
        val CREATE_DRINKPROPS_TABLE = "CREATE TABLE DrinkProps (DrinkID INTEGER, PropertyID INTEGER, " +
                "FOREIGN KEY(DrinkID) REFERENCES Drinks(DrinkID), FOREIGN KEY(PropertyID) REFERENCES Properties (PropertyID)," +
                "PRIMARY KEY(DrinkID, PropertyID))"
        val CREATE_TOOLS_TABLE = "CREATE TABLE Tools (ToolID INTEGER PRIMARY KEY, ToolName TEXT)"
        val CREATE_DRINKTOOLS_TABLE ="CREATE TABLE DrinkTools (DrinkID INTEGER, ToolID INTEGER, " +
                "FOREIGN KEY(DrinkID) REFERENCES Drinks(DrinkID), FOREIGN KEY(ToolID) REFERENCES Tools(ToolID)" +
                "PRIMARY KEY(DrinkID, ToolID))"

        db.execSQL(CREATE_DRINKS_TABLE)
        db.execSQL(CREATE_INGREDIENTS_TABLE)
        db.execSQL(CREATE_PROPERTIES_TABLE)
        db.execSQL(CREATE_INSTRUCTIONS_TABLE)
        db.execSQL(CREATE_MEASUREMENTS_TABLE)
        db.execSQL(CREATE_DRINKPROPS_TABLE)
        db.execSQL(CREATE_TOOLS_TABLE)
        db.execSQL(CREATE_DRINKTOOLS_TABLE)



        addIngredientsToDB("Rom", db)
        addIngredientsToDB("Mynta", db)
        addIngredientsToDB("Cola", db)
        addIngredientsToDB("Sodavatten", db)
        addIngredientsToDB("Sockerlag", db)
        addIngredientsToDB("Gin", db)
        addIngredientsToDB("Tonic", db)
        addIngredientsToDB("Jack Daniels", db)
        addIngredientsToDB("Vodka", db)
        addIngredientsToDB("Kahlua", db)
        addIngredientsToDB("Mjölk", db)
        addIngredientsToDB("Galliano", db)
        addIngredientsToDB("Apelsinjuice", db)
        addIngredientsToDB("Tequila", db)
        addIngredientsToDB("Grenadine", db)
        addIngredientsToDB("Tranbärsjuice", db)
        addIngredientsToDB("Peach schnapps", db)
        addIngredientsToDB("Cointreau", db)
        addIngredientsToDB("Limejuice", db)
        addIngredientsToDB("Espresso", db)
        addIngredientsToDB("Vaniljvodka", db)
        addIngredientsToDB("Sourz Apple", db)
        addIngredientsToDB("Fruktsoda", db)

        addPropertiesToDB("Söt", db)
        addPropertiesToDB("Sur", db)
        addPropertiesToDB("Besk", db)
        addPropertiesToDB("Syrlig", db)
        addPropertiesToDB("Salt", db)
        addPropertiesToDB("Het", db)

        addToolsToDB("Muddlare", db)
        addToolsToDB("Shaker", db)
        addToolsToDB("Barsked", db)


        addRecipeToDB(Drink("Mojito", "Rom", arrayOf("Rom", "Mynta", "Sodavatten"), arrayOf("6 cl", "3 blad", "10 cl"), arrayOf("Söt"), arrayOf("Muddlare"), "Skär en lime i klyftor~ Lägg lime, mynta och socker i ett glas~ Muddla allt i glaset några gånger~ Fyll glaset med krossad is~ Häll i rom och sodavatten~ Blanda om försiktigt", DrinkGlass.LOWBALL), db)
        addRecipeToDB(Drink("Cuba Libre","Rom", arrayOf("Rom", "Cola"), arrayOf("5 cl", "12 cl"), arrayOf("Söt"), arrayOf(), "Fyll ett glas med isbitar~ Häll i rom och cola~ Blanda om", DrinkGlass.HIGHBALL), db)
        addRecipeToDB(Drink("Gin & Tonic", "Gin", arrayOf("Gin", "Tonic"), arrayOf("6 cl", "10 cl"), arrayOf("Besk"), arrayOf(), "Häll i gin och tonic i ett glas~ Blanda om",DrinkGlass.HIGHBALL), db)
        addRecipeToDB(Drink("Jack & Cola", "Whiskey", arrayOf("Jack Daniels", "Cola"), arrayOf("6 cl", "10 cl"), arrayOf("Söt"), arrayOf(), "Häll i Jack Daniels och cola i ett glas~ Blanda om", DrinkGlass.HIGHBALL), db)
        addRecipeToDB(Drink("Black Russian", "Vodka", arrayOf("Vodka", "Kahlua"), arrayOf("5 cl", "2 cl"), arrayOf("Söt"), arrayOf(), "Häll i vodka och Kahlua i ett glas~ Blanda om", DrinkGlass.LOWBALL), db)
        addRecipeToDB(Drink("White Russian", "Vodka", arrayOf("Vodka", "Kahlua", "Mjölk"), arrayOf("5 cl", "2 cl", "3 cl"), arrayOf("Söt"), arrayOf(), "Häll i vodka, Kahlua och grädde i ett glas~ Blanda om", DrinkGlass.LOWBALL), db)
        addRecipeToDB(Drink("Harvey Wallbanger", "Vodka", arrayOf("Vodka", "Galliano", "Apelsinjuice"), arrayOf("4,5 cl", "1,5 cl", "9 cl"), arrayOf("Sur"), arrayOf(), "Häll vodka, Galliano och apelsinjuice i ett glas~ Blanda om", DrinkGlass.HIGHBALL), db)
        addRecipeToDB(Drink("Tequila Sunrise", "Tequila", arrayOf("Tequila", "Apelsinjuice", "Grenadine"), arrayOf("4,5 cl", "9 cl", "1,5 cl"), arrayOf("Besk"), arrayOf(), "Häll tequila och apelsinjuice i ett glas~ Blanda om~ Häll i grenadine försiktigt", DrinkGlass.HIGHBALL), db)
        addRecipeToDB(Drink("Margarita", "Cachaca", arrayOf("Tequila", "Cointreau", "Limejuice"), arrayOf("3,5 cl", "2 cl", "1,5 cl"), arrayOf("Sur", "Besk"), arrayOf("Muddlare", "Shaker"), "Häll sockerlag på kanten av ett glas och doppa det i en tallrik med salt~ Häll i tequila, Cointreau och limejuice i glaset~ Blanda om", DrinkGlass.FLUTE), db)
        addRecipeToDB(Drink("Espresso Martini", "Vodka", arrayOf("Vodka", "Kahlua", "Espresso"), arrayOf("4 cl", "4 cl", "4 cl"), arrayOf("Söt"), arrayOf("Shaker"), "Häll i vodka~ Kahlua och espresso i en shaker med is~ Skaka om rejält~ Häll upp i ett glas", DrinkGlass.FLUTE), db)
        addRecipeToDB(Drink("P2", "Vodka", arrayOf("Vaniljvodka", "Sourz Apple", "Fruktsoda"), arrayOf("3 cl", "3 cl", "10 cl"), arrayOf("Söt", "Sur"), arrayOf(), "Häll i vodka och sourz i ett glas~ Fyll på med fruktsoda", DrinkGlass.HIGHBALL), db)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + "Drinks")
        // Creating tables again
        onCreate(db)
    }

    private fun addRecipeToDB(drink: Drink, db: SQLiteDatabase) {
        val drinkID = addDrinkToDB(drink.name, drink.baseSpirit, drink.drinkGlass.glassID, db)
        val ingredients = drink.ingredients
        val properties = drink.properties
        val measurements = drink.measurements
        val tools = drink.tools
        for (i in ingredients.indices) {
            assignMeasurementsToDrink(drinkID.toInt(), getIngredientIDByName(ingredients[i], db), measurements[i], db)
        }
        for (i in properties.indices) {
            assignPropertiesToDrink(drinkID.toInt(), getPropertyIDByName(properties[i], db), db)
        }
        for (i in tools.indices) {
            assignToolsToDrink(drinkID.toInt(), getToolIDByName(tools[i], db), db)
        }
        addInstructions(drink.instructions, drinkID.toInt(), db)

    }


    private fun addDrinkToDB(name: String, baseSpirit:String, drinkGlass:Int, db: SQLiteDatabase): Long {
        val values = ContentValues()

        values.put("DrinkName", name)
        values.put("IsLiked", 0)
        values.put("BaseSpirit", baseSpirit)
        values.put("DrinkGlass", drinkGlass)

        // Inserting Row
        return db.insert("Drinks", null, values)
    }

    private fun addIngredientsToDB(ingredient: String, db: SQLiteDatabase) {
        val values = ContentValues()

        values.put("IngredientName", ingredient)

        db.insert("Ingredients", null, values)
    }

    private fun addPropertiesToDB(property: String, db: SQLiteDatabase) {
        val values = ContentValues()
        values.put("Property", property)

        db.insert("Properties", null, values)
    }

    private fun addToolsToDB(tool: String, db: SQLiteDatabase) {
        val values = ContentValues()
        values.put("ToolName", tool)

        db.insert("Tools", null, values)
    }

    private fun addInstructions(instructions: String, drinkID: Int, db: SQLiteDatabase) {
        val values = ContentValues()

        values.put("Instructions", instructions)
        values.put("DrinkID", drinkID)

        db.insert("Instructions", null, values)
    }

    private fun assignMeasurementsToDrink(drinkID: Int, ingredientID: Int, measurement: String, db: SQLiteDatabase) {
        val values = ContentValues()

        values.put("DrinkID", drinkID)
        values.put("IngredientID", ingredientID)
        values.put("Measurement", measurement)

        db.insert("Measurements", null, values)
    }

    private fun assignPropertiesToDrink(drinkID: Int, propertyID: Int, db: SQLiteDatabase) {
        val values = ContentValues()

        values.put("DrinkID", drinkID)
        values.put("PropertyID", propertyID)

        db.insert("DrinkProps", null, values)
    }

    private fun assignToolsToDrink(drinkID: Int, toolID: Int, db:SQLiteDatabase){
        val values = ContentValues()

        values.put("DrinkID", drinkID)
        values.put("ToolID", toolID)

        db.insert("DrinkTools", null, values)
    }

    private fun getIngredientIDByName(ingredient: String, db: SQLiteDatabase): Int {
        val projection = arrayOf("IngredientID")

        val selection = "IngredientName" + " = ?"
        val selectionArgs = arrayOf(ingredient)

        val cursor = db.query("Ingredients", projection, selection, selectionArgs, null, null, null)

        cursor?.moveToFirst()

        return cursor!!.getInt(0)
    }

    private fun getPropertyIDByName(property: String, db: SQLiteDatabase): Int {
        val projection = arrayOf("PropertyID")

        val selection = "Property" + " = ?"
        val selectionArgs = arrayOf(property)

        val cursor = db.query("Properties", projection, selection, selectionArgs, null, null, null)

        cursor?.moveToFirst()

        return cursor!!.getInt(0)
    }

    private fun getToolIDByName(tool: String, db: SQLiteDatabase): Int {
        val projection = arrayOf("ToolID")

        val selection = "ToolName" + " = ?"
        val selectionArgs = arrayOf(tool)

        val cursor = db.query("Tools", projection, selection, selectionArgs, null, null, null)

        cursor?.moveToFirst()

        return cursor!!.getInt(0)
    }

    fun getFullRecipe(id: Int): Drink {
        val db = this.readableDatabase
        val id = id.toString()

        var drinkName = ""
        var baseSpirit = ""
        var drinkGlass:DrinkGlass = DrinkGlass.LOWBALL
        var ingredients = arrayOf<String>()
        var measurements = arrayOf<String>()
        var property = arrayOf<String>()
        var tools:Array<String> = arrayOf<String>()
        var instructions = ""

        val selectionArgs = arrayOf(id, id, id, id, id, id)
        val query = "SELECT Drinks.DrinkName, Drinks.BaseSpirit, Drinks.DrinkGlass, " +
                "(SELECT GROUP_CONCAT(Ingredients.IngredientName, '~') FROM Measurements INNER JOIN Ingredients ON " +
                "Measurements.IngredientID=Ingredients.IngredientID WHERE Measurements.DrinkID = ?), " +
                "(SELECT GROUP_CONCAT(Measurements.Measurement, '~') FROM Measurements WHERE Measurements.DrinkID = ?), " +
                "(SELECT GROUP_CONCAT(Properties.Property, '~') FROM DrinkProps INNER JOIN Properties ON " +
                "DrinkProps.PropertyID=Properties.PropertyID WHERE DrinkProps.DrinkID = ?), " +
                "ifnull((SELECT GROUP_CONCAT(Tools.ToolName, '~') FROM DrinkTools INNER JOIN Tools ON " +
                "DrinkTools.ToolID=Tools.ToolID WHERE DrinkTools.DrinkID = ?), \"\"), " +
                "(SELECT Instructions.Instructions FROM Instructions WHERE Instructions.DrinkID = ?) " +
                "FROM Drinks WHERE Drinks.DrinkID = ?"

        val cursor = db.rawQuery(query, selectionArgs)

        if (cursor.moveToFirst()) {
            do {
                drinkName = cursor.getString(0)
                baseSpirit = cursor.getString(1)
                drinkGlass = DrinkGlass.fromInt(cursor.getInt(2))!!
                ingredients = cursor.getString(3).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                measurements = cursor.getString(4).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                property = cursor.getString(5).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                tools = cursor.getString(6).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                instructions = cursor.getString(7)
            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return Drink(drinkName, baseSpirit, ingredients, measurements, property, tools, instructions, drinkGlass)
    }

    /**
     * Get drinks as an ArrayList with optional filter
     */
    fun getDrinksByFilter(isLiked: LikeState, filter:FilterObject? = null): ArrayList<SimpleDrink>
    {
        val db = this.readableDatabase
        val drinkList = ArrayList<SimpleDrink>()

        var searchProps:String = ""
        var searchIngredients:String = ""
        var searchLikes:String = ""
        var other:String = ")"


        if(filter != null){
            if(!filter.properties.isEmpty()){
                searchProps = "AND Properties.Property IN ('" + filter.properties.joinToString("','") + "')"
            }
            if(filter.drinkBaseOther){
                other = "OR Drinks.BaseSpirit NOT IN ('Rom', 'Vodka', 'Tequila', 'Gin', 'Whiskey'))"
            }
            if(!filter.drinkBase.isEmpty()){
                searchIngredients = "AND (Drinks.BaseSpirit IN ('" + filter.drinkBase.joinToString("','") + "')" + other
            }
        }
        if(isLiked != LikeState.IGNORE){
            searchLikes = "AND Drinks.IsLiked IN (" + isLiked.boolInt + ")"
        }


        val query = "SELECT Drinks.DrinkID, Drinks.DrinkName, Drinks.IsLiked, Drinks.DrinkGlass, " +
                "GROUP_CONCAT(DISTINCT Properties.Property) " +
                "FROM Drinks INNER JOIN DrinkProps ON Drinks.DrinkID=DrinkProps.DrinkID " +
                "INNER JOIN Properties ON DrinkProps.PropertyID=Properties.PropertyID " +
                "WHERE 1=1 " + searchProps + searchIngredients + searchLikes + " GROUP BY Drinks.DrinkID";
        val cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                drinkList.add(SimpleDrink(cursor.getString(1), cursor.getString(4), cursor.getInt(0), LikeState.fromInt(cursor.getInt(2))!!, DrinkGlass.fromInt(cursor.getInt(3))!!))
            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return drinkList
    }

    /**
     * Change like state of a drink
     *
     * @param likeState New like state
     */
    fun setLikeState(id: Int, likeState: LikeState){
        val db = this.writableDatabase
        val id = id.toString()

        var vals = ContentValues()
        vals.put("IsLiked", likeState.boolInt)
        db.update("Drinks", vals, "DrinkID=?", arrayOf(id))

        db.close()
    }

    companion object {
        // Database Version
        private val DATABASE_VERSION = 1
        // Database Name
        private val DATABASE_NAME = "Drinks"
    }
}
