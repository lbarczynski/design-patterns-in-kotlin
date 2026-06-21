package dev.bapps.behavioral

class Node(
    val value: Int,
    val children: MutableList<Node> = mutableListOf(),
) : Iterable<Node> {

    override fun iterator(): Iterator<Node> = bfs()
    fun dfs(): Iterator<Node> = DfsIterator(this)
    fun bfs(): Iterator<Node> = BfsIterator(this)
}

class DfsIterator(root: Node) : Iterator<Node> {
    private val stack = ArrayDeque<Node>().apply { addLast(root) }
    private val visited = hashSetOf<Node>()

    override fun hasNext(): Boolean = stack.isNotEmpty()

    override fun next(): Node {
        if (!hasNext())
            throw NoSuchElementException("No more nodes found!")

        val current = stack.removeLast()
        visited.add(current)
        
        current.children.reversed().forEach { child ->
            if (child !in visited) {
                visited.add(child)
                stack.addLast(child)
            }
        }
        return current
    }
}

class BfsIterator(root: Node) : Iterator<Node> {
    private val queue = ArrayDeque<Node>().apply { addLast(root) }
    private val visited = hashSetOf<Node>()

    override fun hasNext(): Boolean = queue.isNotEmpty()

    override fun next(): Node {
        if (!hasNext())
            throw NoSuchElementException("No more nodes found!")

        val current = queue.removeFirst()
        visited.add(current)
        
        current.children.forEach { child ->
            if (child !in visited) {
                visited.add(child)
                queue.addLast(child)
            }
        }
        return current
    }
}