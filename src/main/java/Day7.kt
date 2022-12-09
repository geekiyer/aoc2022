private fun handleCd(command: String, currentDir: Dir): Dir {
    var currentDirectory = currentDir.root
    val parameter = command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
    currentDirectory = when (parameter) {
        "/" -> currentDir.root
        ".." -> currentDir.moveOut()
        else -> currentDir.moveIn(parameter)
    }
    return currentDirectory!!
}

private fun buildDirTree(input: List<String>): Dir {
    var currentDir = Dir("/", null)
    for (line in input) {
        if (line.startsWith("$")) {
            if (line.startsWith("$ cd")) {
                currentDir = handleCd(line, currentDir)
            }
        } else if (line.startsWith("dir")) {
            currentDir.addDirectory(line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1])
        } else {
            currentDir.addFile(
                line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1],
                line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].toInt())
        }
    }
    return currentDir.root!!
}

private fun getDirectoriesWithMaxSize(start: Dir, size: Int): List<Dir> {
    val result = mutableListOf<Dir>()
    if (start.size <= size) {
        result.add(start)
    }
    for (subDir in start.getSubDirectories()) {
        result.addAll(getDirectoriesWithMaxSize(subDir, size))
    }
    return result
}

private fun getDirectoriesWithMinSize(start: Dir, size: Int): List<Dir> {
    val result = mutableListOf<Dir>()
    if (start.size >= size) {
        result.add(start)
    }
    for (subDir in start.getSubDirectories()) {
        result.addAll(getDirectoriesWithMinSize(subDir, size))
    }
    return result
}

fun runPart2(input: List<String>): String {
    val root: Dir = buildDirTree(input)
    val minSizeToFree: Int = 30000000 - (70000000 - root.size)
    return getDirectoriesWithMinSize(root, minSizeToFree).stream().mapToInt(Dir::size).min().asInt.toString()
}

fun runPart1(input: List<String>): String {
    val root: Dir = buildDirTree(input)
    return getDirectoriesWithMaxSize(root, 100000).stream().mapToInt(Dir::size).sum().toString()
}

fun main() {
    val commands = object {}.javaClass.getResourceAsStream("day7.txt")?.bufferedReader()?.readLines()!!
    println(runPart1(commands))
    println(runPart2(commands))
}
