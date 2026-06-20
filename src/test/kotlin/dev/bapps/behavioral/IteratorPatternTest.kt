package dev.bapps.behavioral

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class IteratorPatternTest {

    @Test
    fun `GIVEN tree WHEN iterate with DFS iterator THEN should iterate depth-first`() {
        // given
        val root = Node(
            value = 1,
            children = mutableListOf(
                Node(value = 2, children = mutableListOf(Node(value = 4), Node(value = 5))),
                Node(value = 3, children = mutableListOf(Node(value = 6), Node(value = 7)))
            )
        )

        // when
        val discovered = mutableListOf<Int>()
        for (node in root.dfs()) {
            discovered += node.value
        }

        // then
        val expectedValues = listOf(1, 2, 4, 5, 3, 6, 7)
        assertContentEquals(expectedValues, discovered)
    }

    @Test
    fun `GIVEN tree WHEN iterate with BFS iterator THEN should iterate breadth-first`() {
        // given
        val root = Node(
            value = 1,
            children = mutableListOf(
                Node(value = 2, children = mutableListOf(Node(value = 4), Node(value = 5))),
                Node(value = 3, children = mutableListOf(Node(value = 6), Node(value = 7)))
            )
        )

        // when
        val discovered = mutableListOf<Int>()
        for (node in root.bfs()) {
            discovered += node.value
        }

        // then
        val expectedValues = listOf(1, 2, 3, 4, 5, 6, 7)
        assertContentEquals(expectedValues, discovered)
    }

    @Test
    fun `GIVEN cycle graph WHEN iterate THEN should omit duplicated nodes`() {
        // given
        val root = Node(1, children = mutableListOf(Node(value = 2), Node(value = 3)))
        root.first().children.add(root)
        root.last().children.add(root)

        // when
        val discovered = mutableListOf<Int>()
        for (node in root) {
            discovered += node.value
        }

        // then
        val expectedValues = listOf(1, 2, 3)
        assertContentEquals(expectedValues, discovered)
    }
}