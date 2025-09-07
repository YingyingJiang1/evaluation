    @Override
    public void run()
    {
        int ch;

        try
        {
_outerLoop:
            while (!__isClosed)
            {
                try
                {
                    if ((ch = __read(true)) < 0) {
                        break;
                    }
                }
                catch (InterruptedIOException e)
                {
                    synchronized (__queue)
                    {
                        __ioException = e;
                        __queue.notifyAll();
                        try
                        {
                            __queue.wait(100);
                        }
                        catch (InterruptedException interrupted)
                        {
                            if (__isClosed) {
                                break _outerLoop;
                            }
                        }
                        continue;
                    }
                } catch(RuntimeException re) {
                    // We treat any runtime exceptions as though the
                    // stream has been closed.  We close the
                    // underlying stream just to be sure.
                    super.close();
                    // Breaking the loop has the effect of setting
                    // the state to closed at the end of the method.
                    break _outerLoop;
                }

                // Process new character
                boolean notify = false;
                try
                {
                    notify = __processChar(ch);
                }
                catch (InterruptedException e)
                {
                    if (__isClosed) {
                        break _outerLoop;
                    }
                }

                // Notify input listener if buffer was previously empty
                if (notify) {
                    __client.notifyInputListener();
                }
            }
        }
        catch (IOException ioe)
        {
            synchronized (__queue)
            {
                __ioException = ioe;
            }
            __client.notifyInputListener();
        }

        synchronized (__queue)
        {
            __isClosed      = true; // Possibly redundant
            __hasReachedEOF = true;
            __queue.notify();
        }

        __threaded = false;
    }
