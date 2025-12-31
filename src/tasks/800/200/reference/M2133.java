    @SuppressWarnings("unchecked")
    @NonNull
    private U assertError(@NonNull Predicate<Throwable> errorPredicate, boolean exact) {
        int s = errors.size();
        if (s == 0) {
            throw fail("No errors");
        }

        boolean found = false;

        for (Throwable e : errors) {
            try {
                if (errorPredicate.test(e)) {
                    found = true;
                    break;
                }
            } catch (Throwable ex) {
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }

        if (found) {
            if (s != 1) {
                if (exact) {
                    throw fail("Error present but other errors as well");
                }
                throw fail("One error passed the predicate but other errors are present as well");
            }
        } else {
            if (exact) {
                throw fail("Error not present");
            }
            throw fail("No error(s) passed the predicate");
        }
        return (U)this;
    }
