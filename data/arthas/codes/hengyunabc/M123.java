    private boolean __processChar(int ch) throws InterruptedException
    {
        // Critical section because we're altering __bytesAvailable,
        // __queueTail, and the contents of _queue.
        boolean bufferWasEmpty;
        synchronized (__queue)
        {
            bufferWasEmpty = (__bytesAvailable == 0);
            while (__bytesAvailable >= __queue.length - 1)
            {
                // The queue is full. We need to wait before adding any more data to it. Hopefully the stream owner
                // will consume some data soon!
                if(__threaded)
                {
                    __queue.notify();
                    try
                    {
                        __queue.wait();
                    }
                    catch (InterruptedException e)
                    {
                        throw e;
                    }
                }
                else
                {
                    // We've been asked to add another character to the queue, but it is already full and there's
                    // no other thread to drain it. This should not have happened!
                    throw new IllegalStateException("Queue is full! Cannot process another character.");
                }
            }

            // Need to do this in case we're not full, but block on a read
            if (__readIsWaiting && __threaded)
            {
                __queue.notify();
            }

            __queue[__queueTail] = ch;
            ++__bytesAvailable;

            if (++__queueTail >= __queue.length) {
                __queueTail = 0;
            }
        }
        return bufferWasEmpty;
    }
