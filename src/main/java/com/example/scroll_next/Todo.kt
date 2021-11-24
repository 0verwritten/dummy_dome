package com.example.scroll_next

import android.util.Log
import java.io.FileInputStream

data class Todo(val title: String, val DateTimer: String, var isChecked: Boolean = false){

    override fun toString() : String{
        return "Title: $title\nDate: ${DateTimer.replace('\n', '\t')}\nCheckness: $isChecked\n";
    }
    companion object{
        enum class Position (val pos : Int) { title(0), date(1), checkness(2) }

        fun fromString(stream: FileInputStream) : MutableList<Todo> {
            Log.e("something", "")
            var obj: MutableList<Todo> = arrayListOf();
            var stream = stream.reader().readLines();
            var pos : Position = Position.title;

            run {
                var title: String = "";
                var date: String = "";
                var checkness: String;
                Log.e("file", stream.toString());
                stream.forEach {
                    if (it != "]"&& it != "[]") {
                        when (pos) {
                            Position.title -> {
                                title = it.split("Title: ")[1];
                                pos = Position.date;
                            }
                            Position.date -> {
                                try {
                                    date = it.split("Date:")[1].trimStart();
                                } catch (e: IndexOutOfBoundsException) {
                                    date = "";
                                }
                                pos = Position.checkness;
                            }
                            Position.checkness -> if(it.contains("Checkness")){
                                try {
                                    checkness = it.split("Checkness: ")[1];
                                } catch (e: IndexOutOfBoundsException) {
                                    checkness = "";
                                }
                                obj.add(Todo(title, date, checkness == "true"));
                                pos = Position.title;
                            }
                        }
                    }
                };
            }

            return obj;
        }
    }
}
