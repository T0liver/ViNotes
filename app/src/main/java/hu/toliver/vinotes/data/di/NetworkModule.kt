package hu.toliver.vinotes.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.toliver.vinotes.data.remote.api.CatalogApi
import hu.toliver.vinotes.ui.AppConstants
import javax.inject.Singleton
import java.net.InetAddress
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    @BootstrapClient
    fun provideBootstrapClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()

    @Provides
    @Singleton
    fun provideDnsFallback(@BootstrapClient dnsClient: OkHttpClient): Dns {
        val dohGoogle = DnsOverHttps.Builder()
            .client(dnsClient)
            .url("https://dns.google/dns-query".toHttpUrl())
            .bootstrapDnsHosts(
                InetAddress.getByName("8.8.8.8"),
                InetAddress.getByName("1.1.1.1")
            )
            .resolvePrivateAddresses(true)
            .build()

        val dohCloudflare = DnsOverHttps.Builder()
            .client(dnsClient)
            .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
            .bootstrapDnsHosts(
                InetAddress.getByName("1.1.1.1"),
                InetAddress.getByName("1.0.0.1")
            )
            .resolvePrivateAddresses(true)
            .build()

        return Dns { hostname ->
            // Try system resolver, then Google DoH, then Cloudflare DoH. If all fail, propagate the
            // original error to the caller so higher layers can surface an appropriate message.
            runCatching { Dns.SYSTEM.lookup(hostname) }
                .recoverCatching { dohGoogle.lookup(hostname) }
                .recoverCatching { dohCloudflare.lookup(hostname) }
                .getOrThrow()
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(dns: Dns): OkHttpClient =
        OkHttpClient.Builder()
            .dns(dns)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(AppConstants.DEFAULT_CATALOG_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideCatalogApi(retrofit: Retrofit): CatalogApi =
        retrofit.create(CatalogApi::class.java)
}