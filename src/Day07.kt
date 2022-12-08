import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.name
import kotlin.math.abs

typealias Command = Pair<String, List<String>>

private interface FsEntry {
    val name: String
}

private data class Directory(
    override val name: String,
    var children: List<FsEntry> = emptyList()
) : FsEntry

private data class File(
    override val name: String,
    val size: Int
) : FsEntry

fun main() {
    fun String.parseInput(): List<Command> {
        return split("$ ")
            .drop(1)
            .map {
                val lines = it.lines()
                lines.first() to lines.drop(1).dropLastWhile { line -> line == "" }
            }
    }

    fun findDir(currentPath: Path, root: Directory): Directory {
        var currentDir = root
        for (path in currentPath) {
            currentDir = currentDir.children
                .filterIsInstance<Directory>()
                .find { it.name == path.name } as Directory
        }
        return currentDir
    }

    fun List<Command>.buildFileTree(): Directory {
        var currentPath = Path("/")
        val fileTree = Directory("/")

        forEach { (command, output) ->
            if (command.startsWith("cd")) {
                val newDir = command.split(" ").last()

                /*val currentDir = findDir(currentPath, fileTree)
                currentDir.children += Directory(newDir)*/

                currentPath /= newDir
                currentPath = currentPath.normalize()
            } else if (command == "ls") {
                val currentDir = findDir(currentPath, fileTree)

                val newEntries = output.map {
                    val (sizeOrType, name) = it.split(" ")
                    if (sizeOrType == "dir") {
                        Directory(name)
                    } else {
                        File(name, sizeOrType.toInt())
                    }
                }

                currentDir.children += newEntries
            } else {
                error("Unknown comand $command")
            }
        }
        return fileTree
    }

    fun calcDirectorySize(directory: Directory): Int {
        val filesSize = directory.children
            .filterIsInstance<File>()
            .sumOf { it.size }

        val directoriesSize = directory.children
            .filterIsInstance<Directory>()
            .sumOf { calcDirectorySize(it) }

        return filesSize + directoriesSize
    }

    fun flattenTree(root: Directory): List<Directory> {
        return root.children
            .filterIsInstance<Directory>()
            .fold(emptyList()) { acc, directory ->
                acc + directory + flattenTree(directory)
            }
    }


    fun part1(input: List<Command>): Int {
        val fileTree = input.buildFileTree()

        val flattenTree = flattenTree(fileTree)
        val directorySize = flattenTree
            .map { it to calcDirectorySize(it) }

        return directorySize
            .filter { it.second <= 100_000 }
            .sumOf { it.second }
    }

    fun part2(input: List<Command>): Int {
        val fileTree = input.buildFileTree()

        val totalDiskSpace = 70_000_000
        val spaceRequired = 30_000_000
        val spaceUsed = calcDirectorySize(fileTree)
        val unusedSpace = totalDiskSpace - spaceUsed
        val spaceToClean = abs(unusedSpace - spaceRequired)


        val flattenTree = flattenTree(fileTree)
        val directorySize = flattenTree
            .map { it to calcDirectorySize(it) }

        return directorySize
            .filter { it.second >= spaceToClean }
            .minOf { it.second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputText("Day07_test").parseInput()
    check(part1(testInput) == 95_437)
    check(part2(testInput) == 24_933_642)

    val input = readInputText("Day07").parseInput()
    println(part1(input))
    println(part2(input))
}
