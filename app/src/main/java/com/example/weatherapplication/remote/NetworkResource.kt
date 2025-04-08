package com.example.weatherapplication.remote

import com.example.weatherapplication.data.NetworkResult
import com.example.weatherapplication.data.NetworkResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

class NetworkResource< out Output>(
    private val remoteFetch: suspend () -> Response<Output>?,
    private val refreshControl: RefreshControl = RefreshControl()
) : RefreshControl.Listener, ITimeLimitedResource by refreshControl, KoinComponent {

    private var localFetch: (suspend () -> List<Output>?)? = null
    private var localStore: (suspend (Output) -> Unit)? = null
    private var localDelete: (suspend () -> Unit)? = null

    private var localData: Output? = null

    constructor(
        remoteFetch: suspend () -> Response<Output>?,
        localFetch: suspend () -> List<Output>?,
        localStore: suspend (Output) -> Unit,
        localDelete: suspend () -> Unit,
        refreshControl: RefreshControl = RefreshControl()
    ) : this(remoteFetch, refreshControl) {
            this.localFetch = localFetch
            this.localStore = localStore
            this.localDelete = localDelete
    }

    init {
        refreshControl.addListener(this)
    }


    suspend fun query(force: Boolean = false): Flow<NetworkResult<Output?>> = flow {
        /**
         * if force is false local data is retrieved first
         */
        if (!force) {
            //calling local db
            fetchFromLocal()?.run {
                localData = this
                emit(NetworkResult.success(this))
            }
        }
        /**
         * if refresh time is expired or force is true, network call is made
         * NOTE : for first time, refreshControl.isExpired() always returns true
         */
        if (refreshControl.isExpired() || force) {
            //calling network call
            fetchFromRemote().run {
                /**
                 * if force is true, result is emitted
                 * if force is false and data from network is same as stored in local, then emitting the result and storing in local steps are skipped
                 */
                if (force || !isSameAsCached(this.data) || this.status == NetworkResultStatus.ERROR) {
                    emit(this)
                    kotlin.runCatching {
                        //storing in local is skipped if force is true
                        if (!force && data != null && this.status != NetworkResultStatus.ERROR) {
                            this.let { it1 ->
                                withContext(Dispatchers.IO) {
                                    it1.data?.let { localStore?.let { it2 -> it2(it) } }
                                }
                            }
                            refreshControl.refresh()
                        }
                    }.onFailure {
                        // Error
                    }

                }
            }
        }
    }

    /**
     * checks if output is same as the data fetched from db
     *
     * @param output is compared with local data
     * @return true if data are same or local data is null, else false
     */
    private fun isSameAsCached(output: Output?): Boolean {
        if (output == null) return false
        return localData?.let { deepEquals(output, it) } == true

    }

    fun <T : Any> deepEquals(obj1: T, obj2: T): Boolean {

        kotlin.runCatching {

            if (obj1 != obj2) return false
            // Get the properties of the objects using reflection
            val properties1 = obj1::class.declaredMemberProperties
            val properties2 = obj2::class.declaredMemberProperties

            // Check if the number of properties is the same
            if (properties1.size != properties2.size) return false

            // Iterate through the properties and compare their values
            for (property1 in properties1) {
                val property2 = properties2.find { it.name == property1.name } ?: return false
                val value1 = (property1 as? KProperty1<T, *>)?.get(obj1)
                val value2 = (property2 as? KProperty1<T, *>)?.get(obj2)

                if (property1.name == "primaryKey") continue
                if (property1.name == "playlistId") continue

                // If the properties have different values, return false
                if (value1 != value2) return false

                // If the properties are objects, recursively compare them
                if (value1 != null && value1 is Any && property1.returnType.isMarkedNullable.not()) {
                    if (!deepEquals(value1, value2 as Any)) {
                        return false
                    }
                }
            }

            // If all properties have the same values, return true
            return true
        }
        return false
    }

    override suspend fun cleanup() {
        deleteLocal()
    }


    private suspend fun deleteLocal() = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            localDelete?.let { it() }
        }
    }.getOrNull()

    /**
     * local db fetch call
     *
     */
    private suspend fun fetchFromLocal() = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            localFetch?.let { it() }?.get(0)
        }
    }.getOrNull()

    /**
     * network call
     *
     * @return output with Result wrapper class
     */
    private suspend fun fetchFromRemote() :  NetworkResult<Output?>{
        var res : Response<Output>? = null
        var networkResult : NetworkResult<Output?>? = null

        withContext(Dispatchers.IO){
            kotlin.runCatching {
                res = remoteFetch()
                networkResult = getDataFromResponse(res)
            }.onFailure {
                networkResult = if(it is UnknownHostException) {
                    NetworkResult.error(
                        localData,
                        "Please check your internet connection and try again later",
                        res?.code()
                    )
                } else {
                    NetworkResult.error(
                        localData,
                        it.message,
                        res?.code()
                    )
                }
            }
        }

        return networkResult ?: NetworkResult.error(localData,"",2)
    }

    /**
     * converts Retrofit Response to Result<Output>
     *
     * It also check for auth error. If error code is 401, access token is updated from API.
     * if both access and refresh token are expired then hardLogoutPostRefreshTokenFails is called
     *
     * @param response from network call
     * @return output with Result wrapper class
     */
    private suspend fun getDataFromResponse(response: Response<Output>?): NetworkResult<Output?> {
        if (response?.isSuccessful == true) {
            return NetworkResult.success(response.body(),response.code(),true)
        }
        var errorMessage =response?.errorBody()?.string()
        if(response?.errorBody() != null){
            kotlin.runCatching {
                val errorResponse = errorMessage?.let { JSONObject(it) }
                errorMessage = errorResponse?.getJSONObject("error")?.getString("message")
            }
        }
        return NetworkResult.error(localData,errorMessage,response?.code())
    }
}