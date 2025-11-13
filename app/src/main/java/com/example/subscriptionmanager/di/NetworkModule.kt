import com.example.subscriptionmanager.data.remote.api.`MockSubscriptionService.kt`
import com.example.subscriptionmanager.data.remote.api.`SubscriptionApi.kt`
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): `SubscriptionApi.kt` =
        retrofit.create(`SubscriptionApi.kt`::class.java)

    // MockAPI
    @Provides
    @Singleton
    // 이제 MockService를 SubscriptionApi 타입으로 제공
    fun provideSubscriptionApi(): `SubscriptionApi.kt` {
        return `MockSubscriptionService.kt`()
    }
}
