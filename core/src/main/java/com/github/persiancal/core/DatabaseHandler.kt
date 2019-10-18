package com.github.persiancal.core

import android.content.Context
import com.github.persiancal.core.db.*
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.query

class DatabaseHandler {
    companion object {
        private var instance: DatabaseHandler = DatabaseHandler()
        private var boxStore: BoxStore? = null
        private lateinit var remoteJalaliEventsDbBox: Box<RemoteJalaliEventsDb>
        private lateinit var remoteHijriEventsDbBox: Box<RemoteHijriEventsDb>
        private lateinit var remoteGregorianEventsDbBox: Box<RemoteGregorianEventsDb>

        private lateinit var localJalaliEventsDbBox: Box<LocalJalaliEventsDb>
        private lateinit var localHijriEventsDbBox: Box<LocalHijriEventsDb>
        private lateinit var localGregorianEventsDbBox: Box<LocalGregorianEventsDb>

        fun init(
            context: Context
        ) {
            if (boxStore == null) {
                boxStore = MyObjectBox.builder()
                    .androidContext(context.applicationContext)
                    .build()
            }
            boxStore!!.let {
                remoteJalaliEventsDbBox = it.boxFor(
                    RemoteJalaliEventsDb::class.java
                )
                remoteHijriEventsDbBox = it.boxFor(
                    RemoteHijriEventsDb::class.java
                )
                remoteGregorianEventsDbBox = it.boxFor(
                    RemoteGregorianEventsDb::class.java
                )
                localJalaliEventsDbBox = it.boxFor(
                    LocalJalaliEventsDb::class.java
                )
                localHijriEventsDbBox = it.boxFor(
                    LocalHijriEventsDb::class.java
                )
                localGregorianEventsDbBox = it.boxFor(
                    LocalGregorianEventsDb::class.java
                )
            }
        }

        fun getInstance(): DatabaseHandler {
            return instance
        }
    }

    fun isRemoteJalaliReady(): Boolean {
        return remoteJalaliEventsDbBox.count() != 0L
    }

    fun isRemoteHijriReady(): Boolean {
        return remoteHijriEventsDbBox.count() != 0L
    }

    fun isRemoteGregorianReady(): Boolean {
        return remoteGregorianEventsDbBox.count() != 0L
    }

    fun getRemoteJalaliEvents(dayOfMonth: Int, month: Int): MutableList<RemoteJalaliEventsDb>? {
        val query = remoteJalaliEventsDbBox.query {
            equal(RemoteJalaliEventsDb_.month, month)
            equal(RemoteJalaliEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun getRemoteHijriEvents(dayOfMonth: Int, month: Int): MutableList<RemoteHijriEventsDb>? {
        val query = remoteHijriEventsDbBox.query {
            equal(RemoteHijriEventsDb_.month, month)
            equal(RemoteHijriEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun getRemoteGregorianEvents(
        dayOfMonth: Int,
        month: Int
    ): MutableList<RemoteGregorianEventsDb>? {
        val query = remoteGregorianEventsDbBox.query {
            equal(RemoteGregorianEventsDb_.month, month)
            equal(RemoteGregorianEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun putRemoteJalaliEvents(jalaliDb: RemoteJalaliEventsDb) {
        remoteJalaliEventsDbBox.put(jalaliDb)
    }

    fun putRemoteHijriEvents(hijriDb: RemoteHijriEventsDb) {
        remoteHijriEventsDbBox.put(hijriDb)
    }

    fun putRemoteGregorianEvents(gregorianDb: RemoteGregorianEventsDb) {
        remoteGregorianEventsDbBox.put(gregorianDb)
    }

    fun isLocalJalaliReady(): Boolean {
        return localJalaliEventsDbBox.count() != 0L
    }

    fun isLocalHijriReady(): Boolean {
        return localHijriEventsDbBox.count() != 0L
    }

    fun isLocalGregorianReady(): Boolean {
        return localGregorianEventsDbBox.count() != 0L
    }

    fun getLocalJalaliEvents(dayOfMonth: Int, month: Int): MutableList<LocalJalaliEventsDb>? {
        val query = localJalaliEventsDbBox.query {
            equal(LocalJalaliEventsDb_.month, month)
            equal(LocalJalaliEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun getLocalHijriEvents(dayOfMonth: Int, month: Int): MutableList<LocalHijriEventsDb>? {
        val query = localHijriEventsDbBox.query {
            equal(LocalHijriEventsDb_.month, month)
            equal(LocalHijriEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun getLocalGregorianEvents(dayOfMonth: Int, month: Int): MutableList<LocalGregorianEventsDb>? {
        val query = localGregorianEventsDbBox.query {
            equal(LocalGregorianEventsDb_.month, month)
            equal(LocalGregorianEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun putLocalJalaliEvents(jalaliEventsDb: LocalJalaliEventsDb) {
        localJalaliEventsDbBox.put(jalaliEventsDb)
    }

    fun putLocalHijriEvents(hijriEventsDb: LocalHijriEventsDb) {
        localHijriEventsDbBox.put(hijriEventsDb)
    }

    fun putLocalGregorianEvents(gregorianEventsDb: LocalGregorianEventsDb) {
        localGregorianEventsDbBox.put(gregorianEventsDb)
    }
}