package com.example.affirmations.locale

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.XmlResourceParser
import android.icu.util.ULocale
import android.os.Build
import com.example.affirmations.R
import java.util.Locale

sealed class LocaleCompat<T>(val value: T) {
     class LocaleValue(value: Locale) : LocaleCompat<Locale>(value)
     class ULocaleValue(value: ULocale) : LocaleCompat<ULocale>(value)
}

/**
 * @return a sealed LocaleCompat class containing either a Locale or ULocale
 */
fun getLocaleCompat(localeString: String) : LocaleCompat<*> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocaleCompat.ULocaleValue(ULocale.forLanguageTag(localeString))
    } else {
        LocaleCompat.LocaleValue(Locale.forLanguageTag(localeString))
    }
}

/**
 * Calculates the locale names using the localeCompat sealed class.
 */
@SuppressLint("NewApi")
class LocaleItem(val localeCode:String, context: Context) {
    val nameInDisplayLocale:String
    val nameInSelf:String
    val current:Boolean
    init {
        val localeCompat = getLocaleCompat(localeCode)
        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        when (localeCompat) {
            is LocaleCompat.LocaleValue -> {
                val locale = localeCompat.value
                val language = locale.displayLanguage
                val country = locale.displayCountry
                val localeLanguage = locale.getDisplayLanguage(locale)
                val localeCountry = locale.getDisplayCountry(locale)
                nameInDisplayLocale = "${language}${when(country.length) {
                    0 -> ""
                    else -> " (${country})"
                }}"
                nameInSelf = "${localeLanguage}${when(localeCountry.length) {
                    0 -> ""
                    else -> " (${localeCountry})"
                }}"
                current = currentLocale.equals(locale)
            }
            is LocaleCompat.ULocaleValue -> {
                val locale = localeCompat.value
                val currentLocale = ULocale.forLocale(currentLocale)
                nameInDisplayLocale = locale.displayName
                nameInSelf = locale.getDisplayName(locale)
                current = locale == currentLocale
            }
        }
    }
}

/**
 * Returns a list of LocaleItem objects based on the contents of the locales_config XML resource
 * file. It uses Android Locale object to populate the LocaleItem objects with reasonable display
 * strings.
 * @param context an Android Context, used to fetch resources
 * @return the list of LocaleItem
 */
fun getLanguages(context: Context) : List<LocaleItem>{
    val xpp = context.resources.getXml(R.xml.locale_config)
    val list = mutableListOf<LocaleItem>()
    var eventType = xpp.next()
    while (eventType != XmlResourceParser.END_DOCUMENT) {
        if (eventType == XmlResourceParser.START_TAG) {
            if (xpp.name == "locale") {
                xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "name")?.let{
                    list.add(LocaleItem(localeCode = it, context))
                }
            }
        }
        eventType = xpp.next()
    }
    return list
}
