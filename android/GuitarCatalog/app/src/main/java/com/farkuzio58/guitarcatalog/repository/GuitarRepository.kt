/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Repositorio para interactual con la API.
 */

package com.farkuzio58.guitarcatalog.repository

import com.farkuzio58.guitarcatalog.data.Guitar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class GuitarRepository {
    companion object {
        suspend fun isHostAccessible(host: String): Boolean {
            return withContext(Dispatchers.IO) {
                try {
                    val address = InetAddress.getByName(host)
                    address.isReachable(5000)
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
        }

        suspend fun getBrands(ip:String): List<String>? {

            if (!isHostAccessible(ip)){
                return null
            }
            val url = "https://$ip/brand"

            return withContext(Dispatchers.IO) {
                val url = URL(url)
                val connection = url.openConnection() as HttpURLConnection

                if (connection is HttpsURLConnection) {
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun checkServerTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? =
                            null
                    })
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                    connection.sslSocketFactory = sslContext.socketFactory
                    connection.hostnameVerifier = javax.net.ssl.HostnameVerifier { _, _ -> true }
                }
                try {
                    connection.requestMethod = "GET"
                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val bufferedReader =
                            BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuffer()
                        var inputLine: String?
                        while (bufferedReader.readLine().also { inputLine = it } != null) {
                            response.append(inputLine)
                        }
                        bufferedReader.close()
                        val gson = Gson()
                        var myType: Type = object : TypeToken<List<String>>() {}.type
                        val brands = gson.fromJson<List<String>>(response.toString(), myType)
                        brands
                    } else
                        null
                } finally {
                    connection.disconnect()
                }
            }
        }

        suspend fun getPriceRange(ip: String, ids: String?, shape:String?, brand:String?, tremolo:String?, strings:String?): List<Float>? {
            var thereIsParams = false

            if (!isHostAccessible(ip)){
                return null
            }
            val url = "https://$ip/price"

            var urlTmp = url
            if (ids != null){
                urlTmp += "?favs=$ids"
                thereIsParams = true
            }
            if(shape != null){
                urlTmp += "?shape=$shape"
                thereIsParams = true
            }
            if(brand != null){
                urlTmp += if(thereIsParams)
                    "&brand=$brand"
                else
                    "?brand=$brand"
                thereIsParams = true
            }
            if(tremolo != null){
                urlTmp += if(thereIsParams)
                    "&tremolo=${tremolo.lowercase().replace("si", "yes")}"
                else
                    "?tremolo=${tremolo.lowercase().replace("si", "yes")}"
                thereIsParams = true
            }
            if(strings != null){
                urlTmp += if(thereIsParams)
                    "&strings=$strings"
                else
                    "?strings=$strings"
                thereIsParams = true
            }

            return withContext(Dispatchers.IO) {
                val url = URL(urlTmp)
                val connection = url.openConnection() as HttpURLConnection

                if (connection is HttpsURLConnection) {
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun checkServerTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? =
                            null
                    })
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                    connection.sslSocketFactory = sslContext.socketFactory
                    connection.hostnameVerifier = javax.net.ssl.HostnameVerifier { _, _ -> true }
                }
                try {
                    connection.requestMethod = "GET"
                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val bufferedReader =
                            BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuffer()
                        var inputLine: String?
                        while (bufferedReader.readLine().also { inputLine = it } != null) {
                            response.append(inputLine)
                        }
                        bufferedReader.close()
                        val gson = Gson()
                        var myType: Type = object : TypeToken<List<Float>>() {}.type
                        val priceRange = gson.fromJson<List<Float>>(response.toString(), myType)
                        priceRange
                    } else
                        null
                } finally {
                    connection.disconnect()
                }
            }
        }

        suspend fun getGuitar(ip:String, id: Int): Guitar? {
            if (!isHostAccessible(ip))
                return null

            val urlGet = "https://$ip/get?id=$id"

            return withContext(Dispatchers.IO) {
                val url = URL(urlGet)
                val connection = url.openConnection() as HttpURLConnection

                if (connection is HttpsURLConnection) {
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun checkServerTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? =
                            null
                    })
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                    connection.sslSocketFactory = sslContext.socketFactory
                    connection.hostnameVerifier = javax.net.ssl.HostnameVerifier { _, _ -> true }
                }
                try {
                    connection.requestMethod = "GET"
                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val bufferedReader =
                            BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuffer()
                        var inputLine: String?
                        while (bufferedReader.readLine().also { inputLine = it } != null) {
                            response.append(inputLine)
                        }
                        bufferedReader.close()
                        val gson = Gson()
                        val guitar = gson.fromJson(response.toString(), Guitar::class.java)
                        guitar
                    } else
                        null

                } finally {
                    connection.disconnect()
                }
            }
        }

        suspend fun getGuitars(ip:String, ids: String?, shape:String?, brand:String?, tremolo:String?, strings:String?, price:String?, patternToSearch: String?): Flow<List<Guitar>> = callbackFlow {
            var thereIsParams = false
            if (!isHostAccessible(ip))
                trySend(emptyList())
            try {
                var urlTmp = "https://$ip/list"
                if(patternToSearch != null)
                    urlTmp += "?patternToSearch=$patternToSearch"
                else {
                    if (ids != null) {
                        urlTmp += "?favs=$ids"
                        thereIsParams = true
                    }
                    if (shape != null) {
                        urlTmp += "?shape=$shape"
                        thereIsParams = true
                    }
                    if (brand != null) {
                        urlTmp += if (thereIsParams)
                            "&brand=$brand"
                        else
                            "?brand=$brand"
                        thereIsParams = true
                    }
                    if (tremolo != null) {
                        urlTmp += if (thereIsParams)
                            "&tremolo=${tremolo.lowercase().replace("si", "yes")}"
                        else
                            "?tremolo=${tremolo.lowercase().replace("si", "yes")}"
                        thereIsParams = true
                    }
                    if (strings != null) {
                        urlTmp += if (thereIsParams)
                            "&strings=$strings"
                        else
                            "?strings=$strings"
                        thereIsParams = true
                    }
                    if (price != null) {
                        urlTmp += if (thereIsParams)
                            "&price=$price"
                        else
                            "?price=$price"
                        thereIsParams = true
                    }
                }
                val url = URL(urlTmp)
                val connection = url.openConnection() as HttpURLConnection

                if (connection is HttpsURLConnection) {
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun checkServerTrusted(
                            chain: Array<out java.security.cert.X509Certificate>?,
                            authType: String?
                        ) {
                        }
                        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? =
                            null
                    })
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                    connection.sslSocketFactory = sslContext.socketFactory
                    connection.hostnameVerifier = javax.net.ssl.HostnameVerifier { _, _ -> true }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        connection.requestMethod = "GET"
                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val bufferedReader =
                                BufferedReader(InputStreamReader(connection.inputStream))
                            val response = StringBuffer()
                            var inputLine: String?
                            while (bufferedReader.readLine().also { inputLine = it } != null) {
                                response.append(inputLine)
                            }
                            bufferedReader.close()
                            val gson = Gson()
                            val guitars =
                                gson.fromJson(response.toString(), Array<Guitar>::class.java)
                                    .toList()
                            trySend(guitars).isSuccess
                        } else {
                            trySend(emptyList()).isSuccess
                        }
                    } catch (e: Exception) {
                        trySend(emptyList()).isSuccess
                        close(e)
                    } finally {
                        connection.disconnect()
                    }
                }
            } catch (e: Exception) {
                trySend(emptyList()).isSuccess
            }
            awaitClose()
        }
    }
}