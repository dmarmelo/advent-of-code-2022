private typealias Command = Pair<String, List<String>>

sealed class FsEntry(
    val name: String
) {
    abstract val size: Int
}

private class Directory(
    name: String,
    var children: List<FsEntry> = emptyList()
) : FsEntry(name) {
    override val size: Int
        get() = children.sumOf { it.size }
}

private class File(
    name: String,
    override val size: Int,
) : FsEntry(name)

private fun Directory.findAllSubDirectorires(): List<Directory> =
    children
        .filterIsInstance<Directory>()
        .fold(emptyList()) { acc, directory ->
            acc + directory + directory.findAllSubDirectorires()
        }

private fun Directory.findSubDirectory(path: String): Directory? {
    val subPaths = path.split("/")
        .dropWhile { it == "" }
        .dropLastWhile { it == "" }

    var currentDirectory = this
    for (subPath in subPaths) {
        currentDirectory = currentDirectory.children
            .filterIsInstance<Directory>()
            .find { it.name == subPath } ?: return null
    }
    return currentDirectory
}

fun main() {
    fun String.parseInput(): List<Command> {
        return split("$ ")
            .drop(1)
            .map { it.trim() }
            .map {
                val lines = it.lines()
                lines.first() to lines.drop(1)
            }
    }

    fun List<Command>.buildFileTree(): Directory {
        var currentPath = "/"
        val root = Directory("/")

        forEach { (command, output) ->
            with(command) {
                when {
                    startsWith("cd") -> {
                        currentPath = when (val argument = command.removePrefix("cd ")) {
                            "/" -> "/"
                            ".." -> currentPath.substringBeforeLast("/", "/")
                            else ->
                                if (currentPath.endsWith("/")) currentPath + argument
                                else "$currentPath/$argument"
                        }
                    }

                    startsWith("ls") -> {
                        val newEntries = output.map {
                            val (sizeOrType, name) = it.split(" ")
                            if (sizeOrType == "dir") {
                                Directory(name)
                            } else {
                                File(name, sizeOrType.toInt())
                            }
                        }

                        root.findSubDirectory(currentPath)?.apply {
                            children += newEntries
                        } ?: error("Unknown path $currentPath")
                    }

                    else -> error("Unknown command $command")
                }
            }
        }
        return root
    }

    fun part1(root: Directory): Int {
        return root.findAllSubDirectorires()
            .map { it.size }
            .filter { it <= 100_000 }
            .sum()
    }

    fun part2(root: Directory): Int {
        val totalDiskSpace = 70_000_000
        val spaceRequired = 30_000_000
        val spaceUsed = root.size
        val unusedSpace = totalDiskSpace - spaceUsed
        val spaceToClean = spaceRequired - unusedSpace

        return root.findAllSubDirectorires()
            .map { it.size }
            .filter { it >= spaceToClean }
            .min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputText("Day07_test").parseInput().buildFileTree()
    check(part1(testInput) == 95_437)
    check(part2(testInput) == 24_933_642)

    val input = readInputText("Day07").parseInput().buildFileTree()
    println(part1(input))
    println(part2(input))
}
