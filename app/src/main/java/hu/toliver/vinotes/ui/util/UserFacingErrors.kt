package hu.toliver.vinotes.ui.util

import android.content.Context
import hu.toliver.vinotes.R

fun userFacingErrorMessage(context: Context, raw: String?): String {
    val message = raw?.trim().orEmpty()
    if (message.isEmpty()) return context.getString(R.string.unknown_error)
    if (message.contains("nominatim.openstreetmap.org", ignoreCase = true) ||
        message.contains("Unable to resolve host", ignoreCase = true) ||
        message.contains("UnknownHostException", ignoreCase = true)
    ) {
        return context.getString(R.string.place_lookup_unavailable)
    }
    if (message.contains("CLEARTEXT", ignoreCase = true) ||
        message.contains("network security policy", ignoreCase = true)
    ) {
        return context.getString(R.string.catalog_url_requires_https)
    }
    return message
}