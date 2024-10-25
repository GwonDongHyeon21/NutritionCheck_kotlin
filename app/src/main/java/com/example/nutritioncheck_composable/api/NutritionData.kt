package com.example.nutritioncheck_composable.api

import android.util.Xml
import com.example.nutritioncheck_composable.BuildConfig
import com.example.nutritioncheck_composable.foodList
import com.example.nutritioncheck_composable.model.NutritionDataModel
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

fun nutritionInfo(food: String) {
    val urlBuilder =
        StringBuilder("https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo01/getFoodNtrCpntDbInq01") // URL
    urlBuilder.append(
        "?" + URLEncoder.encode(
            "serviceKey",
            "UTF-8"
        ) + "=" + BuildConfig.NUTRITION_DATA_API
    ) // Service Key
    urlBuilder.append(
        "&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(
            "1",
            "UTF-8"
        )
    ) // 페이지 번호
    urlBuilder.append(
        "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(
            "100",
            "UTF-8"
        )
    ) // 한 페이지 결과 수
    urlBuilder.append(
        "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(
            "xml",
            "UTF-8"
        )
    ) // XML/JSON 여부
    urlBuilder.append(
        "&" + URLEncoder.encode("FOOD_NM_KR", "UTF-8") + "=" + URLEncoder.encode(
            food,
            "UTF-8"
        )
    ) // 식품명
    urlBuilder.append(
        "&" + URLEncoder.encode("MAKER_NM", "UTF-8") + "=" + URLEncoder.encode(
            "",
            "UTF-8"
        )
    ) // 업체명

    val url = URL(urlBuilder.toString())
    val conn = url.openConnection() as HttpURLConnection
    conn.requestMethod = "GET"
    conn.setRequestProperty("Content-type", "application/json")

    val rd: BufferedReader = if (conn.responseCode in 200..300) {
        BufferedReader(InputStreamReader(conn.inputStream))
    } else {
        BufferedReader(InputStreamReader(conn.errorStream))
    }

    val sb = StringBuilder()
    var line: String?
    while (rd.readLine().also { line = it } != null) {
        sb.append(line)
    }
    rd.close()
    conn.disconnect()
    parseResponse(sb.toString())
}

private fun parseResponse(xml: String) {
    try {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(xml))
        parser.nextTag()

        var eventType = parser.eventType
        var foodName: String? = null
        var calories: String? = null
        var carbohydrate: String? = null
        var sugar: String? = null
        var dietaryFiber: String? = null
        var protein: String? = null
        var province: String? = null
        var saturatedFat: String? = null
        var cholesterol: String? = null
        var sodium: String? = null
        var potassium: String? = null
        var vitaminA: String? = null
        var vitaminC: String? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        "item" -> {}

                        "FOOD_NM_KR" -> foodName = parser.nextText()
                        "AMT_NUM1" -> calories = parser.nextText()
                        "AMT_NUM7" -> carbohydrate = parser.nextText()
                        "AMT_NUM8" -> sugar = parser.nextText()
                        "AMT_NUM9" -> dietaryFiber = parser.nextText()
                        "AMT_NUM3" -> protein = parser.nextText()
                        "AMT_NUM4" -> province = parser.nextText()
                        "AMT_NUM25" -> saturatedFat = parser.nextText()
                        "AMT_NUM24" -> cholesterol = parser.nextText()
                        "AMT_NUM14" -> sodium = parser.nextText()
                        "AMT_NUM13" -> potassium = parser.nextText()
                        "AMT_NUM16" -> vitaminA = parser.nextText()
                        "AMT_NUM22" -> vitaminC = parser.nextText()
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (tagName == "item") {
                        foodList.add(
                            NutritionDataModel(
                                foodName.toString(),
                                calories.toString(),
                                carbohydrate.toString(),
                                sugar.toString(),
                                dietaryFiber.toString(),
                                protein.toString(),
                                province.toString(),
                                saturatedFat.toString(),
                                cholesterol.toString(),
                                sodium.toString(),
                                potassium.toString(),
                                vitaminA.toString(),
                                vitaminC.toString(),
                            )
                        )
                    }
                }
            }
            eventType = parser.next()
        }
    } catch (e: XmlPullParserException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

//https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo01/getFoodNtrCpntDbInq01
// ?serviceKey= MhaV5RpnKLUv9MFQZr7AYd96McfCLNFksae09wTWgCQpXD4hQNXIlcAOugChDbrFeGrrB%2BqN%2FjBQJP%2F6pGCQ%2Fw%3D%3D
// &pageNo=1
// &numOfRows=3
// &type=xml