import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

private fun inputFile(name: String) = File("src", "$name.txt")

/**
 * Reads all text from the given input txt file.
 */
fun readInputText(name: String) = inputFile(name).readText()

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = inputFile(name).readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun Sequence<Int>.product(): Int =
        reduce { acc, item -> acc * item }

fun Iterable<Int>.product(): Int =
        reduce { acc, item -> acc * item }

fun Iterable<Long>.product(): Long =
        reduce { acc, item -> acc * item }
