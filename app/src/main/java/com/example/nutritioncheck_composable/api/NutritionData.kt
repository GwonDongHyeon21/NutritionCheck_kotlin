package com.example.nutritioncheck_composable.api

import android.annotation.SuppressLint
import android.util.Xml
import com.example.nutritioncheck_composable.BuildConfig
import com.example.nutritioncheck_composable.ValueSingleton
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
            "serviceKey", "UTF-8"
        ) + "=" + BuildConfig.NUTRITION_DATA_API
    ) // Service Key
    urlBuilder.append(
        "&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(
            "1", "UTF-8"
        )
    ) // 페이지 번호
    urlBuilder.append(
        "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(
            "100", "UTF-8"
        )
    ) // 한 페이지 결과 수
    urlBuilder.append(
        "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(
            "xml", "UTF-8"
        )
    ) // XML/JSON 여부
    urlBuilder.append(
        "&" + URLEncoder.encode("FOOD_NM_KR", "UTF-8") + "=" + URLEncoder.encode(
            food, "UTF-8"
        )
    ) // 식품명
    urlBuilder.append(
        "&" + URLEncoder.encode("MAKER_NM", "UTF-8") + "=" + URLEncoder.encode(
            "", "UTF-8"
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

@SuppressLint("DefaultLocale")
private fun parseResponse(xml: String) {
    try {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(xml))
        parser.nextTag()

        var eventType = parser.eventType
        var foodName = ""
        var calories = ""
        var carbohydrate = ""
        var sugar = ""
        var dietaryFiber = ""
        var protein = ""
        var province = ""
        var saturatedFat = ""
        var cholesterol = ""
        var sodium = ""
        var potassium = ""
        var vitaminA = ""
        var vitaminC = ""
        var amountPer = ""
        var amountAll = ""
        var makerName = ""

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
                        "AMT_NUM15" -> vitaminA = parser.nextText()
                        "AMT_NUM22" -> vitaminC = parser.nextText()
                        "NUTRI_AMOUNT_SERVING" -> amountPer = parser.nextText()
                        "Z10500" -> amountAll = parser.nextText()
                        "MAKER_NM" -> makerName = parser.nextText()
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (tagName == "item") {
                        var amount = 0.0
                        var multiply = 1.0
                        if (amountAll.isNotEmpty())
                            amount = amountAll.replace(",", "").replace("g", "").toDouble()
                        if (amountPer.isEmpty() && amount > 100)
                            multiply = amount / 100

                        ValueSingleton.foodList.add(NutritionDataModel(
                            System.currentTimeMillis(),
                            foodName,
                            String.format(
                                "%.1f",
                                (calories.takeIf { it.isNotEmpty() }?.replace(",", "")?.toDouble()
                                    ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f",
                                (carbohydrate.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f", (sugar.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f",
                                (dietaryFiber.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f", (protein.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f", (province.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f",
                                (saturatedFat.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f",
                                (cholesterol.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f", (sodium.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f", (potassium.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f", (vitaminA.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            String.format(
                                "%.1f", (vitaminC.takeIf { it.isNotEmpty() }?.replace(",", "")
                                    ?.toDouble() ?: 0.0) * multiply
                            ),
                            amountPer.takeIf { it.isNotEmpty() } ?: "",
                            amountAll.takeIf { it.isNotEmpty() } ?: "",
                            makerName.takeIf { it.isNotEmpty() } ?: "없음",
                        ))
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