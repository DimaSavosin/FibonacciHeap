import java.util.Random;

public class Main {
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        int[] array = generateRandomArray(10000);

        // 1 задание: Вставка
        long totalOperationsInsert = 0;
        long totalTimeInsert = 0;

        for (int i = 0; i < array.length; i++) {
            long startTimeInsert = System.nanoTime(); // Записываем время начала операции

            // Вызываем метод insert с указанием индекса элемента
            heap.insert(array[i], i);

            long endTimeInsert = System.nanoTime(); // Записываем время окончания операции
            long insertTime = endTimeInsert - startTimeInsert; // Вычисляем время выполнения операции

            // Получаем количество операций после вставки
            long operationsAfterInsert = heap.getInsertOperations();
            // Обновляем общее количество операций
            totalOperationsInsert += operationsAfterInsert;

            // Обновляем общее время выполнения
            totalTimeInsert += insertTime;
            //System.out.println(operationsAfterInsert);
            //System.out.println(insertTime);
        }
        System.out.println("Общее время добавления: "+totalTimeInsert+" ns");
        System.out.println("Общее количество операций добавления: "+ totalOperationsInsert);

        double averageTimeInsert = totalTimeInsert / (double) array.length;
        double averageOperationsInsert = totalOperationsInsert / (double) array.length;
        System.out.println("Среднее время добавления: " + averageTimeInsert + " ns");
        System.out.println("Среднее количество операций добавления: " + averageOperationsInsert);

        // 2 задание: Поиск
        Random random = new Random();
        long totalOperationsSearch = 0;
        long totalTimeSearch = 0;

        for (int i = 0; i < 100; i++) { // Выполняем поиск для 100 случайных элементов
            int elementToFind = array[random.nextInt(array.length)]; // Случайный элемент из массива

            long startTimeSearch = System.nanoTime(); // Записываем время начала операции

            // Поиск элемента в куче
            boolean found = heap.contains(elementToFind);

            long endTimeSearch = System.nanoTime(); // Записываем время окончания операции
            long searchTime = endTimeSearch - startTimeSearch; // Вычисляем время выполнения операции
            // Получаем количество операций после поиска
            long operationsAfterSearch = heap.getSearchOperations();
            // Обновляем общее количество операций
            totalOperationsSearch += operationsAfterSearch;
            //System.out.println("Время поиска элемента "+ elementToFind+": "+searchTime);
            //System.out.println("Колличество операций поиска одного элемента: "+ operationsAfterSearch);
            // Обновляем общее время выполнения
            totalTimeSearch += searchTime;

        }
        System.out.println("Общее время поиска: "+totalTimeSearch+" ns");
        System.out.println("Общее количество операций поиска: "+ totalOperationsSearch);

        double averageTimeSearch = totalTimeSearch / 100.0;
        double averageOperationsSearch = totalOperationsSearch / 100.0;
        System.out.println("Среднее время поиска: " + averageTimeSearch + " ns");
        System.out.println("Среднее количество операций поиска: " + averageOperationsSearch);

        // 3 задание: Удаление
        long totalOperationsDelete = 0;
        long totalTimeDelete = 0;

        for (int i = 0; i < 1000; i++) { // Выполняем удаление для 1000 случайных элементов
            int elementToRemove = array[random.nextInt(array.length)]; // Случайный элемент из массива

            long startTimeDelete = System.nanoTime(); // Записываем время начала операции

            // Удаляем элемент из кучи
            boolean removed = heap.delete(elementToRemove);

            long endTimeDelete = System.nanoTime(); // Записываем время окончания операции
            long deleteTime = endTimeDelete - startTimeDelete; // Вычисляем время выполнения операции

            // Получаем количество операций после удаления
            long operationsAfterDelete = heap.getDeleteOperations();
            // Обновляем общее количество операций
            totalOperationsDelete += operationsAfterDelete;
            //System.out.println(deleteTime);
            //System.out.println(operationsAfterDelete);

            // Обновляем общее время выполнения
            totalTimeDelete += deleteTime;

        }
        System.out.println("Общее время удаления: "+totalTimeDelete+" ns");
        System.out.println("Общее количество операций удаления: "+ totalOperationsDelete);

        double averageTimeDelete = totalTimeDelete / 1000.0;
        double averageOperationsDelete = totalOperationsDelete / 1000.0;
        System.out.println("Среднее время удаления: " + averageTimeDelete + " ns");
        System.out.println("Среднее количество операций удаления: " + averageOperationsDelete);
    }

    // Генерация случайного массива
    private static int[] generateRandomArray(int length) {
        Random random = new Random();
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(10000);
        }
        return array;
    }
}
