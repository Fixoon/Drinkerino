package com.example.oskar.drinkerino.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.oskar.drinkerino.enums.DrinkGlass
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.objects.Drink
import com.example.oskar.drinkerino.objects.Filter
import com.example.oskar.drinkerino.objects.SimpleDrink
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DBHelper(private var context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    fun checkDBExist(): Boolean {
        var checkDB: SQLiteDatabase? = null

        try {
            val myPath = DB_PATH + DB_NAME
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)
            checkDB.close()

        } catch (e: SQLiteException) {
            //Database doesn't exist. Call "this.readableDatabase" to create Database
            this.readableDatabase
        }

        return checkDB != null
    }

    @Throws(IOException::class)
    fun copyDataBase() {

        try {
            val assetFile = context.assets.open(DB_NAME)
            val outputFile = FileOutputStream(DB_PATH + DB_NAME)
            val buffer = ByteArray(1024)

            var length = assetFile.read(buffer)
            while (length > 0) {
                outputFile.write(buffer, 0, length)
                length = assetFile.read(buffer)
            }

            outputFile.flush()
            outputFile.close()
            assetFile.close()

        } catch (e: IOException) {
            throw Error("Error copying database")
        }
    }

    fun getFullRecipe(id: Int): Drink {
        val db = this.readableDatabase
        val idString = id.toString()

        var drinkName = ""
        var baseSpirit = ""
        var drinkGlass: DrinkGlass = DrinkGlass.LOWBALL
        var likeState: LikeState = LikeState.IGNORE
        var ingredients = arrayOf<String>()
        var measurements = arrayOf<String>()
        var property = arrayOf<String>()
        var tools: Array<String> = arrayOf()
        var instructions = ""

        val selectionArgs = arrayOf(idString, idString, idString, idString, idString, idString)
        val query = "SELECT Drinks.DrinkName, Drinks.BaseSpirit, Drinks.DrinkGlass, Drinks.IsLiked, " +
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
                likeState = LikeState.fromInt(cursor.getInt(3))!!
                ingredients = cursor.getString(4).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                measurements = cursor.getString(5).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                property = cursor.getString(6).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                tools = cursor.getString(7).split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                instructions = cursor.getString(8)
            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return Drink(drinkName, id, baseSpirit, ingredients, measurements, property, tools, instructions, drinkGlass, likeState)
    }

    fun getDrinksByFilter(isLiked: LikeState, filter: Filter? = null): ArrayList<SimpleDrink> {
        val db = this.readableDatabase
        val drinkList = ArrayList<SimpleDrink>()

        var searchProps = ""
        var searchIngredients = ""
        var searchLikes = ""
        var other = ")"


        if (filter != null) {
            if (!filter.properties.isEmpty()) {
                searchProps = "AND DrinkProps.DrinkID IN (SELECT DrinkProps.DrinkID FROM DrinkProps " +
                        "INNER JOIN Properties ON DrinkProps.PropertyID=Properties.PropertyID " +
                        "WHERE Properties.Property IN ('" + filter.properties.joinToString("','") + "'))"
            }
            if (filter.drinkBaseOther) {
                other = "OR Drinks.BaseSpirit NOT IN ('Rom', 'Vodka', 'Tequila', 'Gin', 'Whiskey'))"
            }
            if (!filter.drinkBase.isEmpty()) {
                searchIngredients = "AND (Drinks.BaseSpirit IN " +
                        "('" + filter.drinkBase.joinToString("','") + "')" + other
            }
        }
        if (isLiked != LikeState.IGNORE) {
            searchLikes = "AND Drinks.IsLiked IN (" + isLiked.boolInt + ")"
        }


        val query = "SELECT Drinks.DrinkID, Drinks.DrinkName, Drinks.IsLiked, Drinks.DrinkGlass, " +
                "GROUP_CONCAT(Properties.Property) " +
                "FROM Drinks INNER JOIN DrinkProps ON Drinks.DrinkID=DrinkProps.DrinkID " +
                "INNER JOIN Properties ON DrinkProps.PropertyID=Properties.PropertyID " +
                "WHERE 1=1 " + searchProps + searchIngredients + searchLikes + " GROUP BY Drinks.DrinkID"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                drinkList.add(SimpleDrink(cursor.getString(1),
                        cursor.getString(4),
                        cursor.getInt(0),
                        LikeState.fromInt(cursor.getInt(2))!!,
                        DrinkGlass.fromInt(cursor.getInt(3))!!))
            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return drinkList
    }

    fun setLikeState(id: Int, likeState: LikeState) {
        val db = this.writableDatabase
        val idString = id.toString()

        val values = ContentValues()
        values.put("IsLiked", likeState.boolInt)
        db.update("Drinks", values, "DrinkID=?", arrayOf(idString))

        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        copyDataBase()
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    companion object {
        @SuppressLint("SdCardPath")
        private val DB_PATH = "/data/data/com.example.oskar.drinkerino/databases/"
        private const val DB_NAME = "Drinks"
    }
}