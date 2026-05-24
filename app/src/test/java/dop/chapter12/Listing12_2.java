package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Listing12_2 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.2
     * ───────────────────────────────────────────────────────
     * Another integration test
     * ───────────────────────────────────────────────────────
     */
    @Test
    void taskGroupsAreLeasedForTheirPlannedDuration()
            throws InterruptedException {
        MyStorage storage = myDependencyInjector.myStorage();
        // Tasks in the same Group are "loosely mutually exclusive"
        // and cannot be scheduled at the same time
        TaskGroup group = new TaskGroup("test-" + UUID.randomUUID());
        Task task = Task.builder()
            .id(UUID.randomUUID())
            .group(group)
            .arrivedAt(Instant.now())
            // tasks are leased until their expected completion
            // time. Once expired, new tasks can be scheduled even
            // if this one is still running.
            .completeBy(Instant.now().plusSeconds(2))
            .build();

        // We're starting with a clean slate. Tasks can be
        // freely saved
        storage.save(task);
        // And we find them as active in the task group
        assertEquals(
            storage.findActive(task.taskGroup()),
            Optional.of(task)
        );

        // But if we make another task in the same group
        Task duplicateTask = task.withId(UUID.randomUUID());

        // It's rejected because the active task still holds
        // exclusive rights
        assertThrows(ConflictException.class, () -> {
            storage.save(duplicateTask);
        });
        assertEquals(
            storage.findActive(duplicateTask.taskGroup()),
            Optional.of(task)
        );
        // Letting the lease elapse
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        // now we don't find anything in the task group
        // It might still be running, but it's irrelevant to
        // the scheduler.
        assertEquals(
            storage.findActive(task.taskGroup()),
            Optional.empty()
        );
        // and now the orignial lease expired, our previously
        // rejected task can be scheduled
        storage.save(duplicateTask);
        // and becomes the active task
        assertEquals(
            storage.findActive(duplicateTask.taskGroup()),
            Optional.of(duplicateTask)
        );
    }















    MyDependencyInjector myDependencyInjector;
    record TaskGroup(String value){}
    record Task(UUID id, TaskGroup taskGroup, Instant arrivedAt, Instant completeBy) {
        static Builder builder() { return new Builder(); }
        Task withId(UUID id) { return new Task(id, taskGroup, arrivedAt, completeBy); }
    }
    static class Builder {
        Builder id(UUID id) { return this; }
        Builder group(TaskGroup group) { return this; }
        Builder arrivedAt(Instant arrivedAt) { return this; }
        Builder completeBy(Instant completeBy) { return this; }
        Task build() { return null; }
    }
    interface MyStorage {
        void save(Task task);
        Optional<Task> findActive(TaskGroup group);
    }
    interface MyDependencyInjector {
        MyStorage myStorage();
    }
    static class ConflictException extends RuntimeException {}
}

