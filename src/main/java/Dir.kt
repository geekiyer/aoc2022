class Dir(val name: String?, parent: Dir?) {
    private var parent: Dir? = null
    private val subDirectories: MutableList<Dir> = ArrayList()
    private val files: MutableMap<String, Int> = HashMap()

    init {
        this.parent = parent
    }

    val size: Int
        get() = (files.values.stream().mapToInt { f: Int? -> f!! }.sum()
                + subDirectories.stream().mapToInt { obj: Dir -> obj.size }.sum())
    val root: Dir?
        get() {
            var current: Dir? = this
            while (current!!.parent != null) {
                current = current.parent
            }
            return current
        }

    fun moveOut(): Dir {
        return if (parent == null) this else parent!!
    }

    fun moveIn(name: String?): Dir {
        val subDir = Dir(name, this)
        if (!subDirectories.contains(subDir)) {
            subDirectories.add(subDir)
            updateParent()
        }
        return subDir
    }

    fun addFile(name: String, size: Int) {
        files[name] = size
        updateParent()
    }

    fun addDirectory(name: String?) {
        val subDir = Dir(name, this)
        if (!subDirectories.contains(subDir)) {
            subDirectories.add(Dir(name, this))
            updateParent()
        }
    }

    private fun updateParent() {
        if (parent != null) {
            parent!!.replaceDirectory(this)
        }
    }

    private fun replaceDirectory(dir: Dir) {
        subDirectories.remove(dir)
        subDirectories.add(dir)
    }

    fun getSubDirectories(): List<Dir> {
        return subDirectories
    }

    fun getFiles(): Map<String, Int> {
        return files
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (name?.hashCode() ?: 0)
        result = prime * result + if (parent == null) 0 else parent.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as Dir
        if (name == null) {
            if (other.name != null) return false
        } else if (name != other.name) return false
        if (parent == null) {
            if (other.parent != null) return false
        } else if (parent != other.parent) return false
        return true
    }
}
