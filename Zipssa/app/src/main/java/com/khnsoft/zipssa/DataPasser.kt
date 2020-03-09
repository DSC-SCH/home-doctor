package com.khnsoft.zipssa

import java.lang.Exception

class DataPasser {
    companion object {
        private val datas = mutableMapOf<Int, MyAlertPopup.Data>()

        fun insert(data: MyAlertPopup.Data) : Int{
            val id = datas.keys.max()?.plus(1) ?: 0
            datas[id] = data
            return id
        }

        fun pop(id: Int) : MyAlertPopup.Data? {
            val data = datas[id]
            if (data != null)
                datas.remove(id)
            return data
        }
    }
}