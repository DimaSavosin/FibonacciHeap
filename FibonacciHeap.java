import java.util.ArrayList;
import java.util.List;
class FibonacciHeapNode {
    int key;
    int degree;
    boolean marked;
    FibonacciHeapNode parent;
    FibonacciHeapNode child;
    FibonacciHeapNode left;
    FibonacciHeapNode right;


    public FibonacciHeapNode(int key) {
        this.key = key;
        this.degree = 0;
        this.marked = false;
        this.parent = null;
        this.child = null;
        this.left = this;
        this.right = this;
    }
}

public class FibonacciHeap {
    private FibonacciHeapNode minNode;
    private int size;
    private long insertOperations;
    private long searchOperations;
    private long deleteOperations;



    public FibonacciHeap() {
        minNode = null;
        size = 0;
        insertOperations = 0;
        searchOperations = 0;
        deleteOperations = 0;

    }



    public void insert(int key, int elementIndex) { // добавление


        long startTime = System.nanoTime();
        insertOperations++;

        FibonacciHeapNode newNode = createNewNode(key);
        linkNode(newNode);
        updateMinNode(newNode);

        size++;
        long endTime = System.nanoTime();
        long duration = endTime - startTime;


    }


    private FibonacciHeapNode createNewNode(int key) {//создания нового узла
        insertOperations++; // Операция создания нового узла
        return new FibonacciHeapNode(key);
    }

    private void linkNode(FibonacciHeapNode newNode) {
        if (minNode == null) {
            minNode = newNode;
            return;
        }

        insertOperations += 4; // Увеличиваем счетчик для операций установки связей
        newNode.left = minNode;
        newNode.right = minNode.right;
        minNode.right = newNode;
        newNode.right.left = newNode;
    }

    private void updateMinNode(FibonacciHeapNode newNode) {
        if (newNode.key < minNode.key) {
            insertOperations++; // Операция сравнения ключей
            minNode = newNode;
        }
    }


    private void merge() { // объединение 2 куч
        List<FibonacciHeapNode> rootList = new ArrayList<>();
        FibonacciHeapNode current = minNode;
        do {
            rootList.add(current);
            current = current.right;
        } while (current != minNode);

        int maxDegree = 2 * size;
        FibonacciHeapNode[] degreeRoots = new FibonacciHeapNode[maxDegree];

        for (FibonacciHeapNode node : rootList) {
            int degree = node.degree;
            FibonacciHeapNode next = node;
            do {
                FibonacciHeapNode other = degreeRoots[degree];
                if (other == null) {
                    degreeRoots[degree] = next;
                    break;
                }
                if (next.key > other.key) {
                    FibonacciHeapNode temp = next;
                    next = other;
                    other = temp;
                }
                link(other, next);
                degreeRoots[degree] = null;
                degree++;
            } while (true);
        }

        minNode = null;
        for (FibonacciHeapNode node : degreeRoots) {
            if (node != null) {
                if (minNode == null) {
                    minNode = node;
                } else {
                    node.left.right = node.right;
                    node.right.left = node.left;
                    node.left = minNode;
                    node.right = minNode.right;
                    minNode.right = node;
                    node.right.left = node;
                    if (node.key < minNode.key) {
                        minNode = node;
                    }
                }
            }
        }
    }


    private void link(FibonacciHeapNode child, FibonacciHeapNode parent) {
        // Убираем child из списка корней и делаем его дочерним узлом parent
        child.left.right = child.right;
        child.right.left = child.left;

        // Устанавливаем parent в качестве родителя child
        child.parent = parent;

        // Если у parent нет дочерних узлов, child становится его первым дочерним узлом
        if (parent.child == null) {
            parent.child = child;
            child.left = child;
            child.right = child;
        } else {
            // Вставляем child в список дочерних узлов parent
            child.left = parent.child;
            child.right = parent.child.right;
            parent.child.right = child;
            child.right.left = child;
        }

        // Увеличиваем степень родительского узла
        parent.degree++;
    }


    public int getMin() { // получение минимального элемента
        if (minNode == null)
            return Integer.MAX_VALUE;
        return minNode.key;
    }

    public boolean delete(int key) { // удаление
        deleteOperations++; // Увеличиваем счетчик операций в самом начале метода

        FibonacciHeapNode node = findNode(key);
        if (node == null)
            return false;

        removeNodeFromRootList(node); // Удаление узла из списка корней
        markChildNodesAsRoots(node); // Пометка дочерних узлов как корней
        mergeChildListWithRootList(node); // Слияние списков корней и детей удаленного узла
        updateMinNodeAfterDelete(node); // Обновление minNode
        size--;

        return true;
    }

    private void removeNodeFromRootList(FibonacciHeapNode node) { //удаления узла из списка корней
        deleteOperations += 3; // Операции удаления узла из списка корней
        if (node.right == node) {
            minNode = null;
        } else {
            node.left.right = node.right;
            node.right.left = node.left;
            if (node == minNode) {
                minNode = node.right;
            }
        }
    }

    private void markChildNodesAsRoots(FibonacciHeapNode node) {//пометки дочерних узлов как корней
        if (node.child != null) {
            deleteOperations++; // Операция проверки наличия дочерних узлов
            FibonacciHeapNode child = node.child;
            do {
                FibonacciHeapNode next = child.right;
                child.parent = null;
                child = next;
                deleteOperations += 2; // Операции пометки дочерних узлов как корней
            } while (child != node.child);
        }
    }

    private void mergeChildListWithRootList(FibonacciHeapNode node) {
        if (node.child != null) {
            if (minNode == null) {
                minNode = node.child;
            } else {
                deleteOperations += 8; // Операции слияния списков корней и детей удаленного узла
                FibonacciHeapNode temp = minNode.right;
                minNode.right = node.child;
                node.child.left = minNode;
                temp.left = node.child.right;
                node.child.right.left = temp;
                node.child.right = temp;
                temp.right = node.child;
            }
        }
    }

    private void updateMinNodeAfterDelete(FibonacciHeapNode node) {
        if (minNode == node) {
            consolidate();
            deleteOperations++; // Операция обновления minNode
        }
    }

    private void consolidate() {
        // Размер таблицы зависит от размера кучи. Логарифмическая сложность алгоритма.
        double goldenRatio = (1 + Math.sqrt(5)) / 2;
        int tableSize = (int) Math.ceil(Math.log(size) / Math.log(goldenRatio));
        FibonacciHeapNode[] table = new FibonacciHeapNode[tableSize];

        // Объединение узлов с одинаковым порядком
        FibonacciHeapNode current = minNode;
        do {
            FibonacciHeapNode next = current.right;
            int degree = current.degree;
            while (table[degree] != null) {
                FibonacciHeapNode other = table[degree];
                if (current.key > other.key) {
                    FibonacciHeapNode temp = current;
                    current = other;
                    other = temp;
                }
                link(other, current);
                table[degree] = null;
                degree++;
            }
            table[degree] = current;
            current = next;
        } while (current != minNode);

        // Обновление minNode
        minNode = null;
        for (int i = 0; i < tableSize; i++) {
            if (table[i] != null) {
                if (minNode == null) {
                    minNode = table[i];
                } else {
                    table[i].left.right = table[i].right;
                    table[i].right.left = table[i].left;
                    table[i].left = minNode;
                    table[i].right = minNode.right;
                    minNode.right = table[i];
                    table[i].right.left = table[i];
                    if (table[i].key < minNode.key) {
                        minNode = table[i];
                    }
                }
            }
        }
    }
    public int getSize() {
        return size;
    }

    public FibonacciHeapNode findNode(int key) { //  поиска узла с определенным ключом в куче
        if (minNode == null)
            return null;

        // Используем хэш-таблицу для хранения ссылок на узлы кучи
        int tableSize = (int) (Math.log(size) / Math.log(2)) + 1;
        FibonacciHeapNode[] table = new FibonacciHeapNode[tableSize];
        return findNode(minNode, key, table);
    }

    private FibonacciHeapNode findNode(FibonacciHeapNode startNode, int key, FibonacciHeapNode[] table) {
        FibonacciHeapNode current = startNode;
        FibonacciHeapNode foundNode = null;

        do {
            searchOperations++;
            if (current.key == key) {
                foundNode = current;
                break;
            }

            FibonacciHeapNode child = current.child;
            if (child != null) {
                if (table[current.degree] == null) {
                    table[current.degree] = current;
                    FibonacciHeapNode childFoundNode = findNode(child, key, table);
                    if (childFoundNode != null) {
                        foundNode = childFoundNode;
                        break;
                    }
                }
            }

            current = current.right;
        } while (current != startNode);

        return foundNode;
    }


    public void decreaseKey(int key, int newKey) {
        FibonacciHeapNode node = findNode(key);
        if (node != null)
            decreaseKey(node, newKey);
    }

    private void decreaseKey(FibonacciHeapNode node, int newKey) { // уменьшение ключа
        if (newKey > node.key)
            return;

        node.key = newKey;
        FibonacciHeapNode parent = node.parent;
        if (parent != null && node.key < parent.key) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.key < minNode.key)
            minNode = node;
    }

    private void cut(FibonacciHeapNode child, FibonacciHeapNode parent) { // отрезание узла от родителя
        child.left.right = child.right;
        child.right.left = child.left;
        parent.degree--;
        if (parent.child == child)
            parent.child = child.right;
        if (parent.degree == 0)
            parent.child = null;
        child.left = minNode;
        child.right = minNode.right;
        minNode.right = child;
        child.right.left = child;
        child.parent = null;
        child.marked = false;
    }

    private void cascadingCut(FibonacciHeapNode node) {
        FibonacciHeapNode parent = node.parent;
        if (parent != null) {
            if (!node.marked)
                node.marked = true;
            else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }

    public boolean contains(int key) { // поиск
        searchOperations = 0;
        return findNode(key) != null;
    }
    public long getInsertOperations() {
        return insertOperations;
    }
    public long getSearchOperations() {
        return searchOperations;
    }

    public long getDeleteOperations() {
        return deleteOperations;
    }
    public void resetInsertOperations() {
        insertOperations = 0;
    }
    public void resetDeletOperations(){
        deleteOperations = 0;
    }
}
