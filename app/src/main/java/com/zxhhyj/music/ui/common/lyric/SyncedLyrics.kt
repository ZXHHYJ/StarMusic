package com.zxhhyj.music.ui.common.lyric

class SyncedLyrics(
    val main: List<Pair<Long, String>>,
    val translation: List<Pair<Long, String>>,
    val all: List<Pair<Long, String>>
) {
    fun isEmpty(): Boolean = main.isEmpty() && translation.isEmpty()

    companion object {

        private fun mergeLyrics(
            originalLyrics: List<Pair<Long, String>>,
            translatedLyrics: List<Pair<Long, String>>
        ): List<Pair<Long, String>> {
            val mergedLyrics = mutableListOf<Pair<Long, String>>()

            var originalIndex = 0
            var translatedIndex = 0

            while (originalIndex < originalLyrics.size && translatedIndex < translatedLyrics.size) {
                val originalLyric = originalLyrics[originalIndex]
                val translatedLyric = translatedLyrics[translatedIndex]

                if (originalLyric.first == translatedLyric.first) {
                    val mergedLyric = Pair(
                        originalLyric.first,
                        "${originalLyric.second}\n${translatedLyric.second}"
                    )
                    mergedLyrics.add(mergedLyric)
                    originalIndex++
                    translatedIndex++
                } else if (originalLyric.first < translatedLyric.first) {
                    mergedLyrics.add(originalLyric)
                    originalIndex++
                } else {
                    mergedLyrics.add(translatedLyric)
                    translatedIndex++
                }
            }

            while (originalIndex < originalLyrics.size) {
                mergedLyrics.add(originalLyrics[originalIndex])
                originalIndex++
            }

            while (translatedIndex < translatedLyrics.size) {
                mergedLyrics.add(translatedLyrics[translatedIndex])
                translatedIndex++
            }

            return mergedLyrics
        }

        fun List<String>.toSyncedLyrics(): SyncedLyrics {
            val lyrics = mutableMapOf<Long, String>()
            val translations = mutableMapOf<Long, String>()
            val lineRegex = "\\d{1,3}:\\d{1,2}\\.\\d{2,3}".toRegex()
            val timeRegex = "\\[(.*?)]".toRegex()

            this.forEach { line ->
                val lyric = line.substringAfterLast(']').trim().replace("&quot;", "\"")
                val matches = timeRegex.findAll(line)

                for (match in matches) {
                    val time = match.groupValues[1]
                    if (time.matches(lineRegex) && lyric != "//" && lyric.isNotEmpty()) {
                        val (minute, second, millisecond) = time.split(':', '.')
                        val milliseconds = minute.toLong() * 60_000 +
                                second.toLong() * 1000 +
                                millisecond.toLong() * when (millisecond.length) {
                            3 -> 1
                            2 -> 10
                            else -> error("Invalid millisecond format")
                        }

                        if (milliseconds !in lyrics) {
                            lyrics += milliseconds to lyric
                        } else {
                            translations += milliseconds to lyric
                        }
                    }
                }
            }

            val main = lyrics.toList().sortedBy { it.first }
            val translation = translations.toList().sortedBy { it.first }
            return SyncedLyrics(
                main = main,
                translation = translation,
                all = mergeLyrics(main, translation)
            )
        }
    }
}
