package com.brahmand;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import rx.functions.Action1;

/**
 * Created by adarshpandey on 11/16/15.
 */
public class Queue<T> {
    private static int runningId;

    @Nullable
    final T defaultEvent;

    @NotNull
    public final Class<T> eventType;
    final int id;

    @NotNull
    public final String name;

    @Nullable
    final Action1<Throwable> onError;
    final boolean replayLast;

    Queue(@NotNull String paramString, @NotNull Class<T> paramClass, boolean paramBoolean, @Nullable T paramT, @Nullable Action1<Throwable> paramb)
    {
        this.name = paramString;
        this.eventType = paramClass;
        this.replayLast = paramBoolean;
        this.defaultEvent = paramT;
        this.onError = paramb;
        int i = runningId;
        runningId = i + 1;
        this.id = i;
    }

    public static <T> Builder<T> of(Class<T> paramClass)
    {
        return new Builder<>(paramClass);
    }

    public final boolean equals(Object paramObject)
    {
        return (paramObject != null) && ((paramObject instanceof Queue)) && (((Queue)paramObject).id == this.id);
    }

    public final int hashCode()
    {
        return this.id;
    }

    public final String toString()
    {
        return this.name + "[" + this.eventType.getCanonicalName() + "]";
    }

    public static final class Builder<T>
    {
        private T defaultEvent;
        private final Class<T> eventType;
        private String name;
        private Action1<Throwable> onError;
        private boolean replayLast;

        Builder(Class<T> paramClass)
        {
            this.eventType = paramClass;
        }

        public final Queue<T> get()
        {
            if (this.name == null)
                this.name = (this.eventType.getSimpleName() + "Queue");
            return new Queue<>(this.name, this.eventType, this.replayLast, this.defaultEvent, this.onError);
        }

        public final Builder<T> name(String paramString)
        {
            this.name = paramString;
            return this;
        }

        public final Builder<T> onError(Action1<Throwable> paramb)
        {
            this.onError = paramb;
            return this;
        }

        public final Builder<T> replay()
        {
            this.replayLast = true;
            return this;
        }

        public final Builder<T> replay(T paramT)
        {
            this.replayLast = true;
            this.defaultEvent = paramT;
            return this;
        }
    }
}
